package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Options Screen
 * Allows users to select from different image packs
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_IMAGE_PACK = "save image pack";
    public static final String EDITOR_IMAGE_PACK_ID = "imagePack id";

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
        final ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImagePack(imageId);
            }
        });
    }

    private void saveImagePack(int imagePack) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    public static int[] getImagePackArray(Context context){
        int imageButtonID = OptionActivity.getImagePackID(context);
        int[] imagePack;
        if(imageButtonID == R.id.imgBtnVegetable){
            imagePack = new int[]{R.drawable.broccoli, R.drawable.carrot, R.drawable.eggplant,
                    R.drawable.lettuce, R.drawable.mushroom, R.drawable.onion,
                    R.drawable.radish};
        } else{
            imagePack = new int[]{R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                    R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                    R.drawable.watermelon};
        }
        return imagePack;
    }

    private static int getImagePackID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, -1);
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