package cy.agorise.crystalwallet.views;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 11/9/2017.
 */

public class TransactionListAdapter extends ArrayAdapter<CryptoCoinTransaction> {



    public TransactionListAdapter(Context context, int resource, List<CryptoCoinTransaction> items) {
        super(context, resource, items);
    }
}
