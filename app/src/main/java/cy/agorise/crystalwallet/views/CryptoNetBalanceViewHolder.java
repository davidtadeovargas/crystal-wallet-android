package cy.agorise.crystalwallet.views;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoNetBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class CryptoNetBalanceViewHolder extends RecyclerView.ViewHolder {
    private ImageView cryptoNetIcon;
    private TextView cryptoNetName;
    private CryptoCoinBalanceListView cryptoCoinBalanceListView;

    private Fragment fragment;

    public CryptoNetBalanceViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        cryptoNetIcon = (ImageView) itemView.findViewById(R.id.ivCryptoNetIcon);
        cryptoNetName = (TextView) itemView.findViewById(R.id.tvCryptoNetName);
        cryptoCoinBalanceListView = (CryptoCoinBalanceListView) itemView.findViewById(R.id.cryptoCoinBalancesListView);
        this.fragment = fragment;
    }

    public void clear(){
        cryptoNetName.setText("loading...");
    }

    public void bindTo(final CryptoNetBalance balance) {
        if (balance == null){
            cryptoNetName.setText("loading...");
        } else {
            cryptoNetName.setText(balance.getCryptoNet().getLabel());

            CryptoCoinBalanceListViewModel cryptoCoinBalanceListViewModel = ViewModelProviders.of(this.fragment).get(CryptoCoinBalanceListViewModel.class);
            cryptoCoinBalanceListViewModel.init(balance.getAccountId());
            LiveData<List<CryptoCoinBalance>> cryptoCoinBalanceData = cryptoCoinBalanceListViewModel.getCryptoCoinBalanceList();
            cryptoCoinBalanceListView.setData(null);

            cryptoCoinBalanceData.observe((LifecycleOwner)this.itemView.getContext(), new Observer<List<CryptoCoinBalance>>() {
                @Override
                public void onChanged(List<CryptoCoinBalance> cryptoCoinBalances) {
                    cryptoCoinBalanceListView.setData(cryptoCoinBalances);
                }
            });
        }
    }
}
