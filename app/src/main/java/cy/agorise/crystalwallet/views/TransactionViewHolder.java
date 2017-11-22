package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.activities.CryptoCoinTransactionReceiptActivity;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.viewmodels.CryptoCurrencyViewModel;

/**
 * Created by Henry Varona on 17/9/2017.
 *
 * Represents a transaction view in the crypto net account transaction list
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    /*
     * The view holding the transaction "from"
     */
    private TextView transactionFrom;
    /*
     * The view holding the transaction "to"
     */
    private TextView transactionTo;
    /*
     * The view holding the transaction amount
     */
    private TextView transactionAmount;
    private TextView tvTransactionDate;
    private View rootView;

    private Fragment fragment;

    private long cryptoCoinTransactionId;

    public TransactionViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        //TODO: use ButterKnife to load this
        this.cryptoCoinTransactionId = -1;

        rootView = itemView.findViewById(R.id.rlTransactionItem);
        transactionFrom = (TextView) itemView.findViewById(R.id.fromText);
        transactionTo = (TextView) itemView.findViewById(R.id.toText);
        transactionAmount = (TextView) itemView.findViewById(R.id.amountText);
        tvTransactionDate = (TextView) itemView.findViewById(R.id.tvTransactionDate);
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
        transactionFrom.setText("loading...");
        transactionTo.setText("");
        transactionAmount.setText("");
    }

    /*
     * Binds a transaction object with this element view
     */
    public void bindTo(final CryptoCoinTransaction transaction/*, final OnTransactionClickListener listener*/) {
        if (transaction == null){
            transactionFrom.setText("loading...");
            transactionTo.setText("");
            transactionAmount.setText("");
        } else {
            this.cryptoCoinTransactionId = transaction.getId();
            CryptoCurrencyViewModel cryptoCurrencyViewModel = ViewModelProviders.of(this.fragment).get(CryptoCurrencyViewModel.class);
            CryptoCurrency cryptoCurrency = cryptoCurrencyViewModel.getCryptoCurrencyById(transaction.getIdCurrency());
            String amountString = String.format("%.2f",transaction.getAmount()/Math.pow(10,cryptoCurrency.getPrecision()));

            tvTransactionDate.setText(transaction.getDate().toString());
            transactionFrom.setText(transaction.getFrom());
            transactionTo.setText(transaction.getTo());

            if (transaction.getInput()) {
                transactionAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
            } else {
                transactionAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
            }
            transactionAmount.setText(
                    amountString
                    + " "
                    + cryptoCurrency.getName());
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
