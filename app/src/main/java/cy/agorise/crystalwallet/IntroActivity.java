package cy.agorise.crystalwallet;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class IntroActivity extends LifecycleActivity {

    TransactionListViewModel transactionListViewModel;
    TransactionListView transactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        transactionListView = this.findViewById(R.id.transaction_list);

        transactionListView.init(transactionListViewModel);
    }
}
