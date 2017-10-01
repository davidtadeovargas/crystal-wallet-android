package cy.agorise.crystalwallet.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class AccountSeedViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAccountSeedName;

    public AccountSeedViewHolder(View itemView) {
        super(itemView);
        tvAccountSeedName = (TextView) itemView.findViewById(R.id.tvAccountSeedName);
    }

    public void clear(){
        tvAccountSeedName.setText("loading...");
    }

    public void bindTo(final AccountSeed accountSeed) {
        if (accountSeed == null){
            tvAccountSeedName.setText("loading...");
        } else {
            tvAccountSeedName.setText(accountSeed.getName());
        }
    }
}
