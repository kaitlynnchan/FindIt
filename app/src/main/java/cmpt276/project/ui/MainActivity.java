package cmpt276.project.ui;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;
import cmpt276.project.model.GameConfigs;
import cmpt276.project.model.ScoresManager;
import cmpt276.project.model.Score;

/**
 * MAIN MENU
 * Includes play, options, help, and high score
 *  buttons to navigate through the app
 */
public class MainActivity extends AppCompatActivity {

    private CardDeck cardDeck;
    private ScoresManager scoresManager;
    private GameConfigs gameConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        scoresManager = new ScoresManager();
        gameConfigs = GameConfigs.getInstance();

//        loadGameConfigs();

        setupButtons();
    }

    private void setupButtons() {
        Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement games activity
//                createCardDeck();
                Intent intent = GameActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        ImageButton btnHighScores = findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this, gameConfigs.getCardDeckIndex(cardDeck));
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
    }

    private void createCardDeck() {
        int[] imagePack = OptionActivity.getImagePackArray(MainActivity.this);
        int numImages = OptionActivity.getNumImages(MainActivity.this);
        int pileSize = OptionActivity.getPileSize(MainActivity.this);
        int numCardsTotal = OptionActivity.getNumCardsTotal(MainActivity.this);

        cardDeck.setNumCardsTotal(numCardsTotal);
        cardDeck.setNumImages(numImages);
        cardDeck.setCardIndex();
        cardDeck.setNumCards(pileSize);
        cardDeck.setImageArr(imagePack);
        cardDeck.populateCards();
    }

    private void setupSavedScores() {
        int index = gameConfigs.getCardDeckIndex(cardDeck);
        if(index == -1){
            System.out.println("does not exist");
            scoresManager.setNumMaxScores(5);
            scoresManager.resetScoreArray();
            HighScoreActivity.setDefaultScores(scoresManager);
            gameConfigs.add(cardDeck, scoresManager);
        } else{
            System.out.println("exists");
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());

//            ArrayList<Score> temp = HighScoreActivity.getSavedScores(this);
//            if(index != gameConfigs.getScoreManagerIndex(temp)){
//                gameConfigs.getScoreManager(index).setScoreArray(temp);
//            }
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());

        }
//        scoresManager.setNumMaxScores(5);
//        ArrayList<Score> temp = HighScoreActivity.getSavedScores(this);
//        if(temp == null){
//            HighScoreActivity.setDefaultScores(scoresManager);
//        } else{
//            scoresManager.setScoreArray(temp);
//        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onResume() {
        loadGameConfigs();
//        gameConfigs.print();
        createCardDeck();
        setupSavedScores();
        saveGameConfigs(this, gameConfigs);
//        loadGameConfigs();
        super.onResume();
    }

    private void loadGameConfigs() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared prefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("card decks ya", null);
        Type type = new TypeToken<ArrayList<CardDeck>>() {}.getType();
        ArrayList<CardDeck> arrCardDeckTemp = gson.fromJson(json, type);
        if(arrCardDeckTemp != null) {
            gameConfigs.setCardDecks(arrCardDeckTemp);
        }

        json = sharedPreferences.getString("scores managers ya", null);
        type = new TypeToken<ArrayList<ScoresManager>>() {}.getType();
        ArrayList<ScoresManager> arrScoreManagerTemp = gson.fromJson(json, type);
        if(arrScoreManagerTemp != null) {
            gameConfigs.setScoresManagers(arrScoreManagerTemp);
        }
    }

    public static void saveGameConfigs(Context context, GameConfigs gameConfigs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameConfigs.getCardDecks());
        editor.putString("card decks ya", json);

        json = gson.toJson(gameConfigs.getScoresManagers());
        editor.putString("scores managers ya", json);

        editor.apply();
    }
}