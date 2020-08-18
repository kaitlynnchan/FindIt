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
    private static final String EDITOR_NUM_IMAGES = "num_images";
    private static final String EDITOR_CARD_DECK_SIZE = "card_deck_size";
    private static final String EDITOR_MODE_ID = "id_mode_button";
    private static final String EDITOR_DIFFICULTY_MODE = "difficulty_mode_string";
    private static final int REQUEST_CODE_CUSTOM = 42;

    private static int imgButtonFruits = R.id.imgButtonFruits;
    private static int imgButtonVegs = R.id.imgButtonVegs;
    private static int imgButtonCustom = R.id.imgButtonCustom;
    private static int buttonImages = R.id.buttonImages;
    private static int buttonWordsImages = R.id.buttonWordsImages;
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

        setNumImagesSpinner();
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
                        saveImagePackId(imgButtonFruits);
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
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, imgButtonFruits);
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
        return sharedPreferences.getInt(EDITOR_MODE_ID, buttonImages);
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

        if(getModeId(context) == buttonWordsImages && imageButtonID != imgButtonCustom){
            for(int i = 0; i < packArr.length; i++){
                String temp = context.getResources().getResourceEntryName((int) packArr[i]);
                packArr[i] += "," + temp.replace("_", " ");
            }
        }
        return packArr;
    }

    private void setNumImagesSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumImages);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.numImagesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                int numImages = Integer.parseInt(text);

                if(numCustomImages < getNumCardsTotal(numImages)
                        && getImagePackId(OptionsActivity.this) == imgButtonCustom){
                    Toast.makeText(OptionsActivity.this, R.string.toast_options, Toast.LENGTH_LONG).show();
                    setNumImagesSpinner();
                } else{
                    saveNumImages(numImages);
                    setNumCardsSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] numImagesArray = getResources().getStringArray(R.array.numImagesArray);
        for(int i = 0; i < numImagesArray.length; i++){
            if(numImagesArray[i].equals("" + getNumImages(this))){
                spinner.setSelection(i);
            }
        }
    }

    private void saveNumImages(int numImages){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_IMAGES, numImages);
        editor.apply();
    }

    public static int getNumImages(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_NUM_IMAGES, 3);
    }

    private void setNumCardsSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumCards);
        final String[] cardDeckSizeArray = getResources().getStringArray(R.array.cardDeckSizeArray);
        String[] textArray = setTextArray(cardDeckSizeArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, textArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                if(text.equals(cardDeckSizeArray[0])) {
                    saveCardDeckSize(getNumCardsTotal(getNumImages(getBaseContext())));
                } else {
                    saveCardDeckSize(Integer.parseInt(text));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < textArray.length; i++){
            if(i == 0){
                if(getCardDeckSize(this) == getNumCardsTotal(getNumImages(this))){
                    spinner.setSelection(i);
                }
            } else if(textArray[i].equals("" + getCardDeckSize(this))){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setTextArray(String[] cardDeckSizeArray) {
        int totalCards = getNumCardsTotal(getNumImages(this));
        for(int i = 1; i < cardDeckSizeArray.length; i++){
            if(Integer.parseInt(cardDeckSizeArray[i]) > totalCards){
                return Arrays.copyOfRange(cardDeckSizeArray, 0, i);
            }
        }
        return Arrays.copyOfRange(cardDeckSizeArray, 0, cardDeckSizeArray.length);
    }

    private static int getNumCardsTotal(int numImages) {
        return (numImages * numImages) - numImages + 1;
    }

    private void saveCardDeckSize(int cardDeckSize){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_CARD_DECK_SIZE, cardDeckSize);
        editor.apply();
    }

    public static int getCardDeckSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_CARD_DECK_SIZE, 5);
    }

    private void setDifficultyModeSpinner() {
        Spinner spinner = findViewById(R.id.modeSpinner);
        String[] modeArray = getResources().getStringArray(R.array.modeArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, modeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                saveDifficultyMode(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < modeArray.length; i++){
            if(modeArray[i].equals(getDifficultyModeStr(this))){
                spinner.setSelection(i);
            }
        }
    }

    private void saveDifficultyMode(String mode) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EDITOR_DIFFICULTY_MODE, mode);
        editor.apply();
    }

    private static String getDifficultyModeStr(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String[] modeArray = context.getResources().getStringArray(R.array.modeArray);
        return sharedPreferences.getString(EDITOR_DIFFICULTY_MODE, modeArray[0]);
    }

    public static Mode getDifficultyMode(Context context){
        String difficultyMode = getDifficultyModeStr(context);
        String[] modeArray = context.getResources().getStringArray(R.array.modeArray);
        if(difficultyMode.equals(modeArray[0])){
            return Mode.EASY;
        } else if(difficultyMode.equals(modeArray[1])){
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
                if(numCustomImages >= 7){
                    saveImagePackId(imgButtonCustom);

                    if(numCustomImages < getNumCardsTotal(getNumImages(this))){
                        Toast.makeText(OptionsActivity.this, R.string.toast_options, Toast.LENGTH_LONG).show();

                        int temp = 3;
                        final String[] numImagesArray = getResources().getStringArray(R.array.numImagesArray);
                        for(int i = 0; i < numImagesArray.length; i++){
                            if(getNumCardsTotal(Integer.parseInt(numImagesArray[i])) <= numCustomImages){
                                temp = Integer.parseInt(numImagesArray[i]);
                            }
                        }
                        saveNumImages(temp);
                        setNumImagesSpinner();
                        setNumCardsSpinner();
                    }

                    if(getModeId(this) == buttonWordsImages){
                        Toast.makeText(OptionsActivity.this, R.string.toast_options_mode, Toast.LENGTH_LONG).show();
                        saveModeId(buttonImages);
                        setupModeButton(buttonImages);
                        setupModeButton(buttonWordsImages);
                    }
                } else{
                    Toast.makeText(OptionsActivity.this, R.string.toast_no_images, Toast.LENGTH_LONG).show();
                    if(getImagePackId(this) == imgButtonCustom){
                        saveImagePackId(imgButtonFruits);
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

    private void setupBackButton() {
        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}