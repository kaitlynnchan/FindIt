package cmpt276.project.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import cmpt276.project.R;
import cmpt276.project.model.GameConfigs;
import cmpt276.project.model.Score;
import cmpt276.project.model.ScoresManager;

/**
 * HIGH SCORE SCREEN
 * Displays top number of scores for selected configuration
 * Allows user to reset scores
 */
public class HighScoreActivity extends AppCompatActivity {

    public static final String EXTRA_INDEX = "extra for index";

    private GameConfigs gameConfigs;
    private ScoresManager scoresManager;
    private TextView[] scoresTxtView;
    private int numMaxScores;
    private int index;
    private int numImages;
    private int cardDeckSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        index = intent.getIntExtra(EXTRA_INDEX, -1);

        gameConfigs = GameConfigs.getInstance();
        scoresManager = gameConfigs.getScoreManager(index);
        numMaxScores = scoresManager.getNumMaxScores();
        scoresTxtView = new TextView[numMaxScores];

        numImages = gameConfigs.getCardDecks().get(index).getNumImages();
        cardDeckSize = gameConfigs.getCardDecks().get(index).getCardDeckSize();

        setupHighScores();
        setupResetButton();
        imageSpinner();
        cardSpinner();
        setupBackButton();
    }

    private void setupHighScores() {
        LinearLayout layout = findViewById(R.id.linearLayoutScores);
        for(int i = 0; i < numMaxScores; i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setPadding(0, 0, 0, 0);

            layout.addView(text);
            scoresTxtView[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresTxtView.length; i++){
            int time = scoresManager.getScore(i).getTimeBySeconds();
            int minutes = time / 60;
            int seconds = time % 60;

            String strSeconds = seconds + "";
            if(seconds / 10 < 1){
                strSeconds = "0" + seconds;
            }
            String msg = (i + 1) + ".\t\t\t"
                    + minutes + ":" + strSeconds + "\t\t\t"
                    + scoresManager.getScore(i).getName() + "\t\t\t"
                    + scoresManager.getScore(i).getDate();

            scoresTxtView[i].setText(msg);
        }
    }

    private void setupResetButton() {
        Button btReset = findViewById(R.id.buttonReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    scoresManager.resetScoreArray();
                    setDefaultScores(HighScoreActivity.this, scoresManager);
                    gameConfigs.getScoreManager(index).setScoreArray(scoresManager.getScoreArray());
                    MainActivity.saveGameConfigs(HighScoreActivity.this, gameConfigs);
                    setTexts();
                }
            }
        });
    }

    public static void setDefaultScores(Context context, ScoresManager scoresManager){
        scoresManager.addScore(new Score(
                250,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format))
        );
        scoresManager.addScore(new Score(
                400,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format))
        );
        scoresManager.addScore(new Score(
                20,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format))
        );
        scoresManager.addScore(new Score(
                25,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format))
        );
        scoresManager.addScore(new Score(
                18,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format))
        );
    }

    private void imageSpinner() {
        Spinner spinner = findViewById(R.id.imagesSpinnerHighScore);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.numImagesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                numImages = Integer.parseInt(text);
                updateScoresManager();

                setTexts();
                cardSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] numImagesArray = getResources().getStringArray(R.array.numImagesArray);
        for(int i = 0; i < numImagesArray.length; i++){
            if(numImagesArray[i].equals("" + numImages)){
                spinner.setSelection(i);
            }
        }
    }

    private void cardSpinner() {
        Spinner spinner = findViewById(R.id.cardsSpinnerHighScore);
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

                if(text.equals(cardDeckSizeArray[0])) {
                    cardDeckSize = getNumCardsTotal();
                } else {
                    cardDeckSize = Integer.parseInt(text);
                }

                updateScoresManager();
                setTexts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < textArray.length; i++){
            if(i == 0){
                int numCardsTotal = getNumCardsTotal();
                if(cardDeckSize == numCardsTotal){
                    spinner.setSelection(i);
                }
            } else if(textArray[i].equals("" + cardDeckSize)){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setTextArray(String[] cardDeckSizeArray) {
        String[] textArray;
        int numCardsTotal = getNumCardsTotal();

        if(numCardsTotal == 7){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 2);
        } else if(numCardsTotal == 13){
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, 3);
        } else{
            textArray = Arrays.copyOfRange(cardDeckSizeArray, 0, cardDeckSizeArray.length);
        }
        return textArray;
    }

    private int getNumCardsTotal() {
        int numCardsTotal;
        if (numImages == 3) {
            numCardsTotal = 7;
        } else if (numImages == 6) {
            numCardsTotal = 31;
        } else {
            numCardsTotal = 13;
        }
        return numCardsTotal;
    }

    private void updateScoresManager() {
        index = gameConfigs.getCardDeckIndex(numImages, cardDeckSize);
        scoresManager = new ScoresManager();
        if (index == -1) {
            scoresManager.setNumMaxScores(5);
            setDefaultScores(this, scoresManager);
        } else {
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());
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

    public static Intent makeIntent(Context context, int index){
        Intent intent = new Intent(context, HighScoreActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }
}