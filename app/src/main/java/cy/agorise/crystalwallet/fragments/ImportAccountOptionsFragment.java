package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;

/**
 * Created by xd on 1/25/18.
 * Shows a dialog where the user can select how to import his/her existing account
 */

public class ImportAccountOptionsFragment extends DialogFragment {

    @BindView(R.id.btnCancel)
    Button btnClose;

    public ImportAccountOptionsFragment() {
        // Required empty public constructor
    }

    public static ImportAccountOptionsFragment newInstance() {
        ImportAccountOptionsFragment fragment = new ImportAccountOptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_import_account_options, null);
        ButterKnife.bind(this, view);

        return builder.setView(view).create();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Force dialog fragment to use the full width of the screen
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {
        dismiss();
    }
}
