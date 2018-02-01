package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.application.CrystalSecurityMonitor;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class IntroActivity extends AppCompatActivity {

    TransactionListViewModel transactionListViewModel;
    TransactionListView transactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        this.getApplication().registerActivityLifecycleCallbacks(new CrystalSecurityMonitor(this));


        //Checks if the user has any seed created
        AccountSeedListViewModel accountSeedListViewModel = ViewModelProviders.of(this).get(AccountSeedListViewModel.class);

        if (accountSeedListViewModel.accountSeedsCount() == 0){
            //If the user doesn't have any seeds created, then
            //send the user to create/import an account
            //Intent intent = new Intent(this, AccountSeedsManagementActivity.class);
            //Intent intent = new Intent(this, ImportSeedActivity.class);
            Intent intent = new Intent(this, CreateSeedActivity.class);
            startActivity(intent);
        } else {
            //Intent intent = new Intent(this, CreateSeedActivity.class);
            Intent intent = new Intent(this, BoardActivity.class);
            startActivity(intent);
        }

        /*CrystalDatabase db = CrystalDatabase.getAppDatabase(getApplicationContext());
        List<AccountSeed> seeds = RandomSeedGenerator.generateSeeds(2);
        for(int i=0;i<seeds.size();i++) {
            long newId = db.accountSeedDao().insertAccountSeed(seeds.get(i));
            seeds.get(i).setId(newId);
        }
        List<CryptoNetAccount> accounts = RandomCryptoNetAccountGenerator.generateAccounts(5,seeds);
        for(int i=0;i<accounts.size();i++) {
            long newId = db.cryptoNetAccountDao().insertCryptoNetAccount(accounts.get(i))[0];
            accounts.get(i).setId(newId);
        }
        List<CryptoCoinBalance> balances = RandomCryptoCoinBalanceGenerator.generateCryptoCoinBalances(accounts,5,1,20);
        for(int i=0;i<balances.size();i++) {
            long newId = db.cryptoCoinBalanceDao().insertCryptoCoinBalance(balances.get(i))[0];
            balances.get(i).setId(newId);
        }
        List<CryptoCoinTransaction> transactions = RandomTransactionsGenerator.generateTransactions(accounts,100,1262304001,1496275201,1,999999999);
        for(int i=0;i<transactions.size();i++) {
            long newId = db.transactionDao().insertTransaction(transactions.get(i))[0];
            transactions.get(i).setId(newId);
        }*/

        /*transactionListView = this.findViewById(R.id.transaction_list);

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);
        LiveData<PagedList<CryptoCoinTransaction>> transactionData = transactionListViewModel.getTransactionList();
        transactionListView.setData(null);

        transactionData.observe(this, new Observer<PagedList<CryptoCoinTransaction>>() {
            @Override
            public void onChanged(PagedList<CryptoCoinTransaction> cryptoCoinTransactions) {
                transactionListView.setData(cryptoCoinTransactions);
            }
        });*/
    }
}
