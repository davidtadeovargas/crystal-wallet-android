package cy.agorise.crystalwallet.views;


import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoNetBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;

/**
 * Created by Henry Varona on 11/9/2017.
 */

public class CryptoNetBalanceListAdapter extends ListAdapter<CryptoNetBalance, CryptoNetBalanceViewHolder> {

    Fragment fragment;

    public CryptoNetBalanceListAdapter(Fragment fragment) {
        super(CryptoNetBalance.DIFF_CALLBACK);
        this.fragment = fragment;
    }

    @Override
    public CryptoNetBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_list_item,parent,false);


        return new CryptoNetBalanceViewHolder(v, fragment);
    }

    @Override
    public void onBindViewHolder(CryptoNetBalanceViewHolder holder, int position) {
        CryptoNetBalance balance = getItem(position);
        if (balance != null) {
            holder.bindTo(balance);
        } else {
            holder.clear();
        }
    }
}
