package cy.agorise.crystalwallet;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.randomdatagenerators.RandomTransactionsGenerator;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

import static cy.agorise.crystalwallet.R.string.transactions;

public class IntroActivity extends LifecycleActivity {

    TransactionListViewModel transactionListViewModel;
    TransactionListView transactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /*CrystalDatabase db = CrystalDatabase.getAppDatabase(getApplicationContext());
        List<CryptoCoinTransaction> transactions = RandomTransactionsGenerator.generateTransactions(100,1262304001,1496275201,1,999999999);

        for(int i=0;i<transactions.size();i++) {
            db.transactionDao().insertTransaction(transactions.get(i));
        }*/

        transactionListView = this.findViewById(R.id.transaction_list);

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        LiveData<PagedList<CryptoCoinTransaction>> transactionData = transactionListViewModel.getTransactionList();
        transactionListView.setData(null);

        transactionData.observe(this, new Observer<PagedList<CryptoCoinTransaction>>() {
            @Override
            public void onChanged(PagedList<CryptoCoinTransaction> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions);
            }
        });
    }
}
