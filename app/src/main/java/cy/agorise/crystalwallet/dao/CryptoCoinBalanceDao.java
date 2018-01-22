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
import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface CryptoCoinBalanceDao {

    @Query("SELECT * FROM crypto_coin_balance")
    List<CryptoCoinBalance> getAll();

    @Query("SELECT id as account_id, crypto_net FROM crypto_net_account")
    LiveData<List<CryptoNetBalance>> getAllBalances();

    @Query("SELECT id FROM crypto_net_account WHERE crypto_net = 'BITSHARES' ORDER BY id ASC LIMIT 1")
    long getFirstBitsharesAccountId();

    @Query("SELECT * FROM crypto_coin_balance WHERE account_id = :accountId")
    LiveData<List<CryptoCoinBalance>> getBalancesFromAccount(long accountId);

    @Query("SELECT * FROM crypto_coin_balance WHERE account_id IN (SELECT id FROM crypto_net_account WHERE crypto_net = 'BITSHARES')")
    LiveData<List<CryptoCoinBalance>> getBalancesFromBitsharesAccount();

    @Query("SELECT * FROM crypto_coin_balance WHERE account_id = :accountId AND crypto_currency_id = :assetId")
    CryptoCoinBalance getBalanceFromAccount(long accountId, long assetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCryptoCoinBalance(CryptoCoinBalance... balances);

}
