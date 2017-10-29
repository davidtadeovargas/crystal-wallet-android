package cy.agorise.crystalwallet.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.activities.SendTransactionActivity;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoNetBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;

/**
 * Created by Henry Varona on 17/9/2017.
 */

public class CryptoNetBalanceViewHolder extends RecyclerView.ViewHolder {
    //@BindView(R.id.ivCryptoNetIcon)
    ImageView cryptoNetIcon;

    //@BindView(R.id.tvCryptoNetName)
    TextView cryptoNetName;

    //@BindView(R.id.cryptoCoinBalancesListView)
    CryptoCoinBalanceListView cryptoCoinBalanceListView;

    @BindView(R.id.btnSendFromThisAccount)
    Button btnSendFromThisAccount;

    Context context;

    long cryptoNetAccountId;

    private Fragment fragment;

    public CryptoNetBalanceViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        this.cryptoNetAccountId = -1;

        cryptoNetIcon = (ImageView) itemView.findViewById(R.id.ivCryptoNetIcon);
        cryptoNetName = (TextView) itemView.findViewById(R.id.tvCryptoNetName);
        cryptoCoinBalanceListView = (CryptoCoinBalanceListView) itemView.findViewById(R.id.cryptoCoinBalancesListView);
        btnSendFromThisAccount = (Button) itemView.findViewById(R.id.btnSendFromThisAccount);
        btnSendFromThisAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFromThisAccount();
            }
        });
        this.fragment = fragment;
        this.context = itemView.getContext();
    }

    public void clear(){
        cryptoNetName.setText("loading...");
    }

    //@OnClick(R.id.btnSendFromThisAccount)
    public void sendFromThisAccount(){
        if (this.cryptoNetAccountId >= 0) {
            //Intent intent = new Intent(this.context, SendTransactionActivity.class);
            //this.context.startActivity(intent);

            Intent startActivity = new Intent();
            startActivity.setClass(context, SendTransactionActivity.class);
            startActivity.setAction(SendTransactionActivity.class.getName());
            startActivity.putExtra("CRYPTO_NET_ACCOUNT_ID", this.cryptoNetAccountId);
            startActivity.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(startActivity);
        }
    }

    public void bindTo(final CryptoNetBalance balance) {
        if (balance == null){
            cryptoNetName.setText("loading...");
        } else {
            this.cryptoNetAccountId = balance.getAccountId();
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
