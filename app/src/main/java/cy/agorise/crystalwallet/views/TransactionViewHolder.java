package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
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

    private Fragment fragment;

    public TransactionViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        //TODO: use ButterKnife to load this
        transactionFrom = (TextView) itemView.findViewById(R.id.fromText);
        transactionTo = (TextView) itemView.findViewById(R.id.toText);
        transactionAmount = (TextView) itemView.findViewById(R.id.amountText);
        tvTransactionDate = (TextView) itemView.findViewById(R.id.tvTransactionDate);
        this.fragment = fragment;
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
