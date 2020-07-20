package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import cmpt276.project.R;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_IMAGE_PACK = "shared preferences for image pack";
    public static final String EDITOR_IMAGE_PACK_ID = "id for image pack";
    public static final String EDITOR_MODE_ID = "id for mode button";
    private int imgButtonFruits;
    private int imgButtonVegs;
    private int buttonRegular;
    private int buttonWordsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgButtonFruits = R.id.imgButtonFruits;
        imgButtonVegs = R.id.imgButtonVegs;
        buttonRegular = R.id.buttonRegular;
        buttonWordsImages = R.id.buttonWordsImages;

        setupImageButton(imgButtonFruits);
        setupImageButton(imgButtonVegs);
        setupModeButton(buttonRegular);
        setupModeButton(buttonWordsImages);
        setupBackButton();
    }

    private void setupModeButton(final int modeBtn) {
        Button button = findViewById(modeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveModeId(modeBtn);
                setupModeButton(buttonRegular);
                setupModeButton(buttonWordsImages);
            }
        });

        // Selecting/deselecting mode button
        if(getModeId(this) == modeBtn){
            button.setForegroundGravity(Gravity.END|Gravity.BOTTOM);
            button.setForeground(getDrawable(R.drawable.drawable_magnifying_glass));
        } else{
            button.setForeground(null);
        }
    }

    private void setupImageButton(final int imageId) {
        ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImagePackId(imageId);
                setupImageButton(imgButtonFruits);
                setupImageButton(imgButtonVegs);
            }
        });

        // Selecting/deselecting image package
        if(getImagePackId(this) == imageId){
            button.setForegroundGravity(Gravity.END|Gravity.BOTTOM);
            button.setForeground(getDrawable(R.drawable.drawable_magnifying_glass));
        } else{
            button.setForeground(null);
        }
    }

    private void saveImagePackId(int imagePack) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    private void saveModeId(int mode) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_MODE_ID, mode);
        editor.apply();
    }

    public static int[] getImagePackArray(Context context){
        int imageButtonId = OptionActivity.getImagePackId(context);
        if(imageButtonId == R.id.imgButtonVegs){
            return new int[]{R.drawable.broccoli, R.drawable.carrot, R.drawable.eggplant,
                    R.drawable.lettuce, R.drawable.mushroom, R.drawable.onion,
                    R.drawable.radish};
        } else{
            return new int[]{R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                    R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                    R.drawable.watermelon};
        }
    }

    public static String[] getWordPackArray(Context context){
        int imageButtonId = OptionActivity.getImagePackId(context);
        if(imageButtonId == R.id.imgButtonVegs){
            return new String[]{"broccoli", "carrot", "eggplant", "lettuce", "mushroom", "onion", "radish"};
        } else{
            return new String[]{"red apple", "green apple", "lemon", "mango", "orange", "pumpkin", "watermelon"};
        }
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, R.id.imgButtonFruits);
    }

    public static int getMode(Context context){
        int modeId = OptionActivity.getModeId(context);
        if(modeId == R.id.buttonWordsImages){
            return 1;
        } else{
            return 0;
        }
    }

    private static int getModeId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_MODE_ID, R.id.buttonRegular);
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, OptionActivity.class);
    }
}