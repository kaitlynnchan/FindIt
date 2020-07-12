package cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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
import cmpt276.project.model.ScoreRecording;
import cmpt276.project.model.ScoreRecordingManager;

//to show top five high score
public class HighScoreActivity extends AppCompatActivity {

    public static final String EDITOR_HIGH_SCORE_ARRAY = "editor high score array";
    public static final String SHARED_PREFS_HIGH_SCORE = "shared prefs high score";
    private ScoreRecordingManager manager;
    private TextView[] scoresText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        manager = ScoreRecordingManager.getInstance();
        manager.print();
        scoresText = new TextView[manager.getNumScores()];
        setupHighScore();

        //to reset high score
        Button btReset = findViewById(R.id.ResetButton);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.resetHighScore();
                populateScoreRecording(manager);
                setTexts();
            }
        });
    }

    public static void populateScoreRecording(ScoreRecordingManager manager){
        manager.addNewScore(new ScoreRecording(30, "??", "Month DD, YYYY"));
        manager.addNewScore(new ScoreRecording(45, "??", "Month DD, YYYY"));
        manager.addNewScore(new ScoreRecording(20, "??", "Month DD, YYYY"));
        manager.addNewScore(new ScoreRecording(50, "??", "Month DD, YYYY"));
        manager.addNewScore(new ScoreRecording(35, "??", "Month DD, YYYY"));
    }

    private void setupHighScore() {
        LinearLayout layout = findViewById(R.id.linearScoreBoard);
        for(int i = 0; i < manager.getNumScores(); i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);

            layout.addView(text);
            scoresText[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresText.length; i++){
            String msg = (i + 1) + ". "
                    + manager.getScoreArray().get(i).getTimeBySeconds() + "sec "
                    + manager.getScoreArray().get(i).getName() + " on "
                    + manager.getScoreArray().get(i).getDate();
            scoresText[i].setText(msg);
        }
    }

    public static void saveHighScores(Context context, ScoreRecordingManager manager){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HIGH_SCORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(manager.getScoreArray());
        editor.putString(EDITOR_HIGH_SCORE_ARRAY, json);
        editor.apply();
    }

    public static ArrayList<ScoreRecording> getSavedHighScore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HIGH_SCORE, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_HIGH_SCORE_ARRAY, null);
        Type type = new TypeToken<ArrayList<ScoreRecording>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }

    @Override
    public void onBackPressed() {
        saveHighScores(this, manager);
        super.onBackPressed();
    }
}