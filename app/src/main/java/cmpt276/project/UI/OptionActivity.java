package cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import cmpt276.project.R;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requiredButton(R.id.btnBack);
        requiredButton(R.id.btnReset);
        requiredImageButton(R.id.imgBtnFruit);
        requiredImageButton(R.id.imgBtnVegetable);


    }

    private void requiredImageButton(final int imageId) {
        ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(R.id.imgBtnFruit == imageId){
                    // TO BE CONNECTED TO THE SINGLETON
                }
                else{
                    // TO BE CONNECTED TO THE SINGLETON
                }
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
        return new Intent(context , OptionActivity.class);
    }

}