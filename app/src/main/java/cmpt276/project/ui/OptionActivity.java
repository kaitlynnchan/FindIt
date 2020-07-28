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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Arrays;

import cmpt276.project.R;
import cmpt276.project.model.Mode;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package and mode
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_OPTIONS = "shared preferences for options";
    public static final String EDITOR_IMAGE_PACK_ID = "id for image pack";
    public static final String EDITOR_NUM_IMAGES = "number of images";
    public static final String EDITOR_CARD_DECK_SIZE = "card deck size";
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

        imageSpinner();
        cardSpinner();

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
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    public static int[] getImagePackArray(Context context){
        int imageButtonId = OptionActivity.getImagePackId(context);
        TypedArray temp = context.getResources().obtainTypedArray(R.array.image_pack_fruits);
        if(imageButtonId == R.id.imgButtonVegs){
            temp = context.getResources().obtainTypedArray(R.array.image_pack_vegetables);
        }

        int[] imagePack = new int[temp.length()];
        for(int i = 0; i < temp.length(); i++){
            imagePack[i] = temp.getResourceId(i, -1);
        }
        return imagePack;
    }

    public static String[] getWordArray(Context context){
        int[] imagePack = OptionActivity.getImagePackArray(context);
        String[] wordArray = new String[imagePack.length];
        for (int i = 0; i < imagePack.length; i++) {
            String temp = context.getResources().getResourceEntryName(imagePack[i]);
            wordArray[i] = temp.replace("_", " ");
        }
        return wordArray;
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, R.id.imgButtonFruits);
    }

    private void imageSpinner() {
        Spinner spinner = findViewById(R.id.numImagesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.numImagesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                saveNumImages(Integer.parseInt(text));
                cardSpinner();
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

    private void cardSpinner() {
        Spinner spinner = findViewById(R.id.numCardsSpinner);
        final String[] cardDeckSizeArray = getResources().getStringArray(R.array.cardDeckSizeArray);
        String[] textArray = setTextArray(cardDeckSizeArray);

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, textArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
//                String[] cardDeckSizeArray = parent.getResources().getStringArray(R.array.cardDeckSizeArray);
                if(text.equals(cardDeckSizeArray[0])) {
                    saveCardDeckSize(getNumCardsTotal(getBaseContext()));
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
                if(getCardDeckSize(this) == getNumCardsTotal(this)){
                    spinner.setSelection(i);
                }
            } else if(textArray[i].equals("" + getCardDeckSize(this))){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setTextArray(String[] cardDeckSizeArray) {
        String[] textArray;
        if(getNumCardsTotal(getBaseContext()) == 7){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 2);
        } else if(getNumCardsTotal(getBaseContext()) == 13){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 3);
        } else{
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, cardDeckSizeArray.length);
        }
        return textArray;
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

    public static int getNumCardsTotal(Context context) {
        int numImages = OptionActivity.getNumImages(context);
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

    private void saveModeId(int mode) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_MODE_ID, mode);
        editor.apply();
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
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