package cy.agorise.crystalwallet.views;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 11/9/2017.
 */

public class TransactionListAdapter extends ArrayAdapter<CryptoCoinTransaction> {

    //List<CryptoCoinTransaction> items;

    public TransactionListAdapter(Context context, LiveData<List<CryptoCoinTransaction>> items) {
        super(context, 0, items.getValue());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CryptoCoinTransaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_list_item, parent, false);
        }

        TextView tvFrom = (TextView) convertView.findViewById(R.id.fromText);
        TextView tvTo = (TextView) convertView.findViewById(R.id.toText);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.amountText);

        tvFrom.setText(transaction.getFrom());
        tvTo.setText(transaction.getTo());
        tvAmount.setText(transaction.getAmount());

        return convertView;
    }
}
