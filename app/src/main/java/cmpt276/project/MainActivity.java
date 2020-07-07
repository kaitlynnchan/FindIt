package cmpt276.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import cmpt276.project.model.CardDeck;

/**
 * Main menu
 * Includes play, options, help, and high score
 *  buttons to navigate through the game
 */
public class MainActivity extends AppCompatActivity {

    private CardDeck cardDeck;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

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
                /*int[] fruitIDs = {R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                        R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                        R.drawable.watermelon};*/

                cardDeck.setNumCards(7);
                cardDeck.setNumImages(3);
                cardDeck.setCardIndex();
                cardDeck.setImageArr(new int[] {0, 1, 2, 3, 4, 5, 6});
                cardDeck.populateCards();
                cardDeck.print();
                cardDeck.shuffleCards();
                cardDeck.shuffleImages();
                cardDeck.print();

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
                // Implement options activity
            }
        });

        Button btnHelp = findViewById(R.id.buttonHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement help activity
            }
        });
    }

}