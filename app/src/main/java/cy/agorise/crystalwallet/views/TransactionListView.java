package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cy.agorise.crystalwallet.R;

/**
 * Created by Henry Varona on 10/9/2017.
 */

public class TransactionListView extends RelativeLayout {

    View rootView;
    ListView list;
    ListAdapter listAdapter;

    public TransactionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = inflate(context, R.layout.transaction_list, this);
        list = rootView.findViewById(R.id.transactionListView);
        listAdapter = new TransactionListAdapter();
        list.setAdapter(listAdapter);
    }


}
