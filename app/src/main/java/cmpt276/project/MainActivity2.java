package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requiredButton(R.id.btnBack);
        requiredButton(R.id.btnReset);
        requiredImageButton(R.id.imageButton2);
        requiredImageButton(R.id.imageButton3);


    }

    private void requiredImageButton(int imageId) {
        ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requiredButton(final int buttonId) {
        Button btn = findViewById(buttonId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(R.id.btnBack == buttonId){
                    finish();
                }
                else{
                    // TO BE CONNECTED TO THE SINGLETON
                }

            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context , MainActivity2.class);
    }

}