package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 12/9/2017.
 */
@Dao
public interface TransactionDao {

    @Query("SELECT * FROM crypto_coin_transaction")
    LiveData<List<CryptoCoinTransaction>> getAll();

    @Query("SELECT * FROM crypto_coin_transaction ORDER BY date ASC")
    LivePagedListProvider<Integer, CryptoCoinTransaction>  transactionsByDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTransaction(CryptoCoinTransaction... transactions);

    @Query("DELETE FROM crypto_coin_transaction")
    public void deleteAllTransactions();
}
