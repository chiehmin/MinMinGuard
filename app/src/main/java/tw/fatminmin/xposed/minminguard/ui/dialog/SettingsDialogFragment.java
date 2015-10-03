package tw.fatminmin.xposed.minminguard.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tw.fatminmin.xposed.minminguard.R;

/**
 * Created by fatminmin on 2015/10/3.
 */
public class SettingsDialogFragment extends DialogFragment{

    private Button btnOk;

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.action_settings);

        View v = inflater.inflate(R.layout.dialog_settings, container, false);

        btnOk = (Button) v.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
