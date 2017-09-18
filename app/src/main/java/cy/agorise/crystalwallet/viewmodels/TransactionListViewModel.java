package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.content.Context;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.views.TransactionListView;

/**
 * Created by Henry Varona on 12/9/2017.
 */

public class TransactionListViewModel extends AndroidViewModel {

    private LiveData<PagedList<CryptoCoinTransaction>> transactionList;
    private CrystalDatabase db;

    public TransactionListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        transactionList = this.db.transactionDao().transactionsByDate().create(0,
                new PagedList.Config.Builder()
                        .setPageSize(10)
                        .setPrefetchDistance(10)
                        .build()
        );
    }

    public LiveData<PagedList<CryptoCoinTransaction>> getTransactionList(){
        return this.transactionList;
    }
}
