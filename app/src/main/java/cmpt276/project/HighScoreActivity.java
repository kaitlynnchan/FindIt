package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import cmpt276.project.model.ScoreRecording;
import cmpt276.project.model.ScoreRecordingManager;

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

        Button btReset = findViewById(R.id.ResetButton);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.resetHighScore();
            }
        });
    }

    private void setUpHighScore() {
        //Implement set up high score:

        TextView tv = findViewById(R.id.ScoreRecording);
        /*
        //I'm confusing for sorting array;
        int index = 0;
        for (int i = 0; i < manager.getSize(); i++){
            index = i;
            for(int j = i + 1; j < manager.getSize(); j++){
                if(manager.ScoreArray.get(index).getTimeBySeconds() >manager.ScoreArray.get(j).getTimeBySecond()){
                    value = j;
                }
                if(value =! i){
                    ScoreRecording temp = manager.ScoreArray.get(i);
                    manager.ScoreArray.get(i) = manager.ScoreArray.get(index);
                    manager.ScoreArray.get(index) = temp;
                 }
            }
        }
        */
        for (int i = 0; i < 5; i++){
            String msg = String.format(manager.ScoreArray.get(i).getTimeBySeconds() + " sec "
                    + manager.ScoreArray.get(i).getName()  + " on "
                    + manager.ScoreArray.get(i).getMonth() + " "
                    + manager.ScoreArray.get(i).getDay() + ", "
                    + manager.ScoreArray.get(i).getYear());
            tv.setText(msg);
        }
    }



}