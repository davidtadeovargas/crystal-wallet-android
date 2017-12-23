package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;

public class AccountsFragment extends DialogFragment {

    @BindView(R.id.tvClose)
    TextView tvClose;

    public AccountsFragment() {
        // Required empty public constructor
    }

    public static AccountsFragment newInstance(long cryptoNetAccountId) {
        AccountsFragment f = new AccountsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("CRYPTO_NET_ACCOUNT_ID", cryptoNetAccountId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AccountsTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_accounts, null);
        ButterKnife.bind(this, view);

        builder.setView(view);
        Dialog dialog = builder.create();

        return dialog;

    }

    @Override
    public void onResume() {
        super.onResume();

        // Force dialog fragment to use the full width of the screen
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.tvClose)
    public void cancel(){
        this.dismiss();
    }
}
