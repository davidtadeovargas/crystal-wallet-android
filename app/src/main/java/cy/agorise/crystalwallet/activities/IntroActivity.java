package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.fragments.ImportAccountOptionsFragment;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.randomdatagenerators.RandomCryptoCoinBalanceGenerator;
import cy.agorise.crystalwallet.randomdatagenerators.RandomCryptoNetAccountGenerator;
import cy.agorise.crystalwallet.randomdatagenerators.RandomSeedGenerator;
import cy.agorise.crystalwallet.randomdatagenerators.RandomTransactionsGenerator;
import cy.agorise.crystalwallet.application.CrystalSecurityMonitor;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.surface_view)
    public SurfaceView mSurfaceView;

    @BindView(R.id.btnCreateAccount)
    public Button btnCreateAccount;

    @BindView(R.id.btnImportAccount)
    public Button btnImportAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        // Appbar animation
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Log.d(TAG,"surfaceCreated");
                MediaPlayer mediaPlayer = MediaPlayer.create(IntroActivity.this, R.raw.appbar_background);
                mediaPlayer.setDisplay(mSurfaceView.getHolder());
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                //Log.d(TAG,"surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //Log.d(TAG,"surfaceDestroyed");
            }
        });

        this.getApplication().registerActivityLifecycleCallbacks(CrystalSecurityMonitor.getInstance(this));




            //Checks if the user has any seed created
            AccountSeedListViewModel accountSeedListViewModel = ViewModelProviders.of(this).get(AccountSeedListViewModel.class);

            if (accountSeedListViewModel.accountSeedsCount() == 0) {
                //If the user doesn't have any seeds created, then
                //send the user to create/import an account
                //Intent intent = new Intent(this, AccountSeedsManagementActivity.class);
                //Intent intent = new Intent(this, ImportSeedActivity.class);
                //Intent intent = new Intent(this, CreateSeedActivity.class);
                //startActivity(intent);
            } else {
                //Intent intent = new Intent(this, CreateSeedActivity.class);
                Intent intent = new Intent(this, BoardActivity.class);
                startActivity(intent);
                finish();
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

    @OnClick(R.id.btnCreateAccount)
    public void createAccount() {
        Intent intent = new Intent(this, CreateSeedActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnImportAccount)
    public void importAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("importAccountOptions");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ImportAccountOptionsFragment newFragment = ImportAccountOptionsFragment.newInstance();
        newFragment.show(ft, "importAccountOptions");
    }
}
