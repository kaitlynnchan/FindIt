package cmpt276.project.ui;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Arrays;

import cmpt276.project.R;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package
 */
public class OptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_OPTIONS = "shared preferences for image pack";
    public static final String EDITOR_IMAGE_PACK_ID = "id for image pack";
    private static final String EDITOR_NUM_IMAGES = "number of images";
    private static final String EDITOR_PILE_SIZE = "pile size";
    private int imgButtonFruits;
    private int imgButtonVegs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgButtonFruits = R.id.imgButtonFruits;
        imgButtonVegs = R.id.imgButtonVegs;

        setupImageButton(imgButtonFruits);
        setupImageButton(imgButtonVegs);
        setupBackButton();

        imageSpinner();
        cardSpinner();
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
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_PACK_ID, imagePack);
        editor.apply();
    }

    public static int[] getImagePackArray(Context context){
        int imageButtonId = OptionActivity.getImagePackId(context);
        if(imageButtonId == R.id.imgButtonVegs){
            return new int[]{R.drawable.broccoli, R.drawable.carrot, R.drawable.eggplant,
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
            return new int[]{R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                    R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                    R.drawable.watermelon, R.drawable.avocado, R.drawable.banana, R.drawable.blackberry,
                    R.drawable.blueberry, R.drawable.cherry, R.drawable.coconut,
                    R.drawable.cranberry, R.drawable.dragon_fruit, R.drawable.durian, R.drawable.fig,
                    R.drawable.grapefruit, R.drawable.grapes, R.drawable.kiwi,
                    R.drawable.melon, R.drawable.papaya, R.drawable.peach, R.drawable.pear,
                    R.drawable.pineapple, R.drawable.plum, R.drawable.pomegranate,
                    R.drawable.raspberry, R.drawable.squash, R.drawable.starfruit, R.drawable.strawberry};
        }
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_PACK_ID, R.id.imgButtonFruits);
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

    private void imageSpinner() {
        Spinner spinner = findViewById(R.id.imageNumSpinner);
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
        Spinner spinner = findViewById(R.id.cardNumSpinner);
        String[] numCardsArray = getResources().getStringArray(R.array.numCardsArray);
        String[] textArray;
        if(getNumCardsTotal(getBaseContext()) == 7){
            textArray = Arrays.copyOfRange(numCardsArray, 0, 2);
        } else if(getNumCardsTotal(getBaseContext()) == 13){
            textArray = Arrays.copyOfRange(numCardsArray, 0, 3);
        } else{
            textArray = Arrays.copyOfRange(numCardsArray, 0, numCardsArray.length);
        }

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, textArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                String[] numCardsArray = parent.getResources().getStringArray(R.array.numCardsArray);
                if(text.equals(numCardsArray[0])) {
                    savePileSize(getNumCardsTotal(getBaseContext()));
                } else {
                    savePileSize(Integer.parseInt(text));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < textArray.length; i++){
            if(i == 0){
                if(getPileSize(this) == getNumCardsTotal(this)){
                    spinner.setSelection(i);
                }
            } else if(textArray[i].equals("" + getPileSize(this))){
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

    private void savePileSize(int pileSize){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_PILE_SIZE, pileSize);
        editor.apply();
    }

    public static int getPileSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_PILE_SIZE, 5);
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
}
