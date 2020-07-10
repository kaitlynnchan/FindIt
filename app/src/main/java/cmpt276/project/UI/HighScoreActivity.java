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

    ScoreRecordingManager manager;

    public HighScoreActivity(ScoreRecordingManager manager){
        this.manager = manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

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

        manager.selectionSort();
        for (int i = 0; i < 5; i++){
            String msg = String.format(manager.scoreArray[i].getTimeBySeconds() + " sec "
                    + manager.scoreArray[i].getName()  + " on "
                    + manager.scoreArray[i].getDate());
            tv.setText(msg);
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }
}