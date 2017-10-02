package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by Henry Varona on 27/9/2017.
 */

public class AccountSeedViewModel extends AndroidViewModel {

    private LiveData<AccountSeed> accountSeed;
    private CrystalDatabase db;

    public AccountSeedViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
    }

    public void loadSeed(int seedId){
        this.accountSeed = this.db.accountSeedDao().findById(seedId);
    }

    public void addSeed(AccountSeed seed){
        this.db.accountSeedDao().insertAccountSeed(seed);
    }

    public LiveData<AccountSeed> getAccountSeed(){
        return this.accountSeed;
    }
}
