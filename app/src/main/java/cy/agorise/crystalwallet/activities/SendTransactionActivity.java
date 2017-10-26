package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountViewModel;
import cy.agorise.crystalwallet.viewmodels.GrapheneAccountInfoViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

public class SendTransactionActivity extends AppCompatActivity implements UIValidatorListener {

    SendTransactionValidator sendTransactionValidator;

    @BindView(R.id.etFrom)
    EditText etFrom;
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
    Button btnSend;
    @BindView(R.id.btnCancel)
    Button btnCancel;

    private long cryptoNetAccountId;
    private CryptoNetAccount cryptoNetAccount;
    private GrapheneAccount grapheneAccount;
    private CrystalDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_transaction);

        ButterKnife.bind(this);

        btnSend.setEnabled(false);

        this.cryptoNetAccountId  = getIntent().getLongExtra("CRYPTO_NET_ACCOUNT_ID", -1);

        if (this.cryptoNetAccountId != -1) {
            db = CrystalDatabase.getAppDatabase(this);
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
                    ArrayList<String> assetLabels = new ArrayList<String>();
                    for (CryptoCoinBalance nextBalance : balancesList.getValue()) {
                        assetIds.add(nextBalance.getCryptoCurrencyId());
                    }
                    List<CryptoCurrency> cryptoCurrencyList = db.cryptoCurrencyDao().getByIds(assetIds);
                    for (CryptoCurrency nextCurrency : cryptoCurrencyList) {
                        assetLabels.add(nextCurrency.getName());
                    }

                    ArrayAdapter<String> assetAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, assetLabels);
                    spAsset.setAdapter(assetAdapter);
                }
            });

            sendTransactionValidator = new SendTransactionValidator(this.getApplicationContext(), this.cryptoNetAccount, etFrom, etTo, spAsset, etAmount, etMemo);
            sendTransactionValidator.setListener(this);
        } else {
            this.finish();
        }
    }

    @OnTextChanged(value = R.id.etFrom,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterFromChanged(Editable editable) {
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
        this.finish();
    }

    @OnClick(R.id.btnSend)
    public void importSend(){
        if (this.sendTransactionValidator.isValid()) {
            //TODO convert the amount to long type using the precision of the currency
            ValidateBitsharesSendRequest sendRequest = new ValidateBitsharesSendRequest(
                this.getApplicationContext(),
                this.grapheneAccount,
                this.etTo.getText().toString(),
                Long.parseLong(this.etAmount.getText().toString()),
                ((CryptoCurrency)spAsset.getSelectedItem()).getName(),
                etMemo.getText().toString()
            );

            this.finish();
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final SendTransactionActivity activity = this;

        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == etFrom) {
                    tvFromError.setText("");
                } else if (field.getView() == etTo){
                    tvToError.setText("");
                } else if (field.getView() == etAmount){
                    tvAmountError.setText("");
                } else if (field.getView() == spAsset){
                    tvAssetError.setText("");
                } else if (field.getView() == etMemo){
                    tvMemoError.setText("");
                }

                if (activity.sendTransactionValidator.isValid()){
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }

            }
        });
    }

    @Override
    public void onValidationFailed(ValidationField field) {
        if (field.getView() == etFrom) {
            tvFromError.setText(field.getMessage());
        } else if (field.getView() == etTo){
            tvToError.setText(field.getMessage());
        } else if (field.getView() == spAsset){
            tvAssetError.setText(field.getMessage());
        } else if (field.getView() == etAmount){
            tvAmountError.setText(field.getMessage());
        } else if (field.getView() == etMemo){
            tvMemoError.setText(field.getMessage());
        }
    }
}
