package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class TransactionsFragment extends Fragment {

    @BindView(R.id.vTransactionListView)
    TransactionListView transactionListView;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    public static TransactionsFragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transactions, container, false);
        ButterKnife.bind(this, v);

        TransactionListViewModel transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        LiveData<PagedList<CryptoCoinTransaction>> transactionsLiveData = transactionListViewModel.getTransactionList();

        final Fragment fragment = this;
        transactionsLiveData.observe(this, new Observer<PagedList<CryptoCoinTransaction>>() {
            @Override
            public void onChanged(@Nullable PagedList<CryptoCoinTransaction> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions, fragment);
            }
        });

        return v;
    }
}
