package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;

/**
 * GAME SCREEN
 * Displays score/timer, discard pile, and draw pile
 */
public class GameActivity extends AppCompatActivity {

    private Chronometer timer;
    private CardDeck cardDeck;
    private int numImages;

    private Button[] drawPile;        // Contains the images of a card from the draw pile
    private Button[] discardPile;     // Contains the images of a card from the discard pile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();
        numImages = cardDeck.getNumImages();

        drawPile = new Button[numImages];
        discardPile = new Button[numImages];

        setupDrawCard();
        setupDiscardCard();
        startGame();
        setupBackButton();
    }

    // Begin the game
    private void startGame() {
        final Button startGameButton = findViewById(R.id.buttonStartGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                updateCard();

                ImageView imgDiscard = findViewById(R.id.imageDiscardCard);
                imgDiscard.setVisibility(View.VISIBLE);
                ImageView imgCard = findViewById(R.id.imageCardBack);
                imgCard.setVisibility(View.GONE);
                
                startGameButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Sets up the images of the cards in the draw pile
    // The three buttons correspond to the three images on each card
    // Help taken from Brian: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupDrawCard() {
        TableLayout tableDraw = findViewById(R.id.tableLayoutDraw);
        for(int i = 0; i < numImages; i++){
            final int cardIndex = i;
            Button button = new Button(this);
            button.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            // Avoid clipping text on smaller buttons
            button.setPadding(0, 0, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageClicked(cardIndex);
                }
            });

            tableDraw.addView(button);
            drawPile[i] = button;
            drawPile[i].setVisibility(View.INVISIBLE);
        }
    }

    // Sets up the images for the top card in the discard pile
    // The buttons are purposely unresponsive
    // Help taken from Brian: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupDiscardCard() {
        TableLayout tableDiscard = findViewById(R.id.tableLayoutDiscard);

        for(int i = 0; i < numImages; i++){
            Button button = new Button(this);
            button.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            button.setPadding(0, 0, 0, 0);

            tableDiscard.addView(button);
            discardPile[i] = button;
            discardPile[i].setVisibility(View.INVISIBLE);
        }
    }

    // Checks if the selected image matches an image on on the discard pile card
    private void imageClicked(int index) {
        if (cardDeck.searchDiscardPile(index)) {
            if (cardDeck.getCardIndex() == cardDeck.getCardDeckSize() - 1) {
                stopTimer();
            } else {
                cardDeck.incrementCardIndex();
                updateCard();
            }
        }
    }

    // Locks the button sizes
    // Taken from Brians youtube videos: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void lockButtonSizes() {
        for (int i = 0; i < 3; i++) {
            Button drawButton = drawPile[i];
            Button discardButton = discardPile[i];

            int drawHeight = drawButton.getHeight();
            drawButton.setMinHeight(drawHeight);
            drawButton.setMaxHeight(drawHeight);

            int drawWidth = drawButton.getWidth();
            drawButton.setMinWidth(drawWidth);
            drawButton.setMaxWidth(drawWidth);

            int discardHeight = discardButton.getHeight();
            drawButton.setMinHeight(discardHeight);
            drawButton.setMaxHeight(discardHeight);

            int discardWidth = discardButton.getWidth();
            drawButton.setMinWidth(discardWidth);
            drawButton.setMaxWidth(discardWidth);
        }
    }

    // Change the button icons to the appropriate pictures
    // Used code from Brians youtube video: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void updateCard() {
        for (int i = 0; i < numImages; i++) {
            Object drawObject = cardDeck.getCardObject(cardDeck.getCardIndex(), i);
            Object discardObject = cardDeck.getCardObject(cardDeck.getCardIndex() - 1, i);

            lockButtonSizes();

            setButton(drawObject, drawPile[i]);
            setButton(discardObject, discardPile[i]);
        }
    }

    private void setButton(Object object, Button button) {
        if (object.getClass() == Integer.class) {
            int width = button.getWidth();
            int height = button.getHeight();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), (int) object);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaledBitmap));

            button.setText(null);
        } else if (object.getClass() == String.class) {
            button.setText("" + object);
            button.setBackground(null);
        }
        button.setVisibility(View.VISIBLE);
    }

    private void startTimer() {
        timer = findViewById(R.id.chronometer);
        timer.start();
        timer.setBase(SystemClock.elapsedRealtime());
    }

    private void stopTimer() {
        timer.stop();

        // divide by 1000 to convert values from milliseconds to seconds
        int time = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000.0);
        FragmentManager manager = getSupportFragmentManager();
        WinFragment dialog = new WinFragment(time);
        dialog.show(manager, "");
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

    public static Intent makeIntent(Context context){
        return new Intent(context, GameActivity.class);
    }
}