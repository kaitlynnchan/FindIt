package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import cmpt276.project.R;

/**
 * <h1>HELP SCREEN</h1>
 *  Displays how to play the game, a hyperlink to the 276 website and the necessary citations as required.
 * <p>
 * @author  Lakshay Sethi & .........
 * @since   2020-07-08
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btn = findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }

}