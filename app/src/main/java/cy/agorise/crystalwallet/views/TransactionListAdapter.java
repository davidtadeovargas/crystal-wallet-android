package cy.agorise.crystalwallet.views;


import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import cy.agorise.crystalwallet.models.CryptoCoinTransactionExtended;

/**
 * Created by Henry Varona on 11/9/2017.
 *
 * An adapter to show the elements of a list of crypto net account transactions.
 *
 * Extends from a paged list, so not all transactions will be loaded immediately, but only a segment
 * that will be extended with the scroll of the user
 */

public class TransactionListAdapter extends PagedListAdapter<CryptoCoinTransactionExtended, TransactionViewHolder> {

    private Fragment fragment;

    TransactionListAdapter(Fragment fragment) {
        super(CryptoCoinTransactionExtended.DIFF_CALLBACK);
        this.fragment = fragment;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item,parent,false);


        return new TransactionViewHolder(v, this.fragment);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        CryptoCoinTransactionExtended transaction = getItem(position);
        if (transaction != null) {
            holder.bindTo(transaction);
        } else {
            holder.clear();
        }
    }
}
