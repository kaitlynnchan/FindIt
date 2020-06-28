package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;

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
    private int numImages = 3;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startTimer();
        setupDiscardCard();
        setupDrawCard();
    }

    private void setupDrawCard() {
        TableLayout tableDraw = findViewById(R.id.tableLayoutDraw);

        for(int i = 0; i < numImages; i++){
            ImageButton imageDraw = new ImageButton(this);
            imageDraw.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            tableDraw.addView(imageDraw);
        }
    }

    private void setupDiscardCard() {
        TableLayout tableDiscard = findViewById(R.id.tableLayoutDiscard);

        for(int i = 0; i < numImages; i++){
            ImageView imageDiscard = new ImageView(this);
            imageDiscard.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            tableDiscard.addView(imageDiscard);
        }
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