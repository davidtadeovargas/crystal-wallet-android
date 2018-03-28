package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.Result;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.ContactAddress;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.ContactViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountListViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.CryptoCurrencyAdapter;
import cy.agorise.crystalwallet.views.CryptoNetAccountAdapter;
import cy.agorise.graphenej.Invoice;
import cy.agorise.graphenej.LineItem;
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
    @BindView(R.id.ivPeople)
    ImageView ivPeople;
    CryptoCurrencyAdapter assetAdapter;

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

                    assetAdapter = new CryptoCurrencyAdapter(getContext(), android.R.layout.simple_spinner_item, cryptoCurrencyList);
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

    @OnClick(R.id.ivPeople)
    public void searchContact(){
        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = this.getActivity().getSupportFragmentManager().findFragmentByTag("ContactSelectionDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Show a contact selection list
        ContactSelectionFragment contactSelectionFragment = ContactSelectionFragment.newInstance(this.cryptoNetAccount.getCryptoNet());
        contactSelectionFragment.setTargetFragment(this, 1);
        contactSelectionFragment.show(ft, "ContactSelectionDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1) {
            if(resultCode == 1) {
                long contactId = data.getLongExtra("CONTACT_ID",-1);
                if (contactId > -1){
                    ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
                    contactViewModel.init(contactId);
                    LiveData<List<ContactAddress>> contactAddressesLiveData = contactViewModel.getContactAddresses();

                    contactAddressesLiveData.observe(this, new Observer<List<ContactAddress>>() {
                        @Override
                        public void onChanged(@Nullable List<ContactAddress> contactAddresses) {
                            if (contactAddresses != null) {
                                for (ContactAddress contactAddress : contactAddresses) {
                                    if (contactAddress.getCryptoNet() == cryptoNetAccount.getCryptoNet()) {
                                        etTo.setText(contactAddress.getAddress());
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.dismiss();
    }

    @OnClick(R.id.btnSend)
    public void sendTransaction(){
        if (this.sendTransactionValidator.isValid()) {
            //TODO convert the amount to long type using the precision of the currency
            Long amountFromEditText = Long.parseLong(this.etAmount.getText().toString());
            Long amount = amountFromEditText*Math.round(Math.pow(10,((CryptoCurrency)spAsset.getSelectedItem()).getPrecision()));

            final ValidateBitsharesSendRequest sendRequest = new ValidateBitsharesSendRequest(
                this.getContext(),
                this.grapheneAccount,
                this.etTo.getText().toString(),
                amount,
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
        Invoice invoice = Invoice.fromQrCode(result.getText());

        etTo.setText(invoice.getTo());

        for (int i=0;i<assetAdapter.getCount();i++) {
            if (assetAdapter.getItem(i).getName().equals(invoice.getCurrency())) {
                spAsset.setSelection(i);
                break;
            }
        }
        etMemo.setText(invoice.getMemo());


        double amount = 0.0;
        for (LineItem nextItem : invoice.getLineItems()) {
            amount += nextItem.getQuantity() * nextItem.getPrice();
        }
        DecimalFormat df = new DecimalFormat("####.####");
        df.setRoundingMode(RoundingMode.CEILING);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        etAmount.setText(df.format(amount));
        Log.i("SendFragment",result.getText());
    }
}
