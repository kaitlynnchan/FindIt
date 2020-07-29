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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cmpt276.project.R;
import cmpt276.project.flickr.PhotoGalleryActivity;

public class FlickrEditActivity extends AppCompatActivity {

    private static int numImages;
    private static int numRows;
    private static int numCols;
    private static int counter;

    private Button launchFlickrButton;

    private ImageView[] imageList;
    private static Object[] objects;

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
    }

    private static File[] getNumImagesAndDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("flickrDrawable", Context.MODE_PRIVATE);
        File dir = new File(directory.toString());
        File[] directoryListing = dir.listFiles();
        numImages = directoryListing.length;
        objects = new Object[numImages];

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

    // https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    private void setupFlickrImageTable() {

        final File[] directoryListing = getNumImagesAndDirectory(this);

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

                objects[counter] = b;
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

    public static Object[] getObjects(Context context) {
        final File[] directoryListing = getNumImagesAndDirectory(context);

        Object[] objects = new Object[numImages];
        for(int i = 0; i < numImages; i++){
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(directoryListing[i]));
                System.out.println("" + directoryListing[counter].getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            objects[i] = b;
        }
        return objects;
    }

    private void deleteImageFile(File[] directoryListing, int index) {
        if (directoryListing[index].delete()) System.out.println("Deleted successfully");
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}