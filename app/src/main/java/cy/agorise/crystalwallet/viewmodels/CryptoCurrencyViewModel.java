package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;

/**
 * Created by Henry Varona on 4/11/2017.
 */

public class CryptoCurrencyViewModel extends AndroidViewModel {

    private CrystalDatabase db;

    public CryptoCurrencyViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
    }

    public CryptoCurrency getCryptoCurrencyById(long id){
        return this.db.cryptoCurrencyDao().getById(id);
    }
}
