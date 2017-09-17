package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.util.AttributeSet;
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

    View rootView;
    ListView listView;
    TransactionListAdapter listAdapter;

    TransactionListViewModel transactionListViewModel;

    public TransactionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = inflate(context, R.layout.transaction_list, this);
        this.listView = rootView.findViewById(R.id.transactionListView);
    }

    public void setData(List<CryptoCoinTransaction> data){
        if (this.listAdapter == null) {
            this.listAdapter = new TransactionListAdapter(this.getContext(), data);
            this.listView.setAdapter(this.listAdapter);
        } else {
            this.listAdapter.updateData(data);
        }
    }


}
