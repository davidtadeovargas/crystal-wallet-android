package cy.agorise.crystalwallet.views;


import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;

/**
 * Created by Henry Varona on 11/9/2017.
 */

public class CryptoCoinBalanceListAdapter extends ListAdapter<CryptoCoinBalance, CryptoCoinBalanceViewHolder> {

    public CryptoCoinBalanceListAdapter() {
        super(CryptoCoinBalance.DIFF_CALLBACK);
    }

    @Override
    public CryptoCoinBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_coin_balance_list_item,parent,false);


        return new CryptoCoinBalanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CryptoCoinBalanceViewHolder holder, int position) {
        CryptoCoinBalance balance = getItem(position);
        if (balance != null) {
            holder.bindTo(balance);
        } else {
            holder.clear();
        }
    }
}
