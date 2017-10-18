package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.ViewModelProviders;
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
    private CryptoCoinBalanceListView cryptoCoinBalanceListView;


    public CryptoNetBalanceViewHolder(View itemView) {
        super(itemView);
        cryptoNetIcon = (ImageView) itemView.findViewById(R.id.ivCryptoNetIcon);
        cryptoNetName = (TextView) itemView.findViewById(R.id.tvCryptoNetName);
        cryptoCoinBalanceListView = (CryptoCoinBalanceListView) itemView.findViewById(R.id.cryptoCoinBalancesListView);

    }

    public void clear(){
        cryptoNetName.setText("loading...");
    }

    public void bindTo(final CryptoNetBalance balance) {
        if (balance == null){
            cryptoNetName.setText("loading...");
        } else {
            cryptoNetName.setText(balance.getCryptoNet().getLabel());

            /*transactionListView = this.findViewById(R.id.transaction_list);

        transactionListViewModel = ViewModelProviders.of(getContext).get(TransactionListViewModel.class);
        LiveData<PagedList<CryptoCoinTransaction>> transactionData = transactionListViewModel.getTransactionList();
        transactionListView.setData(null);

        transactionData.observe(this, new Observer<PagedList<CryptoCoinTransaction>>() {
            @Override
            public void onChanged(PagedList<CryptoCoinTransaction> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions);
            }
        });

            cryptoCoinBalanceListView.setData();*/
        }
    }
}
