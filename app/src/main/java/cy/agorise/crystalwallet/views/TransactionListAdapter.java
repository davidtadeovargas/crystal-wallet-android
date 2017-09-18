package cy.agorise.crystalwallet.views;


import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public class TransactionListAdapter extends PagedListAdapter<CryptoCoinTransaction, TransactionViewHolder> {

    public TransactionListAdapter() {
        super(CryptoCoinTransaction.DIFF_CALLBACK);
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        CryptoCoinTransaction transaction = getItem(position);
        if (transaction != null) {
            holder.bindTo(transaction);
        } else {
            holder.clear();
        }
    }
}
