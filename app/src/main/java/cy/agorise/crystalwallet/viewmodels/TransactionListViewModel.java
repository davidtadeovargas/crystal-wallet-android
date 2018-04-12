package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListProvider;
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
        /*transactionList = this.db.transactionDao().transactionsByDate().create(0,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPageSize(10)
                        .setPrefetchDistance(10)
                        .build()
        );*/
    }

    public void initTransactionList(String orderField){
        LivePagedListProvider<Integer, CryptoCoinTransaction> livePagedListProvider = null;

        switch (orderField){
            case "date":
                livePagedListProvider = this.db.transactionDao().transactionsByDate();
                break;
            case "amount":
                livePagedListProvider = this.db.transactionDao().transactionsByAmount();
                break;
            case "is_input":
                livePagedListProvider = this.db.transactionDao().transactionsByIsInput();
                break;
            case "from":
                livePagedListProvider = this.db.transactionDao().transactionsByFrom();
                break;
            case "to":
                livePagedListProvider = this.db.transactionDao().transactionsByTo();
                break;
        }
        if (livePagedListProvider != null) {
            this.transactionList = livePagedListProvider.create(0,
                    new PagedList.Config.Builder()
                            .setEnablePlaceholders(true)
                            .setPageSize(10)
                            .setPrefetchDistance(10)
                            .build()
            );
        } else {
            this.transactionList = null;
        }
    }

    public LiveData<PagedList<CryptoCoinTransaction>> getTransactionList(){
        return this.transactionList;
    }
}
