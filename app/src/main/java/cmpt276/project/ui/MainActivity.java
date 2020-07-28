package cmpt276.project.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cmpt276.project.R;
import cmpt276.project.flickr.PhotoGalleryActivity;
import cmpt276.project.flickr.PhotoGalleryFragment;
import cmpt276.project.model.CardDeck;
import cmpt276.project.model.Mode;
import cmpt276.project.model.GameConfigs;
import cmpt276.project.model.ScoresManager;

/**
 * MAIN MENU
 * Includes play, options, help, and high score
 *  buttons to navigate through the app
 */
public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "shared prefs";
    public static final String EDITOR_CARD_DECKS = "card decks";
    public static final String EDITOR_SCORES_MANAGERS = "scores managers";
    private CardDeck cardDeck;
    private GameConfigs gameConfigs;
    String URL ;
    ImageView image;
    Button button;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        gameConfigs = GameConfigs.getInstance();
        
        setupButtons();




        // Locate the ImageView in activity_main.xml
        image = (ImageView) findViewById(R.id.image);

        // Locate the Button in activity_main.xml
        button = (Button) findViewById(R.id.button);

        // Capture button click
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(URL);
            }
        });

    }

    private void setupButtons() {
        Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        ImageButton btnHighScores = findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(
                        MainActivity.this, gameConfigs.getCardDeckIndex(cardDeck));
                startActivity(intent);
            }
        });

        Button btnOptions = findViewById(R.id.buttonOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button btnHelp = findViewById(R.id.buttonHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button flickrbutton = findViewById(R.id.flickrButton);
        flickrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoGalleryActivity.class);
                startActivity(intent);
                for(int i =0 ;i<7 ;i++){
                URL = PhotoGalleryFragment.getImgArr(getBaseContext()).get(i);}
            }
        });
    }

    private void createCardDeck() {
        int[] imagePack = OptionActivity.getImagePackArray(MainActivity.this);
        String[] wordPack = OptionActivity.getWordArray(MainActivity.this);
        Mode mode = OptionActivity.getMode(MainActivity.this);

        cardDeck.setMode(mode);
        int numImages = OptionActivity.getNumImages(MainActivity.this);
        int cardDeckSize = OptionActivity.getCardDeckSize(MainActivity.this);
        int numCardsTotal = OptionActivity.getNumCardsTotal(MainActivity.this);

        cardDeck.setNumCardsTotal(numCardsTotal);
        cardDeck.setNumImages(numImages);
        cardDeck.setCardIndex();
        cardDeck.setCardDeckSize(cardDeckSize);
        cardDeck.setImageArr(imagePack);
        cardDeck.setWordArr(wordPack);
        cardDeck.populateCards();
    }

    private void setupSavedScores() {
        int index = gameConfigs.getCardDeckIndex(cardDeck);
        ScoresManager scoresManager = new ScoresManager();
        if(index == -1){
            scoresManager.setNumMaxScores(5);
            HighScoreActivity.setDefaultScores(scoresManager);
            gameConfigs.add(cardDeck, scoresManager);
        } else{
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());
        }
    }

    @Override
    protected void onResume() {
        loadGameConfigs();
        createCardDeck();
        setupSavedScores();
        saveGameConfigs(this, gameConfigs);
        super.onResume();
    }

    private void loadGameConfigs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_CARD_DECKS, null);
        Type type = new TypeToken<ArrayList<CardDeck>>() {}.getType();
        ArrayList<CardDeck> arrCardDeckTemp = gson.fromJson(json, type);
        if(arrCardDeckTemp != null) {
            gameConfigs.setCardDecks(arrCardDeckTemp);
        }

        json = sharedPreferences.getString(EDITOR_SCORES_MANAGERS, null);
        type = new TypeToken<ArrayList<ScoresManager>>() {}.getType();
        ArrayList<ScoresManager> arrScoreManagerTemp = gson.fromJson(json, type);
        if(arrScoreManagerTemp != null) {
            gameConfigs.setScoresManagers(arrScoreManagerTemp);
        }
    }

    public static void saveGameConfigs(Context context, GameConfigs gameConfigs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameConfigs.getCardDecks());
        editor.putString(EDITOR_CARD_DECKS, json);

        json = gson.toJson(gameConfigs.getScoresManagers());
        editor.putString(EDITOR_SCORES_MANAGERS, json);

        editor.apply();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }


    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            image.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }
}









