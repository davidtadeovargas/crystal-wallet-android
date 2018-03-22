package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.TimeZone;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.activities.CryptoCoinTransactionReceiptActivity;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.CryptoCurrencyViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountViewModel;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by Henry Varona on 17/9/2017.
 *
 * Represents a transaction view in the crypto net account transaction list
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    /*
     * The view holding the transaction "from"
     */
    private TextView tvFrom;
    /*
     * The view holding the transaction "to"
     */
    private TextView tvTo;
    /*
     * The view holding the transaction amount
     */
    private TextView tvAmount;
    private TextView tvEquivalent;
    private TextView tvTransactionDate;
    private TextView tvTransactionHour;
    private View rootView;

    private Fragment fragment;

    private long cryptoCoinTransactionId;

    public TransactionViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        //TODO: use ButterKnife to load this
        this.cryptoCoinTransactionId = -1;

        rootView = itemView.findViewById(R.id.rlTransactionItem);
        tvFrom = (TextView) itemView.findViewById(R.id.fromText);
        tvTo = (TextView) itemView.findViewById(R.id.toText);
        tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        tvEquivalent = (TextView) itemView.findViewById(R.id.tvEquivalent);
        tvTransactionDate = (TextView) itemView.findViewById(R.id.tvTransactionDate);
        tvTransactionHour = (TextView) itemView.findViewById(R.id.tvTransactionHour);
        this.fragment = fragment;

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ereceiptOfThisTransaction();
            }
        });
    }

    /*
     * dispatch the user to the receipt activity using this transaction
     */
    public void ereceiptOfThisTransaction(){
        //if the transaction was loaded
        if (this.cryptoCoinTransactionId >= 0) {
            Context context = fragment.getContext();
            Intent startActivity = new Intent();
            startActivity.setClass(context, CryptoCoinTransactionReceiptActivity.class);
            startActivity.setAction(CryptoCoinTransactionReceiptActivity.class.getName());
            //Pass the transaction id as an extra parameter to the receipt activity
            startActivity.putExtra("CRYPTO_COIN_TRANSACTION_ID", this.cryptoCoinTransactionId);
            startActivity.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(startActivity);
        }
    }

    /*
     * Clears all info in this element view
     */
    public void clear(){
        tvFrom.setText("loading...");
        tvTo.setText("");
        tvAmount.setText("");
        tvEquivalent.setText("");
        tvTransactionDate.setText("");
        tvTransactionHour.setText("");
    }

    /*
     * Binds a transaction object with this element view
     */
    public void bindTo(final CryptoCoinTransaction transaction/*, final OnTransactionClickListener listener*/) {
        if (transaction == null){
            clear();
        } else {
            this.cryptoCoinTransactionId = transaction.getId();
            CryptoCurrencyViewModel cryptoCurrencyViewModel = ViewModelProviders.of(this.fragment).get(CryptoCurrencyViewModel.class);
            CryptoCurrency cryptoCurrency = cryptoCurrencyViewModel.getCryptoCurrencyById(transaction.getIdCurrency());
            CryptoNetAccountViewModel cryptoNetAccountViewModel = ViewModelProviders.of(this.fragment).get(CryptoNetAccountViewModel.class);
            cryptoNetAccountViewModel.loadCryptoNetAccount(transaction.getAccountId());

            String amountString = String.format("%.2f",transaction.getAmount()/Math.pow(10,cryptoCurrency.getPrecision()));

            GeneralSettingListViewModel generalSettingListViewModel = ViewModelProviders.of(this.fragment).get(GeneralSettingListViewModel.class);
            GeneralSetting timeZoneSetting = generalSettingListViewModel.getGeneralSettingByName(GeneralSetting.SETTING_NAME_TIME_ZONE);

            TimeZone userTimeZone;
            if (timeZoneSetting != null){
                userTimeZone = TimeZone.getTimeZone(timeZoneSetting.getValue());
            } else {
                userTimeZone = TimeZone.getTimeZone("cet");
            }

            DateFormat dateFormat = new SimpleDateFormat("dd MMM");
            dateFormat.setTimeZone(userTimeZone);
            DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            hourFormat.setTimeZone(userTimeZone);

            tvTransactionDate.setText(dateFormat.format(transaction.getDate()));
            tvTransactionHour.setText(hourFormat.format(transaction.getDate()));

            tvFrom.setText(transaction.getFrom());
            tvTo.setText(transaction.getTo());

            LiveData<CryptoNetAccount> cryptoNetAccountLiveData = cryptoNetAccountViewModel.getCryptoNetAccount();

            cryptoNetAccountLiveData.observe(this.fragment, new Observer<CryptoNetAccount>() {
                @Override
                public void onChanged(@Nullable CryptoNetAccount cryptoNetAccount) {
                    if (transaction.getInput()){
                        tvTo.setText(cryptoNetAccount.getName());
                    } else {
                        tvFrom.setText(cryptoNetAccount.getName());
                    }
                }
            });

            String finalAmountText = "";
            if (transaction.getInput()) {
                tvAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
                finalAmountText = "+ "+amountString
                        + " "
                        + cryptoCurrency.getName();
            } else {
                tvAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                finalAmountText = amountString
                        + " "
                        + cryptoCurrency.getName();
            }
            tvAmount.setText(finalAmountText);
            //This will load the transaction receipt when the user clicks this view
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClick(user);
                }
            });*/
        }
    }
}
