package project.findit.ui.flickr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import project.findit.R;
import project.findit.model.flickr.FlickrFetchr;
import project.findit.model.flickr.GalleryItem;
import project.findit.model.flickr.QueryPreferences;
import project.findit.model.flickr.ThumbnailDownloader;

/**
 * PHOTO GALLERY FRAGMENT
 * Displays the images available to the user,
 *  downloads and saves image when a user clicks it
 */
public class PhotoGalleryFragment extends Fragment {

    public static final String FILE_CUSTOM_DRAWABLE = "file_custom_drawable";
    public static final String TAG_DOWNLOAD_IMAGE = "DownloadImage";

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView recyclerViewPhoto;
    private List<GalleryItem> items = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> thumbnailDownloader;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updateItems();

        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        thumbnailDownloader.setThumbnailDownloadListener(
            new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                @Override
                public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    photoHolder.bindDrawable(drawable);
                }
            }
        );
        thumbnailDownloader.start();
        thumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        recyclerViewPhoto = view.findViewById(R.id.recycler_photo);
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_flickr);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupAdapter();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s);
                updateItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
        new FetchItemsTask(query).execute();
    }

    private void setupAdapter() {
        if (isAdded()) {
            recyclerViewPhoto.setAdapter(new PhotoAdapter(items));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewItem;

        public PhotoHolder(final View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.item_image_view);
        }

        public void registerClicks(final int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("caption: " + items.get(position).getCaption());
                    System.out.println("ID: " + items.get(position).getId());
                    System.out.println("url: " + items.get(position).getUrl());

                    itemView.setForeground(getResources().getDrawable(R.drawable.gallery_item_selected));

                    // Got help from:
                    //  https://stackoverflow.com/questions/39897338/how-to-get-current-time-stamp-in-android/39897615
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("PDT"));

                    String bitmapUrl = items.get(position).getUrl();
                    String imageName = items.get(position).getCaption();

                    String temp = sdf.format(new Date());
                    imageName += temp;
                    imageName += ".png";

                    new DownloadFile(bitmapUrl, imageName).execute();
                }
            });
        }

        public void bindDrawable(Drawable drawable) {
            imageViewItem.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> galleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            this.galleryItems = galleryItems;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = galleryItems.get(position);
            Drawable placeholder = getResources().getDrawable(R.drawable.magnifying_glass);
            photoHolder.bindDrawable(placeholder);
            photoHolder.registerClicks(position);
            thumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return galleryItems.size();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>> {

        private String query;

        public FetchItemsTask(String query) {
            this.query = query;
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {

            if (query == null) {
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                return new FlickrFetchr().searchPhotos(query);
            }
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            PhotoGalleryFragment.this.items = items;
            setupAdapter();
        }
    }

    // Got help from:
    //  https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Void, Bitmap> {

        String url;
        String name;

        DownloadFile(String url, String name) {
            this.url = url;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            Bitmap bitmap = null;
            try {
                // Download image using URL
                InputStream input = new java.net.URL(url).openStream();

                bitmap = BitmapFactory.decodeStream(input);
                if (bitmap == null) {
                    return null;
                }

                ContextWrapper cw = new ContextWrapper(getContext());
                File directory = cw.getDir(FILE_CUSTOM_DRAWABLE, Context.MODE_PRIVATE);
                File myPath = new File(directory, name);
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(myPath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    Log.e(TAG_DOWNLOAD_IMAGE, e.getMessage(), e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i(TAG_DOWNLOAD_IMAGE, "SUCCESSFULLY SAVED");
        }
    }

}