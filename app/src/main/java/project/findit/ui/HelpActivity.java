package project.findit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.findit.R;

/**
 * HELP SCREEN
 * Displays how to play the game,
 *  and the necessary citations as required.
 */
public class HelpActivity extends AppCompatActivity {

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView textContent = findViewById(R.id.text_content);
        textContent.setMovementMethod(LinkMovementMethod.getInstance());

        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}