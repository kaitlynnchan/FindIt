package cmpt276.project.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cmpt276.project.R;
import cmpt276.project.model.GameConfigs;
import cmpt276.project.model.Score;
import cmpt276.project.model.ScoresManager;

/**
 * HIGH SCORE SCREEN
 * Displays top number of scores
 * Allows user to reset scores
 */
public class HighScoreActivity extends AppCompatActivity {

    public static final String EXTRA_INDEX = "extra for index";

    private GameConfigs gameConfigs;
    private ScoresManager scoresManager;
    private TextView[] scoresTxtView;
    private int numMaxScores;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        index = intent.getIntExtra(EXTRA_INDEX, -1);
        gameConfigs = GameConfigs.getInstance();
        scoresManager = gameConfigs.getScoreManager(index);
        numMaxScores = scoresManager.getNumMaxScores();
        scoresTxtView = new TextView[numMaxScores];

        setupHighScores();
        setupResetButton();
        setupBackButton();
    }

    private void setupHighScores() {
        LinearLayout layout = findViewById(R.id.linearLayoutScores);
        for(int i = 0; i < numMaxScores; i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setPadding(0, 0, 0, 0);

            layout.addView(text);
            scoresTxtView[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresTxtView.length; i++){
            int time = scoresManager.getScore(i).getTimeBySeconds();
            int minutes = time / 60;
            int seconds = time % 60;

            String strSeconds = seconds + "";
            if(seconds / 10 < 1){
                strSeconds = "0" + seconds;
            }
            String msg = (i + 1) + ".\t\t\t"
                    + minutes + ":" + strSeconds + "\t\t\t"
                    + scoresManager.getScore(i).getName() + "\t\t\t"
                    + scoresManager.getScore(i).getDate();

            scoresTxtView[i].setText(msg);
        }
    }

    private void setupResetButton() {
        Button btReset = findViewById(R.id.buttonReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoresManager.resetScoreArray();
                setDefaultScores(scoresManager);
                gameConfigs.getScoreManager(index).setScoreArray(scoresManager.getScoreArray());
                MainActivity.saveGameConfigs(HighScoreActivity.this, gameConfigs);
                setTexts();
            }
        });
    }

    public static void setDefaultScores(ScoresManager scoresManager){
        scoresManager.addScore(new Score(23, "N/A", "Month DD, YYYY"));
        scoresManager.addScore(new Score(28, "N/A", "Month DD, YYYY"));
        scoresManager.addScore(new Score(20, "N/A", "Month DD, YYYY"));
        scoresManager.addScore(new Score(25, "N/A", "Month DD, YYYY"));
        scoresManager.addScore(new Score(18, "N/A", "Month DD, YYYY"));
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context, int index){
        Intent intent = new Intent(context, HighScoreActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }
}