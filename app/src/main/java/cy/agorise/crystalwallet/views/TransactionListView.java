package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.dao.TransactionDao;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;

/**
 * Created by Henry Varona on 10/9/2017.
 */

public class TransactionListView extends RelativeLayout {

    View rootView;
    ListView listView;
    ListAdapter listAdapter;

    TransactionListViewModel transactionListViewModel;

    public TransactionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = inflate(context, R.layout.transaction_list, this);
        listView = rootView.findViewById(R.id.transactionListView);
    }

    public void init(TransactionListViewModel transactionListViewModel){
        this.transactionListViewModel = transactionListViewModel;
        listAdapter = new TransactionListAdapter(this.getContext(), transactionListViewModel.getTransactionList());
        listView.setAdapter(listAdapter);
    }


}
