package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;

public class CryptoCoinTransactionReceiptActivity extends AppCompatActivity {

    @BindView(R.id.tvOtherName)
    TextView tvOtherName;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvPaymentAmount)
    TextView tvPaymentAmount;

    private long cryptoCoinTransactionId;
    private LiveData<CryptoCoinTransaction> cryptoCoinTransactionLiveData;
    private CrystalDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_receipt);

        ButterKnife.bind(this);


        this.cryptoCoinTransactionId  = getIntent().getLongExtra("CRYPTO_COIN_TRANSACTION_ID", -1);

        if (this.cryptoCoinTransactionId != -1) {
            db = CrystalDatabase.getAppDatabase(this);
            this.cryptoCoinTransactionLiveData = db.transactionDao().getById(this.cryptoCoinTransactionId);

            this.cryptoCoinTransactionLiveData.observe(this, new Observer<CryptoCoinTransaction>() {
                @Override
                public void onChanged(@Nullable CryptoCoinTransaction cryptoCoinTransaction) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
                    CryptoCurrency cryptoCurrency = CrystalDatabase.getAppDatabase(getApplicationContext()).cryptoCurrencyDao().getById(cryptoCoinTransaction.getIdCurrency());

                    String userAccount = (cryptoCoinTransaction.getInput()?cryptoCoinTransaction.getTo():cryptoCoinTransaction.getFrom());
                    String otherAccount = (cryptoCoinTransaction.getInput()?cryptoCoinTransaction.getFrom():cryptoCoinTransaction.getTo());
                    String transactionDateString = dateFormat.format(cryptoCoinTransaction.getDate());
                    String timezoneString = dateFormat.getTimeZone().getDisplayName();
                    String amountString = String.format("%.2f",cryptoCoinTransaction.getAmount()/(Math.pow(10,cryptoCurrency.getPrecision())));

                    tvUserName.setText(userAccount);
                    tvOtherName.setText(otherAccount);
                    tvTime.setText(transactionDateString+" "+timezoneString);
                    tvPaymentAmount.setText(amountString+" "+cryptoCurrency.getName());
                }
            });
        } else {
            this.finish();
        }
    }
}
