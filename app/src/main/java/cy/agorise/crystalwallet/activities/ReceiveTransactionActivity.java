package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
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
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.CryptoCurrencyAdapter;

public class ReceiveTransactionActivity extends AppCompatActivity {

    @BindView(R.id.tvReceiveAddress)
    TextView tvReceiveAddress;

    private long cryptoNetAccountId;
    private CryptoNetAccount cryptoNetAccount;
    private GrapheneAccount grapheneAccount;
    private CrystalDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_transaction);

        ButterKnife.bind(this);

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
                    for (CryptoCoinBalance nextBalance : balancesList.getValue()) {
                        assetIds.add(nextBalance.getCryptoCurrencyId());
                    }
                    List<CryptoCurrency> cryptoCurrencyList = db.cryptoCurrencyDao().getByIds(assetIds);

                    CryptoCurrencyAdapter assetAdapter = new CryptoCurrencyAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, cryptoCurrencyList);
                    //spAsset.setAdapter(assetAdapter);
                }
            });

            //sendTransactionValidator = new SendTransactionValidator(this.getApplicationContext(), this.cryptoNetAccount, etFrom, etTo, spAsset, etAmount, etMemo);
            //sendTransactionValidator.setListener(this);
        } else {
            this.finish();
        }
    }
}
