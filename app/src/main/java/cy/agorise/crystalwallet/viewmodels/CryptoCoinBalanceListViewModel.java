package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.CryptoNetBalance;

/**
 * Created by Henry Varona on 27/9/2017.
 */

public class CryptoCoinBalanceListViewModel extends AndroidViewModel {

    private LiveData<List<CryptoCoinBalance>> cryptoCoinBalanceList;
    private CrystalDatabase db;

    public CryptoCoinBalanceListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
    }

    public void init(long cryptoNetAccountId){
        this.cryptoCoinBalanceList = this.db.cryptoCoinBalanceDao().getBalancesFromAccount(cryptoNetAccountId);
    }

    public LiveData<List<CryptoCoinBalance>> getCryptoCoinBalanceList(){
        return this.cryptoCoinBalanceList;
    }
}
