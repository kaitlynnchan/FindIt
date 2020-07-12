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

import androidx.appcompat.app.AppCompatDialogFragment;

import cmpt276.project.R;
import cmpt276.project.model.ScoreRecording;
import cmpt276.project.model.ScoreRecordingManager;

public class WinFragment extends AppCompatDialogFragment {

    private long time;
    private ScoreRecordingManager manager;    //to store scores
    private View v;

    public WinFragment(long time){
        this.time = time;
    }

    // WinDialog method, Used help from Brian's youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        v = LayoutInflater.from(getActivity()).inflate(R.layout.windialog_layout, null);
        manager = ScoreRecordingManager.getInstance();
        if(time < manager.getLastScore().getTimeBySeconds()){
            setupNewHighScore();
        }

        setupButton();

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                        break;
                }
            }
        };

        // Took help from Brians youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
        return new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setView(v)
                .create();
    }

    private void setupButton() {
        Button btnOk = v.findViewById(R.id.buttonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText userNameEntry = v.findViewById(R.id.editTextNickname);
//                String userName = userNameEntry.getText().toString();
//
//                EditText userDateEntry = v.findViewById(R.id.editTextDate);
//                String userDate = userDateEntry.getText().toString();
//
//                manager.addNewScore(new ScoreRecording((int)time, userName, userDate));
//                manager.print();
//                HighScoreActivity.saveHighScores(getActivity(), manager);
                getActivity().finish();
            }
        });
    }

    private void setupNewHighScore() {
        TextView txtName = v.findViewById(R.id.textNickname);
        txtName.setVisibility(View.VISIBLE);
        TextView txtDate = v.findViewById(R.id.textDate);
        txtDate.setVisibility(View.VISIBLE);

        EditText userName = v.findViewById(R.id.editTextNickname);
        userName.setVisibility(View.VISIBLE);
        EditText userDate = v.findViewById(R.id.editTextDate);
        userDate.setVisibility(View.VISIBLE);
    }
}
