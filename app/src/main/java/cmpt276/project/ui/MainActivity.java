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
import cmpt276.project.model.Mode;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        gameConfigs = GameConfigs.getInstance();

        setupButtons();
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

    private void createCardDeck() {
        int cardDeckSize = OptionActivity.getCardDeckSize(MainActivity.this);
        int numImages = OptionActivity.getNumImages(MainActivity.this);
        int numCardsTotal = OptionActivity.getNumCardsTotal(numImages);
        Object[] packArr = OptionActivity.getPackArray(MainActivity.this);
        Mode gameMode = OptionActivity.getGameMode(MainActivity.this);

        cardDeck.setCardDeckSize(cardDeckSize);
        cardDeck.setNumImages(numImages);
        cardDeck.setNumCardsTotal(numCardsTotal);
        cardDeck.setMode(gameMode);
        cardDeck.setCardIndex();
        cardDeck.populateCards(packArr);
    }

    private void setupSavedScores() {
        int index = gameConfigs.getCardDeckIndex(cardDeck);
        ScoresManager scoresManager = new ScoresManager();
        if(index == -1){
            scoresManager.setNumMaxScores(5);
            HighScoreActivity.setDefaultScores(this, scoresManager);
            gameConfigs.add(cardDeck, scoresManager);
        } else{
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}