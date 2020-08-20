package cmpt276.project.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cmpt276.project.R;
import cmpt276.project.ui.flickr.PhotoGalleryActivity;
import cmpt276.project.ui.flickr.PhotoGalleryFragment;

/**
 * CUSTOM IMAGES SCREEN
 * Allows users to remove and add images from flickr or gallery
 */
public class CustomImagesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FLICKR = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    private LinearLayout linearLayoutImages;
    private RelativeLayout layoutLoadingPanel;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, CustomImagesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_images);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        linearLayoutImages = findViewById(R.id.linear_images);
        layoutLoadingPanel = findViewById(R.id.relative_loading_panel);

        setupImageTable();
        setupLaunchButtons();
    }

    // Got help from:
    //  https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    //  https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupImageTable() {
        final File[] directoryListing = getDirectory(this);
        int numCustomImages = directoryListing.length;
        int counter = 0;
        int numCols = 4;
        int numRows = (int) Math.ceil(numCustomImages / (double) numCols);

        for (int row = 0; row < numRows; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            linearLayoutImages.addView(tableRow);

            for (int col = 0; col < numCols; col++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        300,
                        1.0f
                ));
                imageView.setImageResource(R.drawable.empty_image);

                if(counter < numCustomImages){
                    Bitmap bitmap = getBitmap(directoryListing[counter], this);
                    imageView.setImageBitmap(bitmap);

                    final int index = counter;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteImageFile(directoryListing, index);
                        }
                    });
                    counter++;
                }
                imageView.setScaleType(ScaleType.FIT_XY);
                tableRow.addView(imageView);
            }
        }
    }

    private static Bitmap getBitmap(File file, Context context) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            System.out.println(file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.magnifying_glass);
        }
        return bitmap;
    }

    // Got help from:
    //  https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    private static File[] getDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(PhotoGalleryFragment.FILE_CUSTOM_DRAWABLE, Context.MODE_PRIVATE);
        File dir = new File(directory.toString());
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        return directoryListing;
    }

    public static int getNumCustomImages(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(PhotoGalleryFragment.FILE_CUSTOM_DRAWABLE, Context.MODE_PRIVATE);
        File dir = new File(directory.toString());
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null){
            return directoryListing.length;
        }
        return 0;
    }

    private void deleteImageFile(File[] directoryListing, int index) {
        if (directoryListing[index].delete()) {
            System.out.println("Deleted successfully");
            System.gc();
        }
        updateImageTable();
    }

    private void updateImageTable() {
        linearLayoutImages.removeAllViewsInLayout();
        setupImageTable();
        layoutLoadingPanel.setVisibility(View.GONE);
        linearLayoutImages.setVisibility(View.VISIBLE);
    }

    // Got help and code from:
    //  https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
    //  https://stackoverflow.com/questions/29803924/android-how-to-set-the-photo-selected-from-gallery-to-a-bitmap
    //  https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_FLICKR:
                updateImageTable();
                break;
            case REQUEST_CODE_GALLERY:
                if(resultCode != Activity.RESULT_OK){
                    Toast.makeText(this, R.string.toast_fail, Toast.LENGTH_SHORT).show();
                } else if (data == null){
                    Toast.makeText(this, R.string.toast_null_data, Toast.LENGTH_SHORT).show();
                } else if(data.getClipData() != null) {
                    // User selects multiple images by tapping and holding image
                    linearLayoutImages.setVisibility(View.INVISIBLE);
                    layoutLoadingPanel.setVisibility(View.VISIBLE);

                    // Evaluate the count before the for loop
                    // otherwise, the count is evaluated every loop
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        saveImageBitmap(uri);
                    }
                } else if (data != null) {
                    // User selects one image
                    linearLayoutImages.setVisibility(View.INVISIBLE);
                    layoutLoadingPanel.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    if(uri != null){
                        saveImageBitmap(uri);
                    }
                }
                break;
            default:
                assert false;
        }
    }

    private void saveImageBitmap(Uri uri) {
        final String address = uri.toString();
        System.out.println("IMAGE URI: " + uri);
        for (int i = address.length() - 1; i > 0; i--) {
            if (address.substring(i, i + 1).equals("/")) {
                String name = address.substring(i + 1);
                System.out.println("IMAGE NAME: " + name);

                try {
                    // Got help from:
                    //  https://stackoverflow.com/questions/39897338/how-to-get-current-time-stamp-in-android/39897615
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("PDT"));
                    String temp = sdf.format(new Date());
                    name += temp;
                    name += ".png";

                    new DownloadFile(bitmap, name).execute();
                } catch (IOException e) {
                    System.out.println("useBitmap function failed");
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    // Code for bitmap string conversion: https://stackoverflow.com/questions/22532136/android-how-to-create-a-bitmap-from-a-string
    public static Object[] getCustomArr(Context context) {
        final File[] directoryListing = getDirectory(context);

        Object[] objects = new Object[getNumCustomImages(context)];
        for(int i = 0; i < objects.length; i++){
            Bitmap b = getBitmap(directoryListing[i], context);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
            byte[] byteArr = byteArrayOutputStream.toByteArray();
            String imageStr = Base64.encodeToString(byteArr, Base64.DEFAULT);

            objects[i] = imageStr;
        }
        return objects;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Void, Bitmap> {

        Bitmap bitmap;
        String name;

        DownloadFile(Bitmap bitmap, String name) {
            this.bitmap = bitmap;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            System.out.println("IMAGE CAPTION: " + name);

            try {
                if (bitmap == null){
                    return null;
                }

                ContextWrapper cw = new ContextWrapper(getBaseContext());
                File directory = cw.getDir(PhotoGalleryFragment.FILE_CUSTOM_DRAWABLE, Context.MODE_PRIVATE);
                File myPath = new File(directory, name);
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(myPath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    Log.e(PhotoGalleryFragment.TAG_SAVE_IMAGE, e.getMessage(), e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println("SUCCESSFULLY SAVED");
            updateImageTable();
        }
    }

    private void setupLaunchButtons() {
        Button buttonLaunchFlickr = findViewById(R.id.button_launch_flickr);
        buttonLaunchFlickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PhotoGalleryActivity.makeLaunchIntent(CustomImagesActivity.this);
                startActivityForResult(intent, REQUEST_CODE_FLICKR);
            }
        });

        // Got help from:
        //  https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery
        Button buttonLaunchGallery = findViewById(R.id.button_launch_gallery);
        buttonLaunchGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_CODE_GALLERY);
            }
        });

        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
    }
}