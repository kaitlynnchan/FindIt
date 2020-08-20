package project.findit.ui;

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

import project.findit.model.CardDeck;
import project.findit.model.GameConfigs;
import project.findit.model.Mode;
import project.findit.model.ScoresManager;
import project.findit.R;

/**
 * MAIN MENU
 * Includes buttons to navigate through the app
 */
public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_GAMES = "shared_prefs_games";
    private static final String EDITOR_CARD_DECKS = "editor_card_decks";
    private static final String EDITOR_SCORES_MANAGERS = "editor_scores_managers";

    private CardDeck cardDeck;
    private GameConfigs gameConfigs;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        gameConfigs = GameConfigs.getInstance();

        setupButtons();
    }

    private void setupButtons() {
        Button buttonPlay = findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        ImageButton buttonLeaderBoard = findViewById(R.id.image_button_leader_board);
        buttonLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LeaderBoardActivity.makeLaunchIntent(
                        MainActivity.this, gameConfigs.getCardDeckIndex(cardDeck));
                startActivity(intent);
            }
        });

        Button buttonOptions = findViewById(R.id.button_options);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button buttonHelp = findViewById(R.id.button_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGameConfigs();
        createCardDeck();
        setupSavedScores();
        saveGameConfigs(this, gameConfigs);
    }

    private void loadGameConfigs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_GAMES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_CARD_DECKS, null);
        Type type = new TypeToken<ArrayList<CardDeck>>() {}.getType();
        ArrayList<CardDeck> arrayCardDeckTemp = gson.fromJson(json, type);
        if(arrayCardDeckTemp != null) {
            gameConfigs.setCardDecks(arrayCardDeckTemp);
        }

        json = sharedPreferences.getString(EDITOR_SCORES_MANAGERS, null);
        type = new TypeToken<ArrayList<ScoresManager>>() {}.getType();
        ArrayList<ScoresManager> arrScoreManagerTemp = gson.fromJson(json, type);
        if(arrScoreManagerTemp != null) {
            gameConfigs.setScoresManagers(arrScoreManagerTemp);
        }
    }

    private void createCardDeck() {
        int numCards = OptionsActivity.getNumCards(this);
        int numImagesPerCard = OptionsActivity.getNumImagesPerCard(this);
        Mode difficultyMode = OptionsActivity.getDifficultyMode(this);
        Object[] packArr = OptionsActivity.getPackArray(this);

        cardDeck.setNumCards(numCards);
        cardDeck.setNumImagesPerCard(numImagesPerCard);
        cardDeck.setDifficultyMode(difficultyMode);
        cardDeck.populateCards(packArr);
        cardDeck.resetCurrentIndex();
    }

    private void setupSavedScores() {
        int index = gameConfigs.getCardDeckIndex(cardDeck);
        ScoresManager scoresManager = new ScoresManager();
        if(index == -1){
            scoresManager.setNumMaxScores(5);
            LeaderBoardActivity.setDefaultScores(this, scoresManager);
            gameConfigs.add(cardDeck, scoresManager);
        } else{
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());
        }
    }

    public static void saveGameConfigs(Context context, GameConfigs gameConfigs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SHARED_PREFS_GAMES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameConfigs.getCardDecks());
        editor.putString(EDITOR_CARD_DECKS, json);

        json = gson.toJson(gameConfigs.getScoresManagers());
        editor.putString(EDITOR_SCORES_MANAGERS, json);

        editor.apply();
    }
}