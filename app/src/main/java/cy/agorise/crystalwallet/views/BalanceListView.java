package cy.agorise.crystalwallet.views;

import android.arch.paging.PagedList;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;

/**
 * Created by Henry Varona on 10/9/2017.
 */

public class BalanceListView extends RelativeLayout {

    LayoutInflater mInflater;

    View rootView;
    RecyclerView listView;
    BalanceListAdapter listAdapter;

    BalanceListViewModel balanceListViewModel;

    private int visibleThreshold = 5;
    private boolean loading = true;

    public BalanceListView(Context context){
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public BalanceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public BalanceListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public void init(){
        rootView = mInflater.inflate(R.layout.balance_list, this, true);
        this.listView = rootView.findViewById(R.id.balanceListView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setNestedScrollingEnabled(false);
    }

    public void setData(List<CryptoNetBalance> data){
        if (this.listAdapter == null) {
            this.listAdapter = new BalanceListAdapter();
            this.listView.setAdapter(this.listAdapter);
        }

        if (data != null) {
            this.listAdapter.setList(data);
        }
    }


}
