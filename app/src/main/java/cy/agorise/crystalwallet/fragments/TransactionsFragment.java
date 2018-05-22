package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCoinTransactionExtended;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;
import cy.agorise.crystalwallet.views.TransactionOrderSpinnerAdapter;

public class TransactionsFragment extends Fragment {

    @BindView(R.id.vTransactionListView)
    TransactionListView transactionListView;

    @BindView(R.id.spTransactionsOrder)
    Spinner spTransactionsOrder;

    @BindView(R.id.etTransactionSearch)
    EditText etTransactionSearch;

    RecyclerView balanceRecyclerView;
    FloatingActionButton fabSend;
    FloatingActionButton fabReceive;

    TransactionListViewModel transactionListViewModel;
    LiveData<PagedList<CryptoCoinTransactionExtended>> transactionsLiveData;

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
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        ButterKnife.bind(this, view);

        // Gets the Balance RecyclerView
        balanceRecyclerView = view.findViewById(R.id.transactionListView);
        fabSend = getActivity().findViewById(R.id.fabSend);
        fabReceive = getActivity().findViewById(R.id.fabReceive);

        // TODO move this listener to the activity, to make this fragment reusable
        // Adds listener to the RecyclerView to show and hide buttons at the bottom of the screen
        balanceRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                // Scroll Down
                if( dy >0 ) {
                    if( fabSend.isShown() )
                        fabSend.hide();
                    if( fabReceive.isShown() )
                        fabReceive.hide();
                }
                // Scroll Up
                else if( dy <0 ) {
                    if( !fabSend.isShown() )
                        fabSend.show();
                    if( !fabReceive.isShown() )
                        fabReceive.show();
                }
            }
        });

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);


        initTransactionsOrderSpinner();
        changeTransactionList();
        return view;
    }

    public void changeTransactionList(){
        TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem orderSelected =
                (TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem)(spTransactionsOrder.getSelectedItem());

        if (transactionsLiveData != null){
            transactionsLiveData.removeObservers(this);
        }
        transactionListViewModel.initTransactionList(orderSelected.getField(),etTransactionSearch.getText().toString());
        transactionsLiveData = transactionListViewModel.getTransactionList();

        final Fragment fragment = this;
        transactionsLiveData.observe(this, new Observer<PagedList<CryptoCoinTransactionExtended>>() {
            @Override
            public void onChanged(@Nullable PagedList<CryptoCoinTransactionExtended> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions, fragment);
            }
        });
    }

    @OnTextChanged(value = R.id.etTransactionSearch,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTransactionSearchChange(Editable editable) {
        changeTransactionList();
    }

    public void initTransactionsOrderSpinner(){
        List<TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem> spinnerValues = new ArrayList<TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem>();
        spinnerValues.add(new TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem("date","Date",0,false));
        spinnerValues.add(new TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem("amount","Amount",0,false));
        spinnerValues.add(new TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem("is_input","In/Out",0,false));
        spinnerValues.add(new TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem("from","From",0,false));
        spinnerValues.add(new TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem("to","To",0,false));

        TransactionOrderSpinnerAdapter transactionOrderSpinnerAdapter =
                new TransactionOrderSpinnerAdapter(
                        getContext(), android.R.layout.simple_spinner_item,spinnerValues
                );
        spTransactionsOrder.setAdapter(transactionOrderSpinnerAdapter);

        spTransactionsOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeTransactionList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
