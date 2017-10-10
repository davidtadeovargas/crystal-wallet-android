package cy.agorise.crystalwallet.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class CryptoCoinBalanceViewHolder extends RecyclerView.ViewHolder {
    private TextView cryptoCoinName;
    private TextView cryptoCoinBalance;

    public CryptoCoinBalanceViewHolder(View itemView) {
        super(itemView);
        transactionFrom = (TextView) itemView.findViewById(R.id.fromText);
        transactionTo = (TextView) itemView.findViewById(R.id.toText);
        transactionAmount = (TextView) itemView.findViewById(R.id.amountText);

    }

    public void clear(){
        transactionFrom.setText("loading...");
        transactionTo.setText("");
        transactionAmount.setText("");
    }

    public void bindTo(final CryptoCoinTransaction transaction/*, final OnTransactionClickListener listener*/) {
        if (transaction == null){
            transactionFrom.setText("loading...");
            transactionTo.setText("");
            transactionAmount.setText("");
        } else {
            transactionFrom.setText(transaction.getFrom());
            transactionTo.setText(transaction.getTo());
            transactionAmount.setText("" + transaction.getAmount());
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClick(user);
                }
            });*/
        }
    }
}
