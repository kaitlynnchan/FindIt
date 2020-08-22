package project.findit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;

import project.findit.model.Defaults;
import project.findit.model.Mode;
import project.findit.R;

/**
 * OPTIONS SCREEN
 * Displays options user can choose
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_OPTIONS = "shared_prefs_options";
    private static final String EDITOR_IMAGE_PACK_ID = "editor_image_pack_id";
    private static final String EDITOR_NUM_IMAGES_PER_CARD = "editor_num_images_per_card";
    private static final String EDITOR_NUM_CARDS = "editor_num_cards";
    private static final String EDITOR_MODE_ID = "editor_mode_id";
    private static final String EDITOR_DIFFICULTY_MODE = "editor_difficulty_mode";
    private static final int REQUEST_CODE_CUSTOM = 42;

    private static int imageButtonFruits = R.id.image_button_fruits;
    private static int imageButtonVegs = R.id.image_button_vegs;
    private static int imageButtonCustom = R.id.image_button_custom;
    private static int buttonImages = R.id.button_images;
    private static int buttonWordsImages = R.id.button_words_images;

    private static int numCustomImages;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        numCustomImages = CustomImagesActivity.getNumCustomImages(this);

        setImageButton(imageButtonFruits);
        setImageButton(imageButtonVegs);
        setImageButton(imageButtonCustom);
        setModeButton(buttonImages);
        setModeButton(buttonWordsImages);

        setNumImagesPerCardSpinner();
        setNumCardsSpinner();
        setDifficultyModeSpinner();

        setupToggleButton();
        setupBackButton();
    }

    private void setImageButton(final int imageId) {
        ImageButton button = findViewById(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageId == imageButtonCustom){
                    Intent intent = CustomImagesActivity.makeLaunchIntent(OptionsActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_CUSTOM);
                    if(getImagePackId(OptionsActivity.this) == imageButtonCustom){
                        saveImagePackId(Defaults.DEFAULT_IMAGE_PACK);
                    }
                } else{
                    saveImagePackId(imageId);

                    setImageButton(imageButtonFruits);
                    setImageButton(imageButtonVegs);
                    setImageButton(imageButtonCustom);
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

    private void setModeButton(final int modeBtn) {
        Button button = findViewById(modeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modeBtn == buttonWordsImages
                        && getImagePackId(OptionsActivity.this) == imageButtonCustom){
                    Toast.makeText(
                            OptionsActivity.this,
                            R.string.toast_mode_not_supported,
                            Toast.LENGTH_SHORT)
                            .show();
                } else{
                    saveModeId(modeBtn);

                    setModeButton(buttonImages);
                    setModeButton(buttonWordsImages);
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

    private void setNumImagesPerCardSpinner() {
        Spinner spinner = findViewById(R.id.spinner_num_images_per_card);
        String[] numImagesPerCardArray = getResources()
                .getStringArray(R.array.num_images_per_card_array);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item, numImagesPerCardArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                int numImagesPerCard = Integer.parseInt(value);
                if(numCustomImages < getTotalNumCardsOrImages(numImagesPerCard)
                        && getImagePackId(OptionsActivity.this) == imageButtonCustom){
                    Toast.makeText(
                            OptionsActivity.this,
                            R.string.toast_options_not_supported,
                            Toast.LENGTH_SHORT)
                            .show();
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

    private void setNumCardsSpinner() {
        Spinner spinner = findViewById(R.id.spinner_num_cards);
        String[] numCardsArray = getResources().getStringArray(R.array.num_cards_array);
        numCardsArray = setRangeArray(numCardsArray);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item, numCardsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.equals(getString(R.string.all))) {
                    saveNumCards(
                            getTotalNumCardsOrImages(getNumImagesPerCard(OptionsActivity.this)));
                } else {
                    saveNumCards(Integer.parseInt(value));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < numCardsArray.length; i++){
            if(numCardsArray[i].equals(getString(R.string.all))){
                if(getNumCards(this)
                        == getTotalNumCardsOrImages(getNumImagesPerCard(this))){
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
        return Arrays.copyOfRange(numCardsArray, 0, numCardsArray.length);
    }

    private static int getTotalNumCardsOrImages(int numImagesPerCard) {
        return (numImagesPerCard * numImagesPerCard) - numImagesPerCard + 1;
    }

    private void setDifficultyModeSpinner() {
        Spinner spinner = findViewById(R.id.spinner_difficulty_mode);
        String[] difficultyModeArray = getResources().getStringArray(R.array.difficulty_mode_array);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
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

    private void setupToggleButton() {
        ToggleButton toggleSound = findViewById(R.id.toggle_sound_effects);
        toggleSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    System.out.println("on");
                    AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_DTMF, AudioManager.ADJUST_UNMUTE, 0);
                } else {
                    System.out.println("off");
                    AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(
                            AudioManager.STREAM_DTMF, AudioManager.ADJUST_MUTE, 0);
                }
            }
        });
    }

    private void saveImagePackId(int imagePack) {
        SharedPreferences sharedPreferences
                = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, Defaults.DEFAULT_IMAGE_PACK);
    }

    private void saveModeId(int mode) {
        SharedPreferences sharedPreferences
                = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_MODE_ID, mode);
        editor.apply();
    }

    private static int getModeId(Context context){
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_MODE_ID, Defaults.DEFAULT_MODE_BUTTON);
    }

    private void saveNumImagesPerCard(int numImagesPerCard){
        SharedPreferences sharedPreferences
                = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_IMAGES_PER_CARD, numImagesPerCard);
        editor.apply();
    }

    public static int getNumImagesPerCard(Context context) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValueStr = context.getString(R.string.default_num_images_per_card);
        int defaultValue = Integer.parseInt(defaultValueStr);
        return sharedPreferences.getInt(EDITOR_NUM_IMAGES_PER_CARD, defaultValue);
    }

    private void saveNumCards(int numCards){
        SharedPreferences sharedPreferences
                = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_CARDS, numCards);
        editor.apply();
    }

    public static int getNumCards(Context context) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValueStr = context.getString(R.string.default_num_cards);
        int defaultValue;
        if(defaultValueStr.equals(context.getString(R.string.all))){
            defaultValue = getTotalNumCardsOrImages(getNumImagesPerCard(context));
        } else{
            defaultValue = Integer.parseInt(defaultValueStr);
        }
        return sharedPreferences.getInt(EDITOR_NUM_CARDS, defaultValue);
    }

    private void saveDifficultyMode(String difficultyMode) {
        SharedPreferences sharedPreferences
                = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EDITOR_DIFFICULTY_MODE, difficultyMode);
        editor.apply();
    }

    private static String getDifficultyModeStr(Context context){
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        String defaultValue = context.getString(R.string.default_difficulty_mode);
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

    public static Object[] getPackArray(Context context){
        int imageButtonID = getImagePackId(context);
        Object[] packArr;
        if(imageButtonID == imageButtonCustom){
            packArr = CustomImagesActivity.getCustomArr(context);
        } else if(imageButtonID == imageButtonVegs){
            packArr =  new Object[]{R.drawable.artichoke, R.drawable.asparagus, R.drawable.broccoli,
                    R.drawable.cabbage, R.drawable.carrot, R.drawable.cauliflower,
                    R.drawable.celery, R.drawable.corn, R.drawable.cucumber, R.drawable.eggplant,
                    R.drawable.garlic, R.drawable.ginger, R.drawable.green_bell_pepper,
                    R.drawable.kale, R.drawable.leek, R.drawable.lettuce, R.drawable.mushroom,
                    R.drawable.okra, R.drawable.onion, R.drawable.parsnip, R.drawable.peas,
                    R.drawable.potato, R.drawable.radish, R.drawable.red_bell_pepper,
                    R.drawable.red_cabbage, R.drawable.red_onion, R.drawable.spinach,
                    R.drawable.turnip, R.drawable.yam, R.drawable.yellow_bell_pepper,
                    R.drawable.zucchini};
        } else{
            packArr = new Object[]{R.drawable.avocado, R.drawable.banana, R.drawable.blackberry,
                    R.drawable.blueberry, R.drawable.cherry, R.drawable.coconut,
                    R.drawable.cranberry, R.drawable.dragon_fruit, R.drawable.durian,
                    R.drawable.fig, R.drawable.grapefruit, R.drawable.grapes,
                    R.drawable.green_apple, R.drawable.kiwi, R.drawable.lemon, R.drawable.mango,
                    R.drawable.melon, R.drawable.orange, R.drawable.papaya, R.drawable.peach,
                    R.drawable.pear, R.drawable.pineapple, R.drawable.plum, R.drawable.pomegranate,
                    R.drawable.pumpkin, R.drawable.raspberry, R.drawable.red_apple,
                    R.drawable.squash, R.drawable.starfruit, R.drawable.strawberry,
                    R.drawable.watermelon};
        }

        if(getModeId(context) == buttonWordsImages){
            for(int i = 0; i < packArr.length; i++){
                String temp = context.getResources().getResourceEntryName((int) packArr[i]);
                packArr[i] += "," + temp.replace("_", " ");
            }
        }
        return packArr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        if(requestCode == REQUEST_CODE_CUSTOM){
            numCustomImages = CustomImagesActivity.getNumCustomImages(this);
            int minNumImagesPerCard = Integer.parseInt(getString(R.string.min_num_images_per_card));
            if(numCustomImages >= getTotalNumCardsOrImages(minNumImagesPerCard)){
                saveImagePackId(imageButtonCustom);

                if(numCustomImages < getTotalNumCardsOrImages(getNumImagesPerCard(this))){
                    Toast.makeText(
                            this, R.string.toast_options_not_supported, Toast.LENGTH_SHORT)
                            .show();

                    int maxNumImagesPerCard = getMaxNumImagesPerCard();
                    saveNumImagesPerCard(maxNumImagesPerCard);
                    setNumImagesPerCardSpinner();
                    setNumCardsSpinner();
                }

                if(getModeId(this) == buttonWordsImages){
                    Toast.makeText(
                            this, R.string.toast_mode_not_supported, Toast.LENGTH_SHORT)
                            .show();
                    saveModeId(Defaults.DEFAULT_MODE_BUTTON);
                    setModeButton(buttonImages);
                    setModeButton(buttonWordsImages);
                }
            } else{
                Toast.makeText(
                        this, R.string.toast_not_enough_images, Toast.LENGTH_SHORT)
                        .show();
                if(getImagePackId(this) == imageButtonCustom){
                    saveImagePackId(Defaults.DEFAULT_IMAGE_PACK);
                }
            }
            setImageButton(imageButtonFruits);
            setImageButton(imageButtonVegs);
            setImageButton(imageButtonCustom);
        }
    }

    private int getMaxNumImagesPerCard() {
        int temp = Integer.parseInt(getString(R.string.default_num_images_per_card));
        String[] numImagesPerCardArray = getResources()
                .getStringArray(R.array.num_images_per_card_array);
        for (String s : numImagesPerCardArray) {
            if (getTotalNumCardsOrImages(Integer.parseInt(s)) <= numCustomImages) {
                temp = Integer.parseInt(s);
            }
        }
        return temp;
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}