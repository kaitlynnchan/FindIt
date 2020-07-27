package cmpt276.project.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Locale;

import cmpt276.project.R;
import cmpt276.project.model.CardDeck;
import cmpt276.project.model.GameConfigs;
import cmpt276.project.model.Score;
import cmpt276.project.model.ScoresManager;

/**
 * WIN FRAGMENT
 * Displays win screen, OK button, and allows user to
 *  input a nickname when appropriate
 */
public class WinFragment extends AppCompatDialogFragment {

    private View view;
    private int time;
    private int index;
    private CardDeck cardDeck;
    private ScoresManager scoresManager;
    private GameConfigs gameConfigs;

    public WinFragment(int time){
        this.time = time;
    }

    // Used help from Brian's youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.windialog_layout, null);
//        scoresManager = ScoresManager.getInstance();
        cardDeck = CardDeck.getInstance();
        gameConfigs = GameConfigs.getInstance();
        index = gameConfigs.getCardDeckIndex(cardDeck);
        scoresManager = gameConfigs.getScoreManager(index);

        if(time < scoresManager.getScore(0).getTimeBySeconds()){
            TextView txtHighScore = view.findViewById(R.id.textNewHighScore);
            txtHighScore.setVisibility(View.VISIBLE);
        }

        setupButton();

        // Took help from Brians youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
        return new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setView(view)
                .create();
    }

    private void setupButton() {
        Button btnOk = view.findViewById(R.id.buttonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEntry = view.findViewById(R.id.editTextNickname);
                String userName = userNameEntry.getText().toString();
                if(userName.isEmpty()){
                    userName = getString(R.string.no_answer);
                }

                Calendar c = Calendar.getInstance();
                String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                String day = c.get(Calendar.DAY_OF_MONTH) + "";
                String year = c.get(Calendar.YEAR) + "";
                String userDate = month + " " + day + ", " + year;

                gameConfigs.getScoreManager(index).print();
                scoresManager.addScore(new Score(time, userName, userDate));
                gameConfigs.getScoreManager(index).setScoreArray(scoresManager.getScoreArray());
                gameConfigs.getScoreManager(index).print();
                MainActivity.saveGameConfigs(getActivity(), gameConfigs);
//                HighScoreActivity.saveScores(getActivity(), gameConfigs.getScoreManager(index));

                getActivity().finish();
            }
        });
    }
}
