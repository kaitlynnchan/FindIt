package cmpt276.project.ui.flicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

/**
 * FLICKR EDIT SCREEN
 * Allows users to remove and add images, include launch flicker button
 */
public class FlickrEditActivity extends AppCompatActivity {

    private int numImages;
    private int counter;

    private Button launchFlickrButton;
    private Button launchGalleryButton;

    private ImageView[] imageList;

    private LinearLayout ownFlickrLinearLayout;

    private RelativeLayout loadingPanel;

    private int onActivityResultNumImages;
    private int onActivityResultCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_edit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        numImages = 0;
        counter = 0;

        onActivityResultNumImages = 0;
        onActivityResultCounter = 0;

        ownFlickrLinearLayout = findViewById(R.id.ownFlickrLinearLayout);

        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.INVISIBLE);

        setupFlickrImageTable();

        launchFlickrButton = findViewById(R.id.launchFlickrButton);
        launchFlickrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PhotoGalleryActivity.makeIntent(FlickrEditActivity.this);
                startActivityForResult(intent, 1);
            }
        });

        // https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery
        // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        launchGalleryButton = findViewById(R.id.launchGalleryButton);
        launchGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 2);
            }
        });

        setupBackButton();
    }

    // Calculates the number of rows and columns required for displaying the images, the number of images in the directory, and returns
    // an array that contains the files in the directory
    // Got help and code from: https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    // and: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    private File[] getNumImagesAndDirectory() {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir(PhotoGalleryFragment.FILE_FLICKR_DRAWABLE, Context.MODE_PRIVATE);
        File dir = new File(directory.toString());
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        numImages = directoryListing.length;

        return directoryListing;
    }

    // Displays the flickr image set
    // Got help and code from: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    // and https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    // and Brian's youtube video: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupFlickrImageTable() {

        final File[] directoryListing = getNumImagesAndDirectory();

        int numRows = (numImages % 4 == 0) ?  numImages / 4 : (numImages / 4) + 1;

        for (int i = 0; i < numRows; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            ownFlickrLinearLayout.addView(tableRow);

            for (int j = 0; j < 4; j++) {
                final int index = counter;

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        500,
                        300,
                        1.0f
                ));

                if (counter >= numImages) {
                    imageView.setImageResource(R.drawable.empty_image);
                    imageView.setScaleType(ScaleType.FIT_XY);
                    tableRow.addView(imageView);
                    continue;
                }

                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(directoryListing[counter]));
                    System.out.println("" + directoryListing[counter].getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(b);

                // Avoid clipping text on smaller buttons
                //imageView.setPadding(0, 0, 0, 0);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImageFile(directoryListing, index);
                    }
                });
                imageView.setScaleType(ScaleType.FIT_XY);
                tableRow.addView(imageView);
                counter++;
            }
        }
    }

    // Got help and code from: https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
    // Deletes an image file, and reloads the activity
    private void deleteImageFile(File[] directoryListing, int index) {
        if (directoryListing[index].delete()) {
            System.out.println("Deleted successfully");
            System.gc();
        }
        restartActivity();
    }

    // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
    // https://stackoverflow.com/questions/29803924/android-how-to-set-the-photo-selected-from-gallery-to-a-bitmap
    // Got help and code from: https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
    // Reloads the activity when the user is done using flickr
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) restartActivity();

        else if (requestCode == 2) {
            if(resultCode != Activity.RESULT_OK)
                Toast.makeText(this, "EditOwnImages failed to return from gallery", Toast.LENGTH_SHORT).show();

            else if (data == null)
                Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();

                // If the user selects multiple images by tapping and holding, or one image by tapping and holding
            else if(data.getClipData() != null) {
                ownFlickrLinearLayout.setVisibility(View.INVISIBLE);
                loadingPanel.setVisibility(View.VISIBLE);
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                onActivityResultNumImages = count;
                for(int i = 0; i < count; i++) {
                    final Uri uri = data.getClipData().getItemAt(i).getUri();
                    final String address = uri.toString();
                    System.out.println("IMAGE URI: " + uri);
                    for (int j = address.length() - 1; j > 0; j--) {
                        if (address.substring(j, j + 1).equals("/")) {
                            String name = address.substring(j + 1);
                            System.out.println("IMAGE NAME: " + name);

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                // https://stackoverflow.com/questions/39897338/how-to-get-current-time-stamp-in-android/39897615
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
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
            }

            // If the user selects one image by tapping
            else if (data != null) {
                ownFlickrLinearLayout.setVisibility(View.INVISIBLE);
                loadingPanel.setVisibility(View.VISIBLE);
                onActivityResultNumImages = 1;
                final Uri uri = data.getData();
                final String address = uri.toString();
                System.out.println("IMAGE URI: " + uri);
                for (int j = address.length() - 1; j > 0; j--) {
                    if (address.substring(j, j + 1).equals("/")) {
                        String name = address.substring(j + 1);
                        System.out.println("IMAGE NAME: " + name);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            // https://stackoverflow.com/questions/39897338/how-to-get-current-time-stamp-in-android/39897615
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
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
        }
    }

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
                if (bitmap == null) return null;

                ContextWrapper cw = new ContextWrapper(FlickrEditActivity.this);
                File directory = cw.getDir("flickrDrawable", Context.MODE_PRIVATE);

                File mypath = new File(directory, name);

                FileOutputStream fos ;
                try {
                    fos = new FileOutputStream(mypath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    Log.e("SAVE_IMAGE", e.getMessage(), e);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            onActivityResultCounter++;
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println("SUCCESSFULLY SAVED");
            if (onActivityResultCounter >= onActivityResultNumImages) restartActivity();
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, FlickrEditActivity.class);
    }
}