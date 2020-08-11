package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;

/**
 * GAME SCREEN
 * Displays score/timer, discard pile, and draw pile
 */
public class GameActivity extends AppCompatActivity {

    public static final String FILE_EXPORT_CARDS = "file_export_cards";

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
        startGameButton.setSoundEffectsEnabled(false);
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
            button.setSoundEffectsEnabled(false);

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

    // Checks if the selected image matches an image on on the discard pile card.
    // Takes Screenshot of the cards in a given game.
    private void imageClicked(int index) {
        MediaPlayer foundSound = MediaPlayer.create(this, R.raw.found);
        MediaPlayer incorrectSound = MediaPlayer.create(this, R.raw.incorrect_sound);
        if (cardDeck.searchDiscardPile(index)) {
            foundSound.start();
            if (cardDeck.getCardIndex() == cardDeck.getCardDeckSize() - 1) {
                takeScreenshot(cardDeck.getCardIndex());
                stopTimer();
            } else {
                takeScreenshot(cardDeck.getCardIndex());
                cardDeck.incrementCardIndex();
                updateCard();
            }
        } else{
            incorrectSound.start();
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
            Object[] drawObject = cardDeck.getCardObject(cardDeck.getCardIndex(), i);
            Object[] discardObject = cardDeck.getCardObject(cardDeck.getCardIndex() - 1, i);

            lockButtonSizes();

            setButton(drawObject, drawPile[i]);
            setButton(discardObject, discardPile[i]);
        }
    }

    private void setButton(Object[] object, Button button) {
        Object value = object[0];
        int rotate = Integer.parseInt((String) object[1]);
        int scale = Integer.parseInt((String) object[2]);
        try {
            int image = Integer.parseInt((String) value);
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), image);

            setButtonImage(button, rotate, scale, originalBitmap);
        } catch (NumberFormatException e) {
            System.out.println("not image");
            try {
                byte[] encodeByte = Base64.decode((String) value, Base64.DEFAULT);
                Bitmap bitmapCustom = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                setButtonImage(button, rotate, scale, bitmapCustom);
            } catch (Exception ex) {
                System.out.println("not bitmap image");
                String word = "" + value;
                SpannableString spannableStr = new SpannableString(word);
                spannableStr.setSpan(new RelativeSizeSpan(
                        2f / (float) scale), 0, word.length(), Spanned.SPAN_COMPOSING);

                button.setAllCaps(false);
                button.setText(spannableStr);
                button.setRotation(rotate);
                button.setBackground(null);
            }
        }
        button.setVisibility(View.VISIBLE);
    }

    private void setButtonImage(Button button, int rotate, int scale, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotate, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        Bitmap rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        int width = button.getWidth();
        int height = button.getHeight();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                rotatedBitmap, width / scale, height / scale, true);

        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));

        button.setRotation(0);
        button.setText(null);
    }

    private void startTimer() {
        timer = findViewById(R.id.chronometer);
        timer.start();
        timer.setBase(SystemClock.elapsedRealtime());

        MediaPlayer startSound = MediaPlayer.create(GameActivity.this, R.raw.start_sound);
        startSound.start();
    }

    private void stopTimer() {
        timer.stop();

        MediaPlayer winSound = MediaPlayer.create(this, R.raw.win_sound);
        winSound.start();

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

    // Take Screen Shot of the card.
    // Citation: https://stackoverflow.com/questions/2661536/how-to-programmatically-take-a-screenshot-on-android .
    private void takeScreenshot(int imgNum) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path, appending name you choose for file.
            ContextWrapper cw = new ContextWrapper(getBaseContext());
            File directory = cw.getDir(FILE_EXPORT_CARDS, Context.MODE_PRIVATE);

            File mypathDraw = new File(directory, "Draw"+imgNum+".jpeg");
            File mypathDiscard = new File(directory, "Discard"+imgNum+".jpg");

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            ImageView imgDiscard = findViewById(R.id.imageDiscardCard);
            ImageView imgDraw = findViewById(R.id.imageDrawCard);
            TextView txt = findViewById(R.id.textDiscard);
            int height = imgDiscard.getHeight() + txt.getHeight() - 30;
            int width = (imgDiscard.getHeight() * 3) / 4;
            int xDiscard = (int) imgDiscard.getX() + ((imgDiscard.getWidth() - width) / 2);
            int xDraw = (int) imgDraw.getX() + ((imgDiscard.getWidth() - width) / 2);
            int y = (int) txt.getY() + 10;

            // create the cropped version of the bitmap.
            Bitmap resizedBitmapDraw = Bitmap.createBitmap(bitmap, xDraw, y, width, height);
            Bitmap resizedBitmapDiscard = Bitmap.createBitmap(bitmap, xDiscard, y, width, height);

            FileOutputStream outputStreamDraw = new FileOutputStream(mypathDraw);
            FileOutputStream outputStreamDiscard = new FileOutputStream(mypathDiscard);

            int quality = 100;
            resizedBitmapDraw.compress(Bitmap.CompressFormat.JPEG, quality, outputStreamDraw);
            resizedBitmapDiscard.compress(Bitmap.CompressFormat.JPEG, quality, outputStreamDiscard);

            outputStreamDraw.flush();
            outputStreamDraw.close();

            outputStreamDiscard.flush();
            outputStreamDiscard.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, GameActivity.class);
    }
}