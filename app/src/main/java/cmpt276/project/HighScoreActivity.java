package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        
        setUpHighScore();
    }

    private void setUpHighScore() {
        //Implement set up high score:

        //TextView tv = findViewById(R.id.ScoreRecording);
        //for (int i = 0; i < 5; i++){
        //String msg = String.format(time[i] + " " + name[i]
        // + " on " + month[i] + " " + day[i] ", " + year[i]);
        //tv.setText(msg);
        //}
    }


}