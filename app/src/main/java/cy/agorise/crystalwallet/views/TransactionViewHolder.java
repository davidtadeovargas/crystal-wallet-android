package cy.agorise.crystalwallet.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

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


    public TransactionViewHolder(View itemView) {
        super(itemView);
        //TODO: use ButterKnife to load this
        transactionFrom = (TextView) itemView.findViewById(R.id.fromText);
        transactionTo = (TextView) itemView.findViewById(R.id.toText);
        transactionAmount = (TextView) itemView.findViewById(R.id.amountText);

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
            transactionFrom.setText(transaction.getFrom());
            transactionTo.setText(transaction.getTo());
            transactionAmount.setText("" + transaction.getAmount());
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
