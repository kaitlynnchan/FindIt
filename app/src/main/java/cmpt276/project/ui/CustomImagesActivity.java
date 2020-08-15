package cmpt276.project.ui;

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
import cmpt276.project.ui.flickr.PhotoGalleryActivity;
import cmpt276.project.ui.flickr.PhotoGalleryFragment;

/**
 * CUSTOM IMAGES SCREEN
 * Allows users to remove and add images, include launch flickr and gallery buttons
 */
public class CustomImagesActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FLICKR = 1;
    public static final int REQUEST_CODE_GALLERY = 2;
    private int numImages;
    private int counter;

    private ImageView[] imageList;

    private LinearLayout linearImages;

    private RelativeLayout loadingPanel;

    private int onActivityResultNumImages;
    private int onActivityResultCounter;

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

        numImages = 0;
        counter = 0;

        onActivityResultNumImages = 0;
        onActivityResultCounter = 0;

        linearImages = findViewById(R.id.linearLayoutImages);

        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.INVISIBLE);

        setupImageTable();
        setupButtons();
    }

    // Displays the image set
    // Got help and code from: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    // and https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    // and Brian's youtube video: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupImageTable() {

        final File[] directoryListing = getNumImagesAndDirectory();

        int numRows = (numImages % 4 == 0) ?  numImages / 4 : (numImages / 4) + 1;

        for (int i = 0; i < numRows; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            linearImages.addView(tableRow);

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

        switch (requestCode){
            case REQUEST_CODE_FLICKR:
                restartActivity();
                break;
            case REQUEST_CODE_GALLERY:
                if(resultCode != Activity.RESULT_OK){
                    Toast.makeText(this, R.string.toast_fail, Toast.LENGTH_SHORT).show();
                } else if (data == null){
                    Toast.makeText(this, R.string.toast_null_data, Toast.LENGTH_SHORT).show();
                } else if(data.getClipData() != null) {
                    // If the user selects multiple images by tapping and holding, or one image by
                    // tapping and holding, loading bar will show since of delay
                    linearImages.setVisibility(View.INVISIBLE);
                    loadingPanel.setVisibility(View.VISIBLE);

                    // evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    int count = data.getClipData().getItemCount();
                    onActivityResultNumImages = count;
                    for(int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        saveImageBitmap(uri);
                    }
                } else if (data != null) {
                    // If the user selects one image by tapping
                    linearImages.setVisibility(View.INVISIBLE);
                    loadingPanel.setVisibility(View.VISIBLE);
                    onActivityResultNumImages = 1;
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

                ContextWrapper cw = new ContextWrapper(CustomImagesActivity.this);
                File directory = cw.getDir(PhotoGalleryFragment.FILE_FLICKR_DRAWABLE, Context.MODE_PRIVATE);

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
            if (onActivityResultCounter >= onActivityResultNumImages){
                restartActivity();
            }
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setupButtons() {
        Button buttonLaunchFlickr = findViewById(R.id.buttonLaunchFlickr);
        buttonLaunchFlickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PhotoGalleryActivity.makeIntent(CustomImagesActivity.this);
                startActivityForResult(intent, REQUEST_CODE_FLICKR);
            }
        });

        // https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery
        // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        Button buttonLaunchGallery = findViewById(R.id.buttonLaunchGallery);
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

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}