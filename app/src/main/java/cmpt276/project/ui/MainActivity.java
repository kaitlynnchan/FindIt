package cmpt276.project.ui;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        scoresManager = ScoresManager.getInstance();

        setupButtons();
        setupSavedScores();
    }

    private void setupButtons() {
        Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement games activity
                createCardDeck();
                Intent intent = GameActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        ImageButton btnHighScores = findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HighScoreActivity.makeIntent(MainActivity.this);
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
        String[] wordPack = OptionActivity.getWordPackArray(MainActivity.this);
        cardDeck.setNumCards(7);
        cardDeck.setNumImages(3);
        cardDeck.setMode(OptionActivity.getMode(MainActivity.this));
        cardDeck.setCardIndex();
        cardDeck.setImageArr(imagePack);
        cardDeck.setWordArr(wordPack);
        cardDeck.populateCards();
    }

    private void setupSavedScores() {
        scoresManager.setNumMaxScores(5);
        ArrayList<Score> temp = HighScoreActivity.getSavedScores(this);
        if(temp == null){
            HighScoreActivity.setDefaultScores(scoresManager);
        } else{
            scoresManager.setScoreArray(temp);
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}