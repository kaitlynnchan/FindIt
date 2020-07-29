package cmpt276.project.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cmpt276.project.R;
import cmpt276.project.flickr.PhotoGalleryActivity;
import cmpt276.project.flickr.PhotoGalleryFragment;

public class FlickrEditActivity extends AppCompatActivity {

    private int numImages;
    private int numRows;
    private int numCols;
    private int counter;

    private Button launchFlickrButton;

    private ImageView[] imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_edit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        numImages = 0;
        numRows = 0;
        numCols = 0;
        counter = 0;

        setupFlickrImageTable();

        launchFlickrButton = findViewById(R.id.launchFlickrButton);
        launchFlickrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlickrEditActivity.this, PhotoGalleryActivity.class);
                startActivityForResult(intent, 1);
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
        numImages = directoryListing.length;

        if (numImages < 4) {
            numRows = 2;
            numCols = 2;
        }

        else if (numImages % 2 == 0) {
            numRows = numImages / 2;
            numCols = numImages / 2;
        } else {
            numRows = (numImages / 2) + 1;
            numCols = (numImages / 2) + 1;
        }

        System.out.println("ROWS: " + numRows);
        System.out.println("COLS: " + numCols);

        return directoryListing;
    }

    // Displays the flickr image set
    // Got help and code from: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    // and https://stackoverflow.com/questions/54996665/how-to-save-downloaded-file-in-internal-storage-in-android-studio
    // and Brian's youtube video: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupFlickrImageTable() {

        final File[] directoryListing = getNumImagesAndDirectory();

        TableLayout tableDraw = findViewById(R.id.flickrImageTable);
        for (int i = 0; i < numRows; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            tableDraw.addView(tableRow);

            for (int j = 0; j < numCols; j++) {
                final int index = counter;
                if (counter >= numImages) return;

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

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
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // Got help and code from: https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
    // Reloads the activity when the user is done using flickr
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

}