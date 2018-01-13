package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.viewmodels.validators.ImportSeedValidator;

/**
 * Created by Henry Varona on 27/9/2017.
 */

public class AccountSeedViewModel extends AndroidViewModel {

    private LiveData<AccountSeed> accountSeed;
    private CrystalDatabase db;
    private Application app;

    public AccountSeedViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        this.app = application;
    }

    public void loadSeed(long seedId){
        this.accountSeed = this.db.accountSeedDao().findByIdLiveData(seedId);
    }

    public void addSeed(AccountSeed seed){
        long newId = this.db.accountSeedDao().insertAccountSeed(seed);
        seed.setId(newId);
    }

    public LiveData<AccountSeed> getAccountSeed(){
        return this.accountSeed;
    }

}
