package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountListViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.CryptoCurrencyAdapter;
import cy.agorise.crystalwallet.views.CryptoNetAccountAdapter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SendTransactionFragment extends DialogFragment implements UIValidatorListener, ZXingScannerView.ResultHandler {

    SendTransactionValidator sendTransactionValidator;

    @BindView(R.id.spFrom)
    Spinner spFrom;
    @BindView(R.id.tvFromError)
    TextView tvFromError;
    @BindView(R.id.etTo)
    EditText etTo;
    @BindView(R.id.tvToError)
    TextView tvToError;
    @BindView(R.id.spAsset)
    Spinner spAsset;
    @BindView(R.id.tvAssetError)
    TextView tvAssetError;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAmountError)
    TextView tvAmountError;
    @BindView (R.id.etMemo)
    EditText etMemo;
    @BindView(R.id.tvMemoError)
    TextView tvMemoError;
    @BindView(R.id.btnSend)
    FloatingActionButton btnSend;
    @BindView(R.id.btnCancel)
    TextView btnCancel;

    Button btnScanQrCode;

    private long cryptoNetAccountId;
    private CryptoNetAccount cryptoNetAccount;
    private GrapheneAccount grapheneAccount;
    private CrystalDatabase db;
    private FloatingActionButton fabSend;
    private AlertDialog.Builder builder;

    public static SendTransactionFragment newInstance(long cryptoNetAccountId) {
        SendTransactionFragment f = new SendTransactionFragment();

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
        fabSend = getActivity().findViewById(R.id.fabSend);
        fabSend.hide();

        //AlertDialog.Builder
        builder = new AlertDialog.Builder(getActivity(), R.style.SendTransactionTheme);
        //builder.setTitle("Send");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.send_transaction, null);
        ButterKnife.bind(this, view);

        this.cryptoNetAccountId  = getArguments().getLong("CRYPTO_NET_ACCOUNT_ID",-1);

        if (this.cryptoNetAccountId != -1) {
            db = CrystalDatabase.getAppDatabase(this.getContext());
            this.cryptoNetAccount = db.cryptoNetAccountDao().getById(this.cryptoNetAccountId);

            /*
            * this is only for graphene accounts.
            *
            **/
            this.grapheneAccount = new GrapheneAccount(this.cryptoNetAccount);
            this.grapheneAccount.loadInfo(db.grapheneAccountInfoDao().getByAccountId(this.cryptoNetAccountId));

            final LiveData<List<CryptoCoinBalance>> balancesList = db.cryptoCoinBalanceDao().getBalancesFromAccount(cryptoNetAccountId);
            balancesList.observe(this, new Observer<List<CryptoCoinBalance>>() {
                @Override
                public void onChanged(@Nullable List<CryptoCoinBalance> cryptoCoinBalances) {
                    ArrayList<Long> assetIds = new ArrayList<Long>();
                    for (CryptoCoinBalance nextBalance : balancesList.getValue()) {
                        assetIds.add(nextBalance.getCryptoCurrencyId());
                    }
                    List<CryptoCurrency> cryptoCurrencyList = db.cryptoCurrencyDao().getByIds(assetIds);

                    CryptoCurrencyAdapter assetAdapter = new CryptoCurrencyAdapter(getContext(), android.R.layout.simple_spinner_item, cryptoCurrencyList);
                    spAsset.setAdapter(assetAdapter);
                }
            });
            // TODO SendTransactionValidator to accept spFrom
            sendTransactionValidator = new SendTransactionValidator(this.getContext(), this.cryptoNetAccount, spFrom, etTo, spAsset, etAmount, etMemo);
            sendTransactionValidator.setListener(this);

            CryptoNetAccountListViewModel cryptoNetAccountListViewModel = ViewModelProviders.of(this).get(CryptoNetAccountListViewModel.class);
            List<CryptoNetAccount> cryptoNetAccounts = cryptoNetAccountListViewModel.getCryptoNetAccountList();
            CryptoNetAccountAdapter fromSpinnerAdapter = new CryptoNetAccountAdapter(this.getContext(), android.R.layout.simple_spinner_item, cryptoNetAccounts);
            spFrom.setAdapter(fromSpinnerAdapter);
            spFrom.setSelection(0);

            // etFrom.setText(this.grapheneAccount.getName());
        }

        return builder.setView(view).create();
    }

    @Override
    public void onResume() {
        super.onResume();
        builder.setNeutralButton("Scan QR Code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                beginScanQrCode();
            }
        });

        // Force dialog fragment to use the full width of the screen
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //        btnScanQrCode = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                fabSend.show();
            }
        }, 400);
    }

    @OnItemSelected(R.id.spFrom)
    public void afterFromSelected(Spinner spinner, int position) {
        this.sendTransactionValidator.validate();
    }

    @OnTextChanged(value = R.id.etTo,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterToChanged(Editable editable) {
        this.sendTransactionValidator.validate();
    }

    @OnItemSelected(R.id.spAsset)
    public void afterAssetSelected(Spinner spinner, int position) {
        this.sendTransactionValidator.validate();
    }

    @OnTextChanged(value = R.id.etAmount,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAmountChanged(Editable editable) {
        this.sendTransactionValidator.validate();
    }


    @OnTextChanged(value = R.id.etMemo,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterMemoChanged(Editable editable) {
        this.sendTransactionValidator.validate();
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.dismiss();
    }

    @OnClick(R.id.btnSend)
    public void sendTransaction(){
        if (this.sendTransactionValidator.isValid()) {
            //TODO convert the amount to long type using the precision of the currency
            final ValidateBitsharesSendRequest sendRequest = new ValidateBitsharesSendRequest(
                this.getContext(),
                this.grapheneAccount,
                this.etTo.getText().toString(),
                Long.parseLong(this.etAmount.getText().toString()),
                ((CryptoCurrency)spAsset.getSelectedItem()).getName(),
                etMemo.getText().toString()
            );

            sendRequest.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {
                    if (sendRequest.isSend()){
                        try {
                            this.finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            });
            CryptoNetInfoRequests.getInstance().addRequest(sendRequest);
        }
    }

    public void beginScanQrCode(){
        ZXingScannerView mScannerView = new ZXingScannerView(getContext());
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final SendTransactionFragment fragment = this;

        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == spFrom) {
                    tvFromError.setText("");
                } else if (field.getView() == etTo) {
                    tvToError.setText("");
                } else if (field.getView() == etAmount) {
                    tvAmountError.setText("");
                } else if (field.getView() == spAsset) {
                    tvAssetError.setText("");
                } else if (field.getView() == etMemo) {
                    tvMemoError.setText("");
                }

                if (btnSend != null) {
                    if (sendTransactionValidator.isValid()) {
                        btnSend.setEnabled(true);
                    } else {
                        btnSend.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void onValidationFailed(final ValidationField field) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == spFrom) {
                    tvFromError.setText(field.getMessage());
                } else if (field.getView() == etTo) {
                    tvToError.setText(field.getMessage());
                } else if (field.getView() == spAsset) {
                    tvAssetError.setText(field.getMessage());
                } else if (field.getView() == etAmount) {
                    tvAmountError.setText(field.getMessage());
                } else if (field.getView() == etMemo) {
                    tvMemoError.setText(field.getMessage());
                }
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        Log.i("SendFragment",result.getText());
    }
}
