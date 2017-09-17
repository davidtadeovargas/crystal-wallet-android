package cy.agorise.crystalwallet;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class IntroActivity extends LifecycleActivity {

    TransactionListViewModel transactionListViewModel;
    TransactionListView transactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        transactionListView = this.findViewById(R.id.transaction_list);

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        LiveData<List<CryptoCoinTransaction>> transactionData = transactionListViewModel.getTransactionList();

        transactionData.observe(this, new Observer<List<CryptoCoinTransaction>>() {
            @Override
            public void onChanged(List<CryptoCoinTransaction> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions);
            }
        });
    }
}
