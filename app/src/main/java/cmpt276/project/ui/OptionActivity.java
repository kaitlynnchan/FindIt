package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import cmpt276.project.R;
import cmpt276.project.model.Mode;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package and mode
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_IMAGE_PACK = "shared preferences for image pack";
    public static final String EDITOR_IMAGE_PACK_ID = "id for image pack";
    public static final String EDITOR_MODE_ID = "id for mode button";
    private int imgButtonFruits;
    private int imgButtonVegs;
    private int buttonNormal;
    private int buttonWordsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgButtonFruits = R.id.imgButtonFruits;
        imgButtonVegs = R.id.imgButtonVegs;
        buttonNormal = R.id.buttonNormal;
        buttonWordsImages = R.id.buttonWordsImages;

        setupImageButton(imgButtonFruits);
        setupImageButton(imgButtonVegs);
        setupModeButton(buttonNormal);
        setupModeButton(buttonWordsImages);
        setupBackButton();
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

    private void setupModeButton(final int modeBtn) {
        Button button = findViewById(modeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveModeId(modeBtn);
                setupModeButton(buttonNormal);
                setupModeButton(buttonWordsImages);
            }
        });

        if(modeBtn == buttonNormal){
            String s = getString(R.string.normal);
            SpannableString string = new SpannableString(s);
            string.setSpan(new RelativeSizeSpan(0.75f), s.length() - 8, s.length(), Spanned.SPAN_COMPOSING);
            button.setText(string);
        }

        // Selecting/deselecting mode button
        if(getModeId(this) == modeBtn){
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

    public static String[] getWordArray(Context context){
        int[] imagePack = OptionActivity.getImagePackArray(context);
        String[] wordArray = new String[imagePack.length];
        for (int i = 0; i < imagePack.length; i++) {
            wordArray[i] = context.getResources().getResourceEntryName(imagePack[i]);
        }
        return wordArray;
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, R.id.imgButtonFruits);
    }

    public static Mode getMode(Context context){
        int modeId = OptionActivity.getModeId(context);
        if(modeId == R.id.buttonWordsImages){
            return Mode.WORD_IMAGES;
        } else{
            return Mode.NORMAL;
        }
    }

    private static int getModeId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_MODE_ID, R.id.buttonNormal);
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