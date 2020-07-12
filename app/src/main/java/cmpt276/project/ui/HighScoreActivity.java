package cmpt276.project.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import cmpt276.project.model.ScoreBoard;

/**
 * HIGH SCORE ACTIVITY
 * Displays top number of high scores
 * Allows user to reset high scores
 */
public class HighScoreActivity extends AppCompatActivity {

    public static final String EDITOR_HIGH_SCORE_ARRAY = "editor high score array";
    public static final String SHARED_PREFS_HIGH_SCORE = "shared prefs for high score";

    private ScoreBoard scoreBoard;
    private TextView[] scoresTxtView;
    private int numScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scoreBoard = ScoreBoard.getInstance();
        numScores = scoreBoard.getNumScores();
        scoresTxtView = new TextView[numScores];
        setupHighScore();

        //to reset high score
        Button btReset = findViewById(R.id.buttonReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreBoard.resetHighScore();
                setDefaultScores(scoreBoard);
                setTexts();
            }
        });
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

            layout.addView(text);
            scoresTxtView[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresTxtView.length; i++){
            String msg = (i + 1) + ". "
                    + scoreBoard.getScoreArray().get(i).getTimeBySeconds() + "sec "
                    + scoreBoard.getScoreArray().get(i).getName() + " on "
                    + scoreBoard.getScoreArray().get(i).getDate();
            scoresTxtView[i].setText(msg);
        }
    }

    public static void setDefaultScores(ScoreBoard scoreBoard){
        scoreBoard.addScore(new Score(30, "??", "Month DD, YYYY"));
        scoreBoard.addScore(new Score(45, "??", "Month DD, YYYY"));
        scoreBoard.addScore(new Score(20, "??", "Month DD, YYYY"));
        scoreBoard.addScore(new Score(50, "??", "Month DD, YYYY"));
        scoreBoard.addScore(new Score(35, "??", "Month DD, YYYY"));
    }

    public static void saveHighScores(Context context, ScoreBoard scoreBoard){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HIGH_SCORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(scoreBoard.getScoreArray());
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

    @Override
    public void onBackPressed() {
        saveHighScores(this, scoreBoard);
        super.onBackPressed();
    }
}