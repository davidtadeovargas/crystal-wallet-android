package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;

/**
 * Created by Henry Varona on 10/9/2017.
 */

public class CryptoCoinBalanceListView extends RelativeLayout {

    LayoutInflater mInflater;

    View rootView;
    RecyclerView listView;
    CryptoCoinBalanceListAdapter listAdapter;

    CryptoCoinBalanceListViewModel cryptoCoinBalanceListViewModel;

    private int visibleThreshold = 5;
    private boolean loading = true;

    public CryptoCoinBalanceListView(Context context){
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public CryptoCoinBalanceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public CryptoCoinBalanceListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public void init(){
        rootView = mInflater.inflate(R.layout.crypto_coin_balance_list, this, true);
        this.listView = (RecyclerView) rootView.findViewById(R.id.cryptoCoinBalanceListView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setNestedScrollingEnabled(false);
    }

    public void setData(List<CryptoCoinBalance> data){
        if (this.listAdapter == null) {
            this.listAdapter = new CryptoCoinBalanceListAdapter();
            this.listView.setAdapter(this.listAdapter);
        }

        if (data != null) {
            this.listAdapter.setList(data);
        }
    }


}
