package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import cmpt276.project.R;
import cmpt276.project.ui.flickr.PhotoGalleryFragment;
import cmpt276.project.model.Mode;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package and mode
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_OPTIONS = "shared_preferences_options";
    public static final String EDITOR_IMAGE_PACK_ID = "id_image_pack";
    public static final String EDITOR_NUM_IMAGES = "num_images";
    public static final String EDITOR_CARD_DECK_SIZE = "card_deck_size";
    public static final String EDITOR_MODE_ID = "id_mode_button";
    public static final String EDITOR_DIFFICULTY_MODE = "difficulty_mode_string";

    private static int imgButtonFruits = R.id.imgButtonFruits;
    private static int imgButtonVegs = R.id.imgButtonVegs;
    private static int imgButtonCustom = R.id.imgButtonCustom;
    private static int buttonImages = R.id.buttonImages;
    private static int buttonWordsImages = R.id.buttonWordsImages;
    private static int numCustomImages = 0;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, OptionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

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
                    Intent intent = CustomImagesActivity.makeLaunchIntent(OptionActivity.this);
                    startActivity(intent);
                }
                saveImagePackId(imageId);

                setupImageButton(imgButtonFruits);
                setupImageButton(imgButtonVegs);
                setupImageButton(imgButtonCustom);
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
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, OptionActivity.imgButtonFruits);
    }

    // https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
    // Code for bitmap string conversion: https://stackoverflow.com/questions/22532136/android-how-to-create-a-bitmap-from-a-string
    private static Object[] getCustomArr(Context context) {
        final File[] directoryListing = getDirectory(context);

        Object[] objects = new Object[numCustomImages];
        for(int i = 0; i < numCustomImages; i++){
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(directoryListing[i]));
                System.out.println("" + directoryListing[i].getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
            byte[] byteArr = byteArrayOutputStream.toByteArray();
            String imageStr = Base64.encodeToString(byteArr, Base64.DEFAULT);

            objects[i] = imageStr;
        }
        return objects;
    }

    private static File[] getDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(PhotoGalleryFragment.FILE_CUSTOM_DRAWABLE, Context.MODE_PRIVATE);
        File dir = new File(directory.toString());
        File[] directoryListing = dir.listFiles();
        numCustomImages = directoryListing.length;

        return directoryListing;
    }

    private void setupModeButton(final int modeBtn) {
        Button button = findViewById(modeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getImagePackId(OptionActivity.this) == imgButtonCustom && modeBtn == buttonWordsImages){
                    Toast.makeText(OptionActivity.this, R.string.toast_options_mode, Toast.LENGTH_LONG).show();
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
        return sharedPreferences.getInt(EDITOR_MODE_ID, OptionActivity.buttonImages);
    }

    public static Object[] getPackArray(Context context){
        int imageButtonID = OptionActivity.getImagePackId(context);
        Object[] packArr;

        if(imageButtonID == OptionActivity.imgButtonCustom){
            packArr = OptionActivity.getCustomArr(context);
        } else if(imageButtonID == OptionActivity.imgButtonVegs){
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

        if(getModeId(context) == OptionActivity.buttonWordsImages && imageButtonID != OptionActivity.imgButtonCustom){
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
                int imageNum = Integer.parseInt(text);

                getDirectory(OptionActivity.this);
                int totalImages = getNumCardsTotal(imageNum);

                if(numCustomImages < totalImages && getImagePackId(OptionActivity.this) == imgButtonCustom){
                    Toast.makeText(OptionActivity.this, R.string.toast_options, Toast.LENGTH_LONG).show();
                } else{
                    saveNumImages(imageNum);
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
                    saveCardDeckSize(getNumCardsTotal(OptionActivity.getNumImages(getBaseContext())));
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
                if(getCardDeckSize(this) == getNumCardsTotal(OptionActivity.getNumImages(this))){
                    spinner.setSelection(i);
                }
            } else if(textArray[i].equals("" + getCardDeckSize(this))){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setTextArray(String[] cardDeckSizeArray) {
        String[] textArray;
        if(getNumCardsTotal(OptionActivity.getNumImages(this)) == 7){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 2);
        } else if(getNumCardsTotal(OptionActivity.getNumImages(this)) == 13){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 3);
        } else{
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, cardDeckSizeArray.length);
        }
        return textArray;
    }

    public static int getNumCardsTotal(int numImages) {
        int numCardsTotal;
        if(numImages == 3){
            numCardsTotal = 7;
        } else if(numImages == 6){
            numCardsTotal = 31;
        } else{
            numCardsTotal = 13;
        }
        return numCardsTotal;
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
    protected void onResume() {
        if(getModeId(this) == buttonWordsImages && getImagePackId(this) == numCustomImages){
            Toast.makeText(OptionActivity.this, R.string.toast_options_mode, Toast.LENGTH_LONG).show();
        }
        setNumImagesSpinner();
        setNumCardsSpinner();

        super.onResume();
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirectory(OptionActivity.this);
                int totalImages = getNumCardsTotal(OptionActivity.getNumImages(getBaseContext()));

                if(numCustomImages < totalImages && getImagePackId(OptionActivity.this) == imgButtonCustom){
                    Toast.makeText(OptionActivity.this, R.string.toast_options, Toast.LENGTH_LONG).show();
                } else if (getImagePackId(OptionActivity.this) == imgButtonCustom && getModeId(OptionActivity.this) == buttonWordsImages){
                    Toast.makeText(OptionActivity.this, R.string.toast_options_mode, Toast.LENGTH_LONG).show();
                } else{
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getDirectory(OptionActivity.this);
        int totalImages = getNumCardsTotal(OptionActivity.getNumImages(this));

        if(numCustomImages < totalImages && getImagePackId(OptionActivity.this) == imgButtonCustom){
            Toast.makeText(OptionActivity.this, R.string.toast_options, Toast.LENGTH_LONG).show();
        }  else if (getImagePackId(OptionActivity.this) == imgButtonCustom && getModeId(OptionActivity.this) == buttonWordsImages){
            Toast.makeText(OptionActivity.this, R.string.toast_options_mode, Toast.LENGTH_LONG).show();
        } else{
            super.onBackPressed();
        }
    }
}