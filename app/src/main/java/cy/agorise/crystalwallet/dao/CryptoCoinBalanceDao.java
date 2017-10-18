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

    @Query("SELECT id as account_id, crypto_net FROM crypto_net_account")
    LiveData<List<CryptoNetBalance>> getAllBalances();

    @Query("SELECT * FROM crypto_coin_balance WHERE account_id = :accountId")
    LiveData<List<CryptoCoinBalance>> getBalancesFromAccount(long accountId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCryptoCoinBalance(CryptoCoinBalance... balances);

}
