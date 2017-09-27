package cy.agorise.crystalwallet.views;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;

/**
 * Created by Henry Varona on 10/9/2017.
 */

public class TransactionListView extends RelativeLayout {

    LayoutInflater mInflater;

    View rootView;
    RecyclerView listView;
    TransactionListAdapter listAdapter;

    TransactionListViewModel transactionListViewModel;

    private int visibleThreshold = 5;
    private boolean loading = true;

    public TransactionListView(Context context){
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public TransactionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public TransactionListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public void init(){
        rootView = mInflater.inflate(R.layout.transaction_list, this, true);
        this.listView = rootView.findViewById(R.id.transactionListView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setNestedScrollingEnabled(false);


        /*this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loading && linearLayoutManager.getItemCount() <= (linearLayoutManager.findLastVisibleItemPosition() + visibleThreshold)){
                    onLoadMore();
                    loading = true;
                }
            }
        });*/
    }

    //public void onLoadMore(){
    //    listAdapter.add();

    //}

    public void setData(PagedList<CryptoCoinTransaction> data){
        if (this.listAdapter == null) {
            this.listAdapter = new TransactionListAdapter();
            this.listView.setAdapter(this.listAdapter);
        }

        if (data != null) {
            this.listAdapter.setList(data);
        }
    }


}
