package cmpt276.project.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

import cmpt276.project.R;
import cmpt276.project.model.Mode;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package and mode
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_OPTIONS = "shared_preferences_options";
    private static final String EDITOR_IMAGE_PACK_ID = "id_image_pack";
    private static final String EDITOR_NUM_IMAGES_PER_CARD = "num_images_per_card";
    private static final String EDITOR_NUM_CARDS = "num_cards";
    private static final String EDITOR_MODE_ID = "id_mode_button";
    private static final String EDITOR_DIFFICULTY_MODE = "difficulty_mode_string";
    private static final int REQUEST_CODE_CUSTOM = 42;

    private static int imgButtonFruits = R.id.imgButtonFruits;
    private static int imgButtonVegs = R.id.imgButtonVegs;
    private static int imgButtonCustom = R.id.imgButtonCustom;
    private static int buttonImages = R.id.buttonImages;
    private static int buttonWordsImages = R.id.buttonWordsImages;
    private static final int DEFAULT_IMAGE_PACK = imgButtonFruits;
    private static final int DEFAULT_MODE_BUTTON = buttonImages;

    private static int numCustomImages;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        numCustomImages = CustomImagesActivity.getNumCustomImages(this);

        setupImageButton(imgButtonFruits);
        setupImageButton(imgButtonVegs);
        setupImageButton(imgButtonCustom);
        setupModeButton(buttonImages);
        setupModeButton(buttonWordsImages);

        setNumImagesPerCardSpinner();
        setNumCardsSpinner();
        setDifficultyModeSpinner();

        setupBackButton();
    }

    private void setupImageButton(final int imageId) {
        ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageId == imgButtonCustom){
                    Intent intent = CustomImagesActivity.makeLaunchIntent(OptionsActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_CUSTOM);
                    if(getImagePackId(OptionsActivity.this) == imgButtonCustom){
                        saveImagePackId(DEFAULT_IMAGE_PACK);
                    }
                } else{
                    saveImagePackId(imageId);

                    setupImageButton(imgButtonFruits);
                    setupImageButton(imgButtonVegs);
                    setupImageButton(imgButtonCustom);
                }
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
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, DEFAULT_IMAGE_PACK);
    }

    private void setupModeButton(final int modeBtn) {
        Button button = findViewById(modeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modeBtn == buttonWordsImages && getImagePackId(OptionsActivity.this) == imgButtonCustom){
                    Toast.makeText(OptionsActivity.this, R.string.toast_options_mode, Toast.LENGTH_SHORT).show();
                } else{
                    saveModeId(modeBtn);

                    setupModeButton(buttonImages);
                    setupModeButton(buttonWordsImages);
                }
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

    private void saveModeId(int mode) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_MODE_ID, mode);
        editor.apply();
    }

    private static int getModeId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_MODE_ID, DEFAULT_MODE_BUTTON);
    }

    public static Object[] getPackArray(Context context){
        int imageButtonID = getImagePackId(context);
        Object[] packArr;
        if(imageButtonID == imgButtonCustom){
            packArr = CustomImagesActivity.getCustomArr(context);
        } else if(imageButtonID == imgButtonVegs){
            packArr =  new Object[]{R.drawable.broccoli, R.drawable.carrot, R.drawable.eggplant,
                    R.drawable.lettuce, R.drawable.mushroom, R.drawable.onion, R.drawable.radish,
                    R.drawable.artichoke, R.drawable.asparagus, R.drawable.cabbage,
                    R.drawable.cauliflower, R.drawable.celery, R.drawable.corn, R.drawable.cucumber,
                    R.drawable.garlic, R.drawable.ginger, R.drawable.green_bell_pepper,
                    R.drawable.kale, R.drawable.leek, R.drawable.okra, R.drawable.parsnip,
                    R.drawable.peas, R.drawable.potato, R.drawable.red_bell_pepper,
                    R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.spinach,
                    R.drawable.turnip, R.drawable.yam, R.drawable.yellow_bell_pepper,
                    R.drawable.zucchini};
        } else{
            packArr = new Object[]{R.drawable.red_apple, R.drawable.green_apple, R.drawable.lemon,
                    R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                    R.drawable.watermelon, R.drawable.avocado, R.drawable.banana, R.drawable.blackberry,
                    R.drawable.blueberry, R.drawable.cherry, R.drawable.coconut,
                    R.drawable.cranberry, R.drawable.dragon_fruit, R.drawable.durian, R.drawable.fig,
                    R.drawable.grapefruit, R.drawable.grapes, R.drawable.kiwi,
                    R.drawable.melon, R.drawable.papaya, R.drawable.peach, R.drawable.pear,
                    R.drawable.pineapple, R.drawable.plum, R.drawable.pomegranate,
                    R.drawable.raspberry, R.drawable.squash, R.drawable.starfruit, R.drawable.strawberry};
        }

        if(getModeId(context) == buttonWordsImages){
            for(int i = 0; i < packArr.length; i++){
                String temp = context.getResources().getResourceEntryName((int) packArr[i]);
                packArr[i] += "," + temp.replace("_", " ");
            }
        }
        return packArr;
    }

    private void setNumImagesPerCardSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumImagesPerCard);
        String[] numImagesPerCardArray = getResources().getStringArray(R.array.numImagesPerCardArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, numImagesPerCardArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                int numImagesPerCard = Integer.parseInt(value);
                if(numCustomImages < getTotalNumCardsOrImages(numImagesPerCard)
                        && getImagePackId(OptionsActivity.this) == imgButtonCustom){
                    Toast.makeText(OptionsActivity.this, R.string.toast_options, Toast.LENGTH_SHORT).show();
                    setNumImagesPerCardSpinner();
                } else{
                    saveNumImagesPerCard(numImagesPerCard);
                    setNumCardsSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < numImagesPerCardArray.length; i++){
            if(numImagesPerCardArray[i].equals("" + getNumImagesPerCard(this))){
                spinner.setSelection(i);
            }
        }
    }

    private void saveNumImagesPerCard(int numImagesPerCard){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_IMAGES_PER_CARD, numImagesPerCard);
        editor.apply();
    }

    public static int getNumImagesPerCard(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValueStr = context.getResources().getString(R.string.defaultNumImagesPerCard);
        int defaultValue = Integer.parseInt(defaultValueStr);
        return sharedPreferences.getInt(EDITOR_NUM_IMAGES_PER_CARD, defaultValue);
    }

    private void setNumCardsSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumCards);
        String[] numCardsArray = getResources().getStringArray(R.array.numCardsArray);
        numCardsArray = setRangeArray(numCardsArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, numCardsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.equals(getString(R.string.all))) {
                    saveNumCards(getTotalNumCardsOrImages(getNumImagesPerCard(getBaseContext())));
                } else {
                    saveNumCards(Integer.parseInt(value));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < numCardsArray.length; i++){
            if(i == 0){
                if(getNumCards(this) == getTotalNumCardsOrImages(getNumImagesPerCard(this))){
                    spinner.setSelection(i);
                }
            } else if(numCardsArray[i].equals("" + getNumCards(this))){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setRangeArray(String[] numCardsArray) {
        int totalCards = getTotalNumCardsOrImages(getNumImagesPerCard(this));
        for(int i = 0; i < numCardsArray.length; i++){
            if(!numCardsArray[i].equals(getString(R.string.all))){
                if(Integer.parseInt(numCardsArray[i]) > totalCards){
                    return Arrays.copyOfRange(numCardsArray, 0, i);
                }
            }
        }
        return new String[]{getString(R.string.all)};
    }

    private static int getTotalNumCardsOrImages(int numImagesPerCard) {
        return (numImagesPerCard * numImagesPerCard) - numImagesPerCard + 1;
    }

    private void saveNumCards(int numCards){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_CARDS, numCards);
        editor.apply();
    }

    public static int getNumCards(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValueStr = context.getResources().getString(R.string.defaultNumCards);
        int defaultValue;
        if(defaultValueStr.equals(context.getString(R.string.all))){
            defaultValue = getTotalNumCardsOrImages(getNumImagesPerCard(context));
        } else{
            defaultValue = Integer.parseInt(defaultValueStr);
        }
        return sharedPreferences.getInt(EDITOR_NUM_CARDS, defaultValue);
    }

    private void setDifficultyModeSpinner() {
        Spinner spinner = findViewById(R.id.difficultyModeSpinner);
        String[] difficultyModeArray = getResources().getStringArray(R.array.difficultyModeArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, difficultyModeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                saveDifficultyMode(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < difficultyModeArray.length; i++){
            if(difficultyModeArray[i].equals(getDifficultyModeStr(this))){
                spinner.setSelection(i);
            }
        }
    }

    private void saveDifficultyMode(String difficultyMode) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EDITOR_DIFFICULTY_MODE, difficultyMode);
        editor.apply();
    }

    private static String getDifficultyModeStr(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValue = context.getResources().getString(R.string.defaultDifficultyMode);
        return sharedPreferences.getString(EDITOR_DIFFICULTY_MODE, defaultValue);
    }

    public static Mode getDifficultyMode(Context context){
        String difficultyMode = getDifficultyModeStr(context);
        if(difficultyMode.equals(context.getString(R.string.easy))){
            return Mode.EASY;
        } else if(difficultyMode.equals(context.getString(R.string.normal))){
            return Mode.NORMAL;
        } else{
            return Mode.HARD;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        switch (requestCode){
            case REQUEST_CODE_CUSTOM:
                numCustomImages = CustomImagesActivity.getNumCustomImages(this);
                int minNumImagesPerCard = Integer.parseInt(getString(R.string.minNumImagesPerCard));
                if(numCustomImages >= getTotalNumCardsOrImages(minNumImagesPerCard)){
                    saveImagePackId(imgButtonCustom);

                    if(numCustomImages < getTotalNumCardsOrImages(getNumImagesPerCard(this))){
                        Toast.makeText(OptionsActivity.this, R.string.toast_options, Toast.LENGTH_SHORT).show();

                        int maxNumImagesPerCard = getMaxNumImagePerCard();
                        saveNumImagesPerCard(maxNumImagesPerCard);
                        setNumImagesPerCardSpinner();
                        setNumCardsSpinner();
                    }

                    if(getModeId(this) == buttonWordsImages){
                        Toast.makeText(OptionsActivity.this, R.string.toast_options_mode, Toast.LENGTH_SHORT).show();
                        saveModeId(DEFAULT_MODE_BUTTON);
                        setupModeButton(buttonImages);
                        setupModeButton(buttonWordsImages);
                    }
                } else{
                    Toast.makeText(OptionsActivity.this, R.string.toast_no_images, Toast.LENGTH_SHORT).show();
                    if(getImagePackId(this) == imgButtonCustom){
                        saveImagePackId(DEFAULT_IMAGE_PACK);
                    }
                }
                setupImageButton(imgButtonFruits);
                setupImageButton(imgButtonVegs);
                setupImageButton(imgButtonCustom);

                break;
            default:
                assert false;
        }
    }

    private int getMaxNumImagePerCard() {
        int temp = Integer.parseInt(getString(R.string.defaultNumImagesPerCard));
        String[] numImagesPerCardArray = getResources().getStringArray(R.array.numImagesPerCardArray);
        for (String s : numImagesPerCardArray) {
            if (getTotalNumCardsOrImages(Integer.parseInt(s)) <= numCustomImages) {
                temp = Integer.parseInt(s);
            }
        }
        return temp;
    }

    private void setupBackButton() {
        Button btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}