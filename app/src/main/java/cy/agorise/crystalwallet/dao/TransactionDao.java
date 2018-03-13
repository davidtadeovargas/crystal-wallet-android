package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 12/9/2017.
 */
@Dao
public interface TransactionDao {

    @Query("SELECT * FROM crypto_coin_transaction")
    LiveData<List<CryptoCoinTransaction>> getAll();

    @Query("SELECT * FROM crypto_coin_transaction ORDER BY date DESC")
    LivePagedListProvider<Integer, CryptoCoinTransaction>  transactionsByDate();

    @Query("SELECT * FROM crypto_coin_transaction WHERE account_id = :idAccount ORDER BY date DESC")
    LiveData<List<CryptoCoinTransaction>> getByIdAccountLiveData(long idAccount);

    @Query("SELECT * FROM crypto_coin_transaction WHERE account_id = :idAccount ORDER BY date DESC")
    List<CryptoCoinTransaction> getByIdAccount(long idAccount);

    @Query("SELECT * FROM crypto_coin_transaction WHERE id = :id")
    LiveData<CryptoCoinTransaction> getById(long id);

    @Query("SELECT * FROM crypto_coin_transaction WHERE date = :date and 'from' = :from and 'to' = :to and amount = :amount ")
    CryptoCoinTransaction getByTransaction(Date date, String from, String to, long amount);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertTransaction(CryptoCoinTransaction... transactions);

    @Query("DELETE FROM crypto_coin_transaction")
    public void deleteAllTransactions();
}
