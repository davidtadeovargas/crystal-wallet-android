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
    private ImportSeedValidator importSeedValidator;
    private Application app;

    public AccountSeedViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        this.app = application;
    }

    public void loadSeed(int seedId){
        this.accountSeed = this.db.accountSeedDao().findById(seedId);
    }

    public ImportSeedValidator getValidator(){
        if (this.importSeedValidator == null){
            this.importSeedValidator = new ImportSeedValidator(this.app.getResources());

        }
        return null;
    }

    public void addSeed(AccountSeed seed){
        this.db.accountSeedDao().insertAccountSeed(seed);
    }

    public LiveData<AccountSeed> getAccountSeed(){
        return this.accountSeed;
    }

    public void validateAccountSeed(){
        if (this.accountSeed != null){
            AccountSeed seed = this.accountSeed.getValue();

            ValidateImportBitsharesAccountRequest request = new ValidateImportBitsharesAccountRequest(seed.getName(),seed.getMasterSeed());
            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {

                }
            });
        }
    }
}
