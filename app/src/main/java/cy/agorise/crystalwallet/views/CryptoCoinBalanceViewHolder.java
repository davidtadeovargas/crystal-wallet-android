package cy.agorise.crystalwallet.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class CryptoCoinBalanceViewHolder extends RecyclerView.ViewHolder {
    private TextView cryptoCoinName;
    private TextView cryptoCoinBalance;

    public CryptoCoinBalanceViewHolder(View itemView) {
        super(itemView);
        cryptoCoinName = (TextView) itemView.findViewById(R.id.tvCryptoCoinName);
        cryptoCoinBalance = (TextView) itemView.findViewById(R.id.tvCryptoCoinBalanceAmount);

    }

    public void clear(){
        cryptoCoinName.setText("loading...");
        cryptoCoinBalance.setText("");
    }

    public void bindTo(final CryptoCoinBalance balance/*, final OnTransactionClickListener listener*/) {
        if (balance == null){
            cryptoCoinName.setText("loading...");
            cryptoCoinBalance.setText("");
        } else {
            //cryptoCoinName.setText(balance.getCoin().getLabel());
            cryptoCoinBalance.setText(""+balance.getBalance());
        }
    }
}
