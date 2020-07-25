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
import android.widget.Toast;

import java.util.Arrays;

import cmpt276.project.R;

/**
 * OPTIONS SCREEN
 * Allows users to select an image package
 */
public class OptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String SHARED_PREFS_IMAGE_PACK = "shared preferences for image pack";
    public static final String EDITOR_IMAGE_PACK_ID = "id for image pack";
    private static final String SHARED_PREFS_IMAGE_NUM = "shared preferences for image number";
    private static final String EDITOR_IMAGE_NUM = "id for image number";
    private static final String SHARED_PREFS_PILE_SIZE = "shared preferences for pile size";
    private static final String EDITOR_PILE_SIZE = "id for pile size";
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

        imageSpinner(R.id.imageNumSpinner , R.array.imageNumArray);
        cardSpinner(R.id.cardNumSpinner , R.array.cardNumArray);

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

    public static int[] getImagePackArray(Context context){
        int imageButtonId = OptionActivity.getImagePackId(context);
        if(imageButtonId == R.id.imgButtonVegs){

                return new int[]{R.drawable.broccoli, R.drawable.carrot, R.drawable.eggplant,
                        R.drawable.lettuce, R.drawable.mushroom, R.drawable.onion,
                        R.drawable.radish,R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                        R.drawable.mango, R.drawable.orange, R.drawable.pumpkin};


        } else{

                return new int[]{R.drawable.apple, R.drawable.green_apple, R.drawable.lemon,
                        R.drawable.mango, R.drawable.orange, R.drawable.pumpkin,
                        R.drawable.watermelon, R.drawable.carrot, R.drawable.eggplant,
                        R.drawable.lettuce, R.drawable.mushroom, R.drawable.onion,
                        R.drawable.radish};

        }
    }

    private static int getImagePackId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_PACK, MODE_PRIVATE);
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

    private void imageSpinner(int buttonId, int textArray) {
        Spinner spinner = findViewById(buttonId);
        String[] temp = getResources().getStringArray(textArray);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,textArray,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    private void cardSpinner(int buttonId, int textArrayNum) {
        Spinner spinner = findViewById(buttonId);
        String[] textArray = getResources().getStringArray(textArrayNum);
        String[] textArray1;
        if(getCardNum(getBaseContext()) == 7){
            textArray1 = Arrays.copyOfRange(textArray, 0, 2);
        }
        else if(getCardNum(getBaseContext()) == 13){
            textArray1 = Arrays.copyOfRange(textArray, 0, 3);
        }
        else{
            textArray1 = Arrays.copyOfRange(textArray, 0, 5);
        }

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this,android.R.layout.simple_spinner_item,textArray1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void saveNumImages(int num){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_IMAGE_NUM, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_IMAGE_NUM, num);
        editor.apply();
    }

    public static int getNumImages(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_IMAGE_NUM, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_IMAGE_NUM, 4);
    }

    private void savePileSize(int num){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_PILE_SIZE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_PILE_SIZE, num);
        editor.apply();
    }

    public static int getPileSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_PILE_SIZE, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_PILE_SIZE, 4);

    }

    public static int getCardNum(Context context) {
        int numImages = OptionActivity.getNumImages(context);
        int cardNum;
        if(numImages == 3){
            cardNum = 7;
        }
        else if(numImages == 4){
            cardNum = 13;
        }
        else{
            cardNum = 31;
        }
        return cardNum;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        // Create function to send the data to the required classes.

        String[] temp = parent.getResources().getStringArray(R.array.cardNumArray);
        if(text.equals(temp[0]) || text.equals(temp[1]) || text.equals(temp[2]) || text.equals(temp[3])){
            if(text.equals(temp[0])) {
                savePileSize(getCardNum(getBaseContext()));
            }
            else {
                savePileSize(Integer.parseInt(text));
            }
        }
        else {
            String[] tempArray = parent.getResources().getStringArray(R.array.imageNumArray);
            if(text.equals(tempArray[0]) || text.equals(tempArray[1]) || text.equals(temp[2])  ) {

                saveNumImages(Integer.parseInt(text));
                cardSpinner(R.id.cardNumSpinner , R.array.cardNumArray);
            }
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    // Pass default values for both spinners.
        saveNumImages(3);
        savePileSize(7);
    }
}
