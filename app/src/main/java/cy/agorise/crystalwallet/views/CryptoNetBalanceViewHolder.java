package cy.agorise.crystalwallet.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoNetBalance;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class CryptoNetBalanceViewHolder extends RecyclerView.ViewHolder {
    private ImageView cryptoNetIcon;
    private TextView cryptoNetName;
    private CryptoNetBalanceListView cryptoNetBalanceListView;


    public CryptoNetBalanceViewHolder(View itemView) {
        super(itemView);
        cryptoNetIcon = (ImageView) itemView.findViewById(R.id.ivCryptoNetIcon);
        cryptoNetName = (TextView) itemView.findViewById(R.id.tvCryptoNetName);
        cryptoNetBalanceListView = (CryptoNetBalanceListView) itemView.findViewById(R.id.cryptoCoinBalanceListView);

    }

    public void clear(){
        cryptoNetName.setText("loading...");
    }

    public void bindTo(final CryptoNetBalance balance) {
        if (balance == null){
            cryptoNetName.setText("loading...");
        } else {
            cryptoNetName.setText(balance.getCryptoCoin().getLabel());
        }
    }
}
