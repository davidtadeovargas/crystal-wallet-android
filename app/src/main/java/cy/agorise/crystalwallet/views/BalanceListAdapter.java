package cy.agorise.crystalwallet.views;


import android.arch.paging.PagedListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cy.agorise.crystalwallet.R;

/**
 * Created by Henry Varona on 11/9/2017.
 */

public class BalanceListAdapter extends ListAdapter<CryptoNetBalance, BalanceViewHolder> {

    public BalanceListAdapter() {
        super(CryptoNetBalance.DIFF_CALLBACK);
    }

    @Override
    public BalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_list_item,parent,false);


        return new BalanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BalanceViewHolder holder, int position) {
        CryptoNetBalance balance = getItem(position);
        if (balance != null) {
            holder.bindTo(balance);
        } else {
            holder.clear();
        }
    }
}
