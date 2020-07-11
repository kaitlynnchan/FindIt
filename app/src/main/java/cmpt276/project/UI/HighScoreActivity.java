package cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.project.R;
import cmpt276.project.model.ScoreRecordingManager;

//to show top five high score
public class HighScoreActivity extends AppCompatActivity {

    private ScoreRecordingManager manager;
    GameActivity g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        manager = g.manager;

        setUpHighScore();

        //to reset high score
        Button btReset = findViewById(R.id.ResetButton);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.resetHighScore();
            }
        });

    }

    private void setUpHighScore() {
        //Implement set up top five high score:
        TextView tv = findViewById(R.id.ScoreRecording);
        if (manager.getScoreArray().length >= 5) {
            manager.selectionSort();
            for (int i = 0; i < 5; i++) {
                String msg = manager.getScoreArray()[i].getTimeBySeconds() + " sec "
                        + manager.getScoreArray()[i].getName() + " on "
                        + manager.getScoreArray()[i].getDate();
                tv.setText(msg);
            }
        }
        else{
            manager.selectionSort();
            for (int i = 0; i < manager.getScoreArray().length; i++) {
                String msg = manager.getScoreArray()[i].getTimeBySeconds() + " sec "
                        + manager.getScoreArray()[i].getName() + " on "
                        + manager.getScoreArray()[i].getDate();
                tv.setText(msg);
            }
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HighScoreActivity.class);
    }
}