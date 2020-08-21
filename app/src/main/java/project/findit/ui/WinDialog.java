package project.findit.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Locale;

import project.findit.model.GameConfigs;
import project.findit.model.Score;
import project.findit.model.ScoresManager;
import project.findit.R;

/**
 * WIN FRAGMENT
 * Displays win screen, OK button, EXPORT IMAGES button, and allows user to
 *  input a nickname for saving the score
 */
public class WinDialog extends AppCompatDialogFragment {

    private View view;
    private int time;
    private GameConfigs gameConfigs;
    private ScoresManager scoresManager;

    public WinDialog(int time){
        this.time = time;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_win, null);
        gameConfigs = GameConfigs.getInstance();
        scoresManager = gameConfigs.getCurrentScoreManager();

        if(time < scoresManager.getScore(0).getTimeBySeconds()){
            TextView txtHighScore = view.findViewById(R.id.text_new_high_score);
            txtHighScore.setVisibility(View.VISIBLE);
        }

        setupButton();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void setupButton() {
        Button buttonOk = view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEntry = view.findViewById(R.id.edit_text_nickname);
                String userName = userNameEntry.getText().toString();
                if(userName.isEmpty()){
                    userName = getString(R.string.no_answer);
                }

                Calendar c = Calendar.getInstance();
                String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                String day = c.get(Calendar.DAY_OF_MONTH) + "";
                String year = c.get(Calendar.YEAR) + "";
                String userDate = month + " " + day + ", " + year;

                scoresManager.addScore(new Score(time, userName, userDate));
                gameConfigs.getCurrentScoreManager().setScoreArray(scoresManager.getScoreArray());
                MainActivity.saveGameConfigs(getActivity(), gameConfigs);

                getActivity().finish();
            }
        });

        Button buttonExport = view.findViewById(R.id.button_export);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.toast_export, Toast.LENGTH_LONG).show();
            }
        });
    }
}
