package cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        populateScoreRecording();
        setupHighScore();

        //to reset high score
        Button btReset = findViewById(R.id.ResetButton);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.resetHighScore();
            }
        });

    }

    void populateScoreRecording(){
        //I have no idea how to add scoreRecording which is from game activity
    }

    private void setupHighScore() {
        //Implement set up top five high score:

        TextView tv = (TextView) findViewById(R.id.scoreOutput);
        for (int i = 0; i < manager.getScoreArray().size(); i++) {
            String msg = manager.getScoreArray().get(i).getTimeBySeconds() + " sec "
                    + manager.getScoreArray().get(i).getName() + " on "
                    + manager.getScoreArray().get(i).getDate();
            tv.setText(msg);
        }

    }


    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }
}