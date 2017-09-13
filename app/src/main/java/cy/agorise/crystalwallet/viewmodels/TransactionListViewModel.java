package cy.agorise.crystalwallet.viewmodels;

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

public class TransactionListViewModel extends ViewModel {

    private LiveData<List<CryptoCoinTransaction>> transactionList;
    private CrystalDatabase db;

    public TransactionListViewModel(Context context){
        this.db = CrystalDatabase.getAppDatabase(context);
        transactionList = this.db.transactionDao().getAll();
    }

    public LiveData<List<CryptoCoinTransaction>> getTransactionList(){
        return this.transactionList;
    }

}
