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
import cmpt276.project.model.Score;
import cmpt276.project.model.HighScores;

/**
 * HIGH SCORE SCREEN
 * Displays top number of high scores
 * Allows user to reset high scores
 */
public class HighScoreActivity extends AppCompatActivity {

    public static final String EDITOR_HIGH_SCORE_ARRAY = "editor high score array";
    public static final String SHARED_PREFS_HIGH_SCORE = "shared prefs for high score";

    private HighScores highScores;
    private TextView[] scoresTxtView;
    private int numScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        highScores = HighScores.getInstance();
        numScores = highScores.getNumScores();
        scoresTxtView = new TextView[numScores];
        setupHighScore();

        setupResetButton();
        setupBackButton();
    }

    private void setupHighScore() {
        LinearLayout layout = findViewById(R.id.linearScoreBoard);
        for(int i = 0; i < numScores; i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setPadding(50, 20, 0, 0);

            layout.addView(text);
            scoresTxtView[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresTxtView.length; i++){
            String msg = (i + 1) + ". "
                    + highScores.getScoreArray().get(i).getTimeBySeconds() + "sec "
                    + highScores.getScoreArray().get(i).getName() + " on "
                    + highScores.getScoreArray().get(i).getDate();
            scoresTxtView[i].setText(msg);
        }
    }

    private void setupResetButton() {
        Button btReset = findViewById(R.id.btnReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highScores.resetHighScore();
                setDefaultScores(highScores);
                setTexts();
            }
        });
    }

    public static void setDefaultScores(HighScores highScores){
        highScores.addScore(new Score(30, "N/A", "Month DD, YYYY"));
        highScores.addScore(new Score(45, "N/A", "Month DD, YYYY"));
        highScores.addScore(new Score(20, "N/A", "Month DD, YYYY"));
        highScores.addScore(new Score(50, "N/A", "Month DD, YYYY"));
        highScores.addScore(new Score(35, "N/A", "Month DD, YYYY"));
    }

    public static void saveHighScores(Context context, HighScores highScores){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HIGH_SCORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highScores.getScoreArray());
        editor.putString(EDITOR_HIGH_SCORE_ARRAY, json);
        editor.apply();
    }

    public static ArrayList<Score> getSavedHighScore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HIGH_SCORE, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_HIGH_SCORE_ARRAY, null);
        Type type = new TypeToken<ArrayList<Score>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHighScores(HighScoreActivity.this, highScores);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveHighScores(this, highScores);
        super.onBackPressed();
    }
}