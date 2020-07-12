package cmpt276.project.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cmpt276.project.R;
import cmpt276.project.model.ScoreRecording;
import cmpt276.project.model.ScoreRecordingManager;

public class WinFragment extends AppCompatDialogFragment {

    private View view;
    private long time;
    private ScoreRecordingManager manager;

    public WinFragment(long time){
        this.time = time;
    }

    // WinDialog method, Used help from Brian's youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.windialog_layout, null);
        manager = ScoreRecordingManager.getInstance();
        if(time < manager.getLastScore().getTimeBySeconds()){
            setupNewHighScore();
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
                if(userNameEntry.getVisibility() == View.VISIBLE){
                    String userName = userNameEntry.getText().toString();

                    if(userName.isEmpty()){
                        userName = "N/A";
                    }

                    Calendar c = Calendar.getInstance();
                    String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                    String day = c.get(Calendar.DAY_OF_MONTH) + "";
                    String year = c.get(Calendar.YEAR) + "";
                    String userDate = month + " " + day + ", " + year;

                    manager.addNewScore(new ScoreRecording((int)time, userName, userDate));
                    manager.print();
                    HighScoreActivity.saveHighScores(getActivity(), manager);
                }
                getActivity().finish();
            }
        });
    }

    private void setupNewHighScore() {
        TextView txtName = view.findViewById(R.id.textNickname);
        txtName.setVisibility(View.VISIBLE);
        EditText userName = view.findViewById(R.id.editTextNickname);
        userName.setVisibility(View.VISIBLE);

    }
}
