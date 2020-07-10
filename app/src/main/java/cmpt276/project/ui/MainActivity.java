package cmpt276.project.ui;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;

/**
 * Main menu
 * Includes play, options, help, and high score
 *  buttons to navigate through the game
 */
public class MainActivity extends AppCompatActivity {

    private CardDeck cardDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();

        setupButtons();
    }

    private void setupButtons() {
        Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement games activity
                createCardDeck();
                Intent intent = GameActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        ImageButton btnHighScores = findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement high scores activity
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
        cardDeck.setNumCards(7);
        cardDeck.setNumImages(3);
        cardDeck.setCardIndex();
        cardDeck.setImageArr(imagePack);
        cardDeck.populateCards();
        cardDeck.shuffleCards();
        cardDeck.shuffleImages();
    }

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, MainActivity.class);

    }
}