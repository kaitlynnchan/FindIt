package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;

import cmpt276.project.model.CardDeck;

/**
 * Game Screen
 * Displays:
 *      score,
 *      high score,
 *      discard pile,
 *      draw pile
 */
public class GameActivity extends AppCompatActivity {

    private Chronometer timer;

    private CardDeck cardDeck;

    private Button[] drawPileImages;        // Contains the three images of a card from the draw pile
    private Button[] discardPileImages;     // Contains the three images of a card from the discard pile
    private Button startGameButton;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardDeck = CardDeck.getInstance();

        drawPileImages = new Button[3];
        discardPileImages = new Button[3];
        startGameButton = findViewById(R.id.startGameButton);

        setupDiscardCard();
        setupDrawCard();
        startGame();
    }

    // Begin the game
    private void startGame() {
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                updateCardImages();
                startGameButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Sets up the images of the cards in the draw pile
    // The three buttons correspond to the three images on each card
    // Help taken from Brian: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupDrawCard() {
        TableLayout tableDraw = findViewById(R.id.tableLayoutDraw);

        for(int i = 0; i < 3; i++){
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
            drawPileImages[i] = button;
            drawPileImages[i].setVisibility(View.INVISIBLE);
        }
    }

    // Sets up the images for the top card in the discard pile
    // The buttons are purposely unresponsive
    // Help taken from Brian: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void setupDiscardCard() {
        TableLayout tableDiscard = findViewById(R.id.tableLayoutDiscard);

        for(int i = 0; i < 3; i++){
            Button button = new Button(this);
            button.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            button.setPadding(0, 0, 0, 0);

            tableDiscard.addView(button);
            discardPileImages[i] = button;
            discardPileImages[i].setVisibility(View.INVISIBLE);
        }
    }

    // Checks if the selected image matches an image on on the discard pile card
    private void imageClicked(int index) {
        if (cardDeck.searchDiscardPile(index) == 0) {
            if (cardDeck.returnCardIndex() == 6) {
                stopTimer();
                FragmentManager manager = getSupportFragmentManager();
                WinFragment dialog = new WinFragment();
                dialog.show(manager, "");
            }

            else {
                cardDeck.incrementCardIndex();
                updateCardImages();
            }
        }
    }

    // Locks the button sizes
    // Taken from Brians youtube videos: https://www.youtube.com/watch?v=4MFzuP1F-xQ
    private void lockButtonSizes() {
        for (int i = 0; i < 3; i++) {

            Button drawButton = drawPileImages[i];
            Button discardButton = discardPileImages[i];

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
    private void updateCardImages() {
        for (int i = 0; i < 3; i++) {

            int drawImageID = cardDeck.returnCardImage(cardDeck.returnCardIndex(), i);
            int discardImageID = cardDeck.returnCardImage(cardDeck.returnCardIndex() - 1, i);

            Button drawButton = drawPileImages[i];
            Button discardButton = discardPileImages[i];

            lockButtonSizes();

            int drawWidth = drawButton.getWidth();
            int drawHeight = drawButton.getHeight();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), setImage(drawImageID));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, drawWidth, drawHeight, true);
            Resources resource = getResources();
            drawButton.setBackground(new BitmapDrawable(resource, scaledBitmap));

            drawButton.setVisibility(View.VISIBLE);

            int discardWidth = discardButton.getWidth();
            int discardHeight = discardButton.getHeight();

            originalBitmap = BitmapFactory.decodeResource(getResources(), setImage(discardImageID));
            scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, discardWidth, discardHeight, true);
            resource = getResources();
            discardButton.setBackground(new BitmapDrawable(resource, scaledBitmap));

            discardButton.setVisibility(View.VISIBLE);
        }
    }

    // Sets buttons to the appropriate images based on their index
    public int setImage(int index) {
        if (index == 0) return R.drawable.apple;
        else if (index == 1) return R.drawable.green_apple;
        else if (index == 2) return R.drawable.lemon;
        else if (index == 3) return R.drawable.mango;
        else if (index == 4) return R.drawable.orange;
        else if (index == 5) return R.drawable.pumpkin;
        else return R.drawable.watermelon;
    }

    private void startTimer() {
        timer = findViewById(R.id.chronometer);
        timer.start();
    }

    private void stopTimer() {
        timer.stop();

        String time = timer.getText().toString();
        int seconds = Integer.parseInt(time.substring(time.length() - 2));
        int minutes = Integer.parseInt(time.substring(time.length() - 5, time.length() - 3));
    }
}