package project.findit.ui;

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
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import project.findit.R;
import project.findit.model.CardDeck;
import project.findit.model.SoundEffect;

/**
 * GAME SCREEN
 * Displays score/timer, discard pile, and draw pile
 */
public class GameActivity extends AppCompatActivity {

    private static final String FILE_EXPORT_CARDS = "file_export_cards";
    private static final String TAG_WIN_DIALOG = "tag_win_dialog";

    private Chronometer timer;
    private CardDeck cardDeck;
    private int numImagesPerCard;
    private Button[] drawCard;
    private Button[] discardCard;
    private SoundPool soundPool;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cardDeck = CardDeck.getInstance();
        numImagesPerCard = cardDeck.getNumImagesPerCard();

        drawCard = new Button[numImagesPerCard];
        discardCard = new Button[numImagesPerCard];

        setupSounds();
        setupDrawCard();
        setupDiscardCard();
        startGame();
        setupBackButton();
    }

    private void setupSounds() {
        soundPool = SoundEffect.buildSoundPool();
        SoundEffect.loadSounds(this, soundPool);
    }

    private void setupDrawCard() {
        TableLayout tableDraw = findViewById(R.id.table_draw);
        for(int i = 0; i < numImagesPerCard; i++){
            final int cardIndex = i;
            Button button = new Button(this);
            button.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f));

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
            drawCard[i] = button;
            drawCard[i].setVisibility(View.INVISIBLE);
        }
    }

    // The buttons are purposely unresponsive so the user can only click on the draw card
    private void setupDiscardCard() {
        TableLayout tableDiscard = findViewById(R.id.table_discard);
        for(int i = 0; i < numImagesPerCard; i++){
            Button button = new Button(this);
            button.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            button.setPadding(0, 0, 0, 0);

            tableDiscard.addView(button);
            discardCard[i] = button;
            discardCard[i].setVisibility(View.INVISIBLE);
        }
    }

    private void imageClicked(int index) {
        if (cardDeck.searchDiscardCard(index)) {
            SoundEffect.playSound(soundPool, SoundEffect.CORRECT);
            if (cardDeck.getCurrentCardIndex() == cardDeck.getNumCards() - 1) {
                takeScreenshot(cardDeck.getCurrentCardIndex());
                stopTimer();
            } else {
                takeScreenshot(cardDeck.getCurrentCardIndex());
                cardDeck.incrementCardIndex();
                updateCard();
            }
        } else{
            SoundEffect.playSound(soundPool, SoundEffect.INCORRECT);
        }
    }

    private void updateCard() {
        for (int i = 0; i < numImagesPerCard; i++) {
            Object[] drawObject = cardDeck.getCardObject(cardDeck.getCurrentCardIndex(), i);
            Object[] discardObject = cardDeck.getCardObject(cardDeck.getCurrentCardIndex() - 1, i);

            lockButtonSizes(drawCard);
            lockButtonSizes(discardCard);

            setButton(drawObject, drawCard[i]);
            setButton(discardObject, discardCard[i]);
        }
    }

    private void lockButtonSizes(Button[] card) {
        for (int i = 0; i < card.length; i++) {
            Button button = card[i];

            int height = button.getHeight();
            button.setMinHeight(height);
            button.setMaxHeight(height);

            int width = button.getWidth();
            button.setMinWidth(width);
            button.setMaxWidth(width);
        }
    }

    private void setButton(Object[] object, Button button) {
        Object value = object[0];
        int rotation = Integer.parseInt((String) object[1]);
        int scale = Integer.parseInt((String) object[2]);
        try {
            int image = Integer.parseInt((String) value);
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), image);
            setButtonImage(button, rotation, scale, originalBitmap);
        } catch (NumberFormatException e) {
            System.out.println("not image");
            try {
                byte[] encodeByte = Base64.decode((String) value, Base64.DEFAULT);
                Bitmap bitmapCustom = BitmapFactory
                        .decodeByteArray(encodeByte, 0, encodeByte.length);

                setButtonImage(button, rotation, scale, bitmapCustom);
            } catch (Exception ex) {
                System.out.println("not bitmap image");
                String word = "" + value;
                SpannableString strSpannable = new SpannableString(word);
                strSpannable.setSpan(
                        new RelativeSizeSpan(2f / (float) scale),
                        0,
                        word.length(),
                        Spanned.SPAN_COMPOSING);

                button.setAllCaps(false);
                button.setText(strSpannable);
                button.setRotation(rotation);
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

    private void startGame() {
        final Button buttonStartGame = findViewById(R.id.button_start_game);
        buttonStartGame.setSoundEffectsEnabled(false);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                updateCard();

                ImageView imageDiscard = findViewById(R.id.image_discard_card);
                imageDiscard.setVisibility(View.VISIBLE);
                ImageView imageCard = findViewById(R.id.image_card_back);
                imageCard.setVisibility(View.GONE);

                buttonStartGame.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startTimer() {
        timer = findViewById(R.id.chronometer_timer);
        timer.start();
        timer.setBase(SystemClock.elapsedRealtime());
        cardDeck.incrementCardIndex();

        SoundEffect.playSound(soundPool, SoundEffect.START);
    }

    private void stopTimer() {
        timer.stop();

        SoundEffect.playSound(soundPool, SoundEffect.WIN);

        // divide by 1000 to convert values from milliseconds to seconds
        int timeInSeconds = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000.0);
        FragmentManager manager = getSupportFragmentManager();
        WinDialog dialog = new WinDialog(timeInSeconds);
        dialog.show(manager, TAG_WIN_DIALOG);
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    // Take Screen Shot of the card.
    // Citation: https://stackoverflow.com/questions/2661536/how-to-programmatically-take-a-screenshot-on-android .
    private void takeScreenshot(int imgNum) {
        Date date = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);

        try {
            // image naming and path, appending name you choose for file.
            ContextWrapper cw = new ContextWrapper(getBaseContext());
            File directory = cw.getDir(FILE_EXPORT_CARDS, Context.MODE_PRIVATE);

            File mypathDraw = new File(directory, "Draw"+imgNum+".jpg");
            File mypathDiscard = new File(directory, "Discard"+imgNum+".jpg");

            // create bitmap screen capture
            View view = getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            ImageView imgDiscard = findViewById(R.id.image_discard_card);
            ImageView imgDraw = findViewById(R.id.image_draw_card);
            TextView txt = findViewById(R.id.text_discard);
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
}