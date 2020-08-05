package cmpt276.project.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cmpt276.project.R;
import cmpt276.project.flickr.PhotoGalleryActivity;
import cmpt276.project.flickr.PhotoGalleryFragment;

public class EditOwnImagesActivity extends AppCompatActivity {

    private Button launchGalleryButton;

    private int numImages;
    private int counter;

    private int onActivityResultNumImages;
    private int onActivityResultCounter;

    private LinearLayout linearLayout;

    private RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_own_images);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        linearLayout = findViewById(R.id.horizontalScrollViewLinearLayout);
        linearLayout.setVisibility(View.VISIBLE);

        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.INVISIBLE);

        launchGalleryButton = findViewById(R.id.launchGallery);
        launchGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyImage();
            }
        });

        numImages = 0;
        counter = 0;
        onActivityResultCounter = 0;
        onActivityResultNumImages = 0;

        setupMyImageTable();
    }

    private File[] getNumImagesAndDirectory() {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("myDrawable", Context.MODE_PRIVATE);
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
    private void setupMyImageTable() {

        final File[] directoryListing = getNumImagesAndDirectory();

        for (int i = 0; i < numImages; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            for (int j = 0; j < 2; j++) {

                if (counter == numImages) break;

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));

                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(directoryListing[counter]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(b);
                final int index = counter;
                // Avoid clipping text on smaller buttons
                //imageView.setPadding(0, 0, 0, 0);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImageFile(directoryListing, index);
                    }
                });

                tableRow.addView(imageView);
                counter++;
            }

            linearLayout.addView(tableRow);
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

    // https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery
    // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
    public void getMyImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
    // https://stackoverflow.com/questions/29803924/android-how-to-set-the-photo-selected-from-gallery-to-a-bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        onActivityResultNumImages = 0;
        onActivityResultCounter = 0;

        if(resultCode != Activity.RESULT_OK)
            Toast.makeText(this, "EditOwnImages failed to return from gallery", Toast.LENGTH_SHORT).show();

        else if (data == null)
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();

        // If the user selects multiple images by tapping and holding, or one image by tapping and holding
        else if(data.getClipData() != null) {
            linearLayout.setVisibility(View.INVISIBLE);
            loadingPanel.setVisibility(View.VISIBLE);
            int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
            onActivityResultNumImages = count;
            for(int i = 0; i < count; i++) {
                final Uri uri = data.getClipData().getItemAt(i).getUri();
                final String address = uri.toString();
                System.out.println("IMAGE URI: " + uri);
                for (int j = address.length() - 1; j > 0; j--) {
                    if (address.substring(j, j + 1).equals("/")) {
                        String name = address.substring(j + 1, address.length());
                        System.out.println("IMAGE NAME: " + name);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
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
            linearLayout.setVisibility(View.INVISIBLE);
            loadingPanel.setVisibility(View.VISIBLE);
            onActivityResultNumImages = 1;
            final Uri uri = data.getData();
            final String address = uri.toString();
            System.out.println("IMAGE URI: " + uri);
            for (int j = address.length() - 1; j > 0; j--) {
                if (address.substring(j, j + 1).equals("/")) {
                    String name = address.substring(j + 1, address.length());
                    System.out.println("IMAGE NAME: " + name);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
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

        else {
            System.out.println("clipData is null");
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

                ContextWrapper cw = new ContextWrapper(EditOwnImagesActivity.this);
                File directory = cw.getDir("myDrawable", Context.MODE_PRIVATE);

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
        // Restart activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}