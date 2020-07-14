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
import cmpt276.project.model.ScoresManager;

/**
 * HIGH SCORE SCREEN
 * Displays top number of scores
 * Allows user to reset scores
 */
public class HighScoreActivity extends AppCompatActivity {

    public static final String EDITOR_SCORES_ARRAY = "editor for scores array";
    public static final String SHARED_PREFS_SCORES = "shared prefs for scores";

    private ScoresManager scoresManager;
    private TextView[] scoresTxtView;
    private int numMaxScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scoresManager = ScoresManager.getInstance();
        numMaxScores = scoresManager.getNumMaxScores();
        scoresTxtView = new TextView[numMaxScores];

        scoresManager.print();
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

    public static void saveScores(Context context, ScoresManager scoresManager){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_SCORES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(scoresManager.getScoreArray());
        editor.putString(EDITOR_SCORES_ARRAY, json);
        editor.apply();
    }

    public static ArrayList<Score> getSavedScores(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_SCORES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_SCORES_ARRAY, null);
        Type type = new TypeToken<ArrayList<Score>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScores(HighScoreActivity.this, scoresManager);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveScores(this, scoresManager);
        super.onBackPressed();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }
}