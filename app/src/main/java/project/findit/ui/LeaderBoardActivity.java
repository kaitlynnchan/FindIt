package project.findit.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import project.findit.model.GameConfigs;
import project.findit.model.Score;
import project.findit.model.ScoresManager;
import project.findit.R;

/**
 * LEADER BOARD SCREEN
 * Displays top number of scores for selected configuration
 * Allows user to reset scores
 */
public class LeaderBoardActivity extends AppCompatActivity {

    private GameConfigs gameConfigs;
    private ScoresManager scoresManager;
    private TextView[] textViewScores;
    private int numMaxScores;
    private int index;
    private int numImagesPerCard;
    private int numCards;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, LeaderBoardActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        gameConfigs = GameConfigs.getInstance();
        index = gameConfigs.getCurrentGameIndex();
        numImagesPerCard = gameConfigs.getCardDeck(index).getNumImagesPerCard();
        numCards = gameConfigs.getCardDeck(index).getNumCards();

        scoresManager = gameConfigs.getScoreManager(index);
        numMaxScores = scoresManager.getNumMaxScores();
        textViewScores = new TextView[numMaxScores];

        setupLeaderBoard();
        setupResetButton();
        setNumImagesPerCardSpinner();
        setNumCardsSpinner();
        setupBackButton();
    }

    private void setupLeaderBoard() {
        LinearLayout layoutScores = findViewById(R.id.linear_scores);
        for(int i = 0; i < numMaxScores; i++){
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            text.setTextColor(Color.parseColor("#000000"));
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            layoutScores.addView(text);
            textViewScores[i] = text;
        }
        setScoresText();
    }

    private void setScoresText() {
        for(int i = 0; i < textViewScores.length; i++){
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

            textViewScores[i].setText(msg);
        }
    }

    private void setupResetButton() {
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    scoresManager.resetScoreArray();
                    setDefaultScores(LeaderBoardActivity.this, scoresManager);
                    gameConfigs.getScoreManager(index).setScoreArray(scoresManager.getScoreArray());
                    MainActivity.saveGameConfigs(LeaderBoardActivity.this, gameConfigs);
                    setScoresText();
                }
            }
        });
    }

    public static void setDefaultScores(Context context, ScoresManager scoresManager){
        scoresManager.addScore(new Score(
                250,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format)));
        scoresManager.addScore(new Score(
                400,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format)));
        scoresManager.addScore( new Score(
                20,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format)));
        scoresManager.addScore(new Score(
                25,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format)));
        scoresManager.addScore(new Score(
                18,
                context.getString(R.string.no_answer),
                context.getString(R.string.date_format)));
    }

    private void setNumImagesPerCardSpinner() {
        Spinner spinner = findViewById(R.id.spinner_num_images_per_card);
        String[] numImagesPerCardArray =
                getResources().getStringArray(R.array.num_images_per_card_array);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, numImagesPerCardArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                numImagesPerCard = Integer.parseInt(value);
                updateScoresManager();
                setNumCardsSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < numImagesPerCardArray.length; i++){
            if(numImagesPerCardArray[i].equals("" + numImagesPerCard)){
                spinner.setSelection(i);
            }
        }
    }

    private void setNumCardsSpinner() {
        Spinner spinner = findViewById(R.id.spinner_num_cards);
        String[] numCardsArray = getResources().getStringArray(R.array.num_cards_array);
        numCardsArray = setRangeArray(numCardsArray);

        ArrayAdapter<CharSequence> adapter =  new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, numCardsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.equals(getString(R.string.all))) {
                    numCards = getTotalNumCards();
                } else {
                    numCards = Integer.parseInt(value);
                }

                updateScoresManager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for(int i = 0; i < numCardsArray.length; i++){
            if(numCardsArray[i].equals(getString(R.string.all))){
                if(numCards == getTotalNumCards()){
                    spinner.setSelection(i);
                }
            } else if(numCardsArray[i].equals("" + numCards)){
                spinner.setSelection(i);
            }
        }
    }

    private String[] setRangeArray(String[] numCardsArray) {
        int totalCards = getTotalNumCards();
        for(int i = 0; i < numCardsArray.length; i++){
            if(!numCardsArray[i].equals(getString(R.string.all))){
                if(Integer.parseInt(numCardsArray[i]) > totalCards){
                    return Arrays.copyOfRange(numCardsArray, 0, i);
                }
            }
        }
        return Arrays.copyOfRange(numCardsArray, 0, numCardsArray.length);
    }

    private int getTotalNumCards() {
        return (numImagesPerCard * numImagesPerCard) - numImagesPerCard + 1;
    }

    private void updateScoresManager() {
        index = gameConfigs.getCardDeckIndex(numImagesPerCard, numCards);
        scoresManager = new ScoresManager();
        if (index == -1) {
            scoresManager.setNumMaxScores(5);
            setDefaultScores(this, scoresManager);
        } else {
            scoresManager.setNumMaxScores(gameConfigs.getScoreManager(index).getNumMaxScores());
            scoresManager.setScoreArray(gameConfigs.getScoreManager(index).getScoreArray());
        }
        setScoresText();
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