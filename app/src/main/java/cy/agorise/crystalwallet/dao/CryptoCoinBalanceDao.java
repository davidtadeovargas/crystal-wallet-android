package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.CryptoNetBalance;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface CryptoCoinBalanceDao {

    @Query("SELECT * FROM crypto_coin_balance")
    List<CryptoCoinBalance> getAll();

    @Query("SELECT * FROM crypto_net_account WHERE account_id = :acountId")
    LiveData<List<CryptoNetBalance>> getAllFromAccount(long accountId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCryptoCoinBalance(CryptoCoinBalance... balances);

}
