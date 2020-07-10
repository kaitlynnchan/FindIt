package cmpt276.project.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import cmpt276.project.R;

public class WinFragment extends AppCompatDialogFragment {

    // WinDialog method, Used help from Brian's youtube videos: https://www.youtube.com/watch?v=y6StJRn-Y-A&feature=youtu.be
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.windialog_layout, null);

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
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
