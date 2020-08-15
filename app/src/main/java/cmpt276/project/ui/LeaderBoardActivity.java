package cmpt276.project.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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
 * LEADER BOARD SCREEN
 * Displays top number of scores for selected configuration
 * Allows user to reset scores
 */
public class LeaderBoardActivity extends AppCompatActivity {

    public static final String EXTRA_INDEX = "extra for index";

    private GameConfigs gameConfigs;
    private ScoresManager scoresManager;
    private TextView[] scoresTxtView;
    private int numMaxScores;
    private int index;
    private int numImagesOnCard;
    private int cardDeckSize;

    public static Intent makeLaunchIntent(Context context, int index){
        Intent intent = new Intent(context, LeaderBoardActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        Intent intent = getIntent();
        index = intent.getIntExtra(EXTRA_INDEX, -1);

        gameConfigs = GameConfigs.getInstance();
        scoresManager = gameConfigs.getScoreManager(index);
        numMaxScores = scoresManager.getNumMaxScores();
        scoresTxtView = new TextView[numMaxScores];

        numImagesOnCard = gameConfigs.getCardDecks().get(index).getNumImagesOnCard();
        cardDeckSize = gameConfigs.getCardDecks().get(index).getCardDeckSize();

        setupHighScores();
        setupResetButton();
        setNumImagesSpinner();
        setNumCardsSpinner();
        setupBackButton();
    }

    private void setupHighScores() {
        LinearLayout layoutScores = findViewById(R.id.linearLayoutScores);
        for(int i = 0; i < numMaxScores; i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            layoutScores.addView(text);
            scoresTxtView[i] = text;
        }
        setTexts();
    }

    private void setTexts() {
        for(int i = 0; i < scoresTxtView.length; i++){
            int timeInSeconds = scoresManager.getScore(i).getTimeBySeconds();
            int minutes = timeInSeconds / 60;
            int seconds = timeInSeconds % 60;

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
        Button buttonReset = findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    scoresManager.resetScoreArray();
                    setDefaultScores(LeaderBoardActivity.this, scoresManager);
                    gameConfigs.getScoreManager(index).setScoreArray(scoresManager.getScoreArray());
                    MainActivity.saveGameConfigs(LeaderBoardActivity.this, gameConfigs);
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

    private void setNumImagesSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumImagesScores);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.numImagesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                numImagesOnCard = Integer.parseInt(text);
                updateScoresManager();

                setTexts();
                setNumCardsSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] numImagesArray = getResources().getStringArray(R.array.numImagesArray);
        for(int i = 0; i < numImagesArray.length; i++){
            if(numImagesArray[i].equals("" + numImagesOnCard)){
                spinner.setSelection(i);
            }
        }
    }

    private void setNumCardsSpinner() {
        Spinner spinner = findViewById(R.id.spinnerNumCardsScores);
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
        if (numImagesOnCard == 3) {
            numCardsTotal = 7;
        } else if (numImagesOnCard == 6) {
            numCardsTotal = 31;
        } else {
            numCardsTotal = 13;
        }
        return numCardsTotal;
    }

    private void updateScoresManager() {
        index = gameConfigs.getCardDeckIndex(numImagesOnCard, cardDeckSize);
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
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}