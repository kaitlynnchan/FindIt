package cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import cmpt276.project.R;
import cmpt276.project.model.ScoreRecording;
import cmpt276.project.model.ScoreRecordingManager;

//to show top five high score
public class HighScoreActivity extends AppCompatActivity {

    private ScoreRecordingManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        manager = new ScoreRecordingManager(5);

        populateScoreRecording();
        manager.print();
        setupHighScore();
//
//        //to reset high score
//        Button btReset = findViewById(R.id.ResetButton);
//        btReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manager.resetHighScore();
//            }
//        });

    }

    void populateScoreRecording(){
        manager.addNewScore(new ScoreRecording(0, "kk", "June 16"));
        manager.addNewScore(new ScoreRecording(45, "kk", "June 16"));
        manager.addNewScore(new ScoreRecording(20, "kk", "June 16"));
        manager.addNewScore(new ScoreRecording(60, "kk", "June 16"));
        manager.addNewScore(new ScoreRecording(80, "kk", "June 16"));
        manager.addNewScore(new ScoreRecording(100, "kk", "June 16"));
    }

    private void setupHighScore() {
        //Implement set up top five high score:

        LinearLayout layout = findViewById(R.id.linearScoreBoard);

        for(int i = 0; i < manager.getNumScores(); i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            String msg = manager.getScoreArray().get(i).getTimeBySeconds() + " sec "
                    + manager.getScoreArray().get(i).getName() + " on "
                    + manager.getScoreArray().get(i).getDate();
            text.setText(msg);
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            layout.addView(text);
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }
}