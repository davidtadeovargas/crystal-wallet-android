package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.views.TransactionListView;

/**
 * Created by Henry Varona on 12/9/2017.
 */

public class TransactionListViewModel extends AndroidViewModel {

    private LiveData<List<CryptoCoinTransaction>> transactionList;
    private CrystalDatabase db;

    public TransactionListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        transactionList = this.db.transactionDao().getAll();
    }

    public LiveData<List<CryptoCoinTransaction>> getTransactionList(){
        return this.transactionList;
    }

}
