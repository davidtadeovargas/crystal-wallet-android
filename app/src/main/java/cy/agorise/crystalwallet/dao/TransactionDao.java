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
import cy.agorise.crystalwallet.models.CryptoCoinTransactionExtended;

/**
 * Created by Henry Varona on 12/9/2017.
 */
@Dao
public interface TransactionDao {

    static final String transactionsQuery = "SELECT cct.*, cna.name AS user_account_name, c.name AS contact_name FROM crypto_coin_transaction cct " +
            "LEFT JOIN crypto_net_account cna ON cct.account_id = cna.id " +
            "LEFT JOIN contact c ON c.id =  (SELECT ca.contact_id FROM contact_address ca WHERE ca.address LIKE (CASE is_input WHEN 1 THEN cct.\"from\" ELSE cct.\"to\" END) LIMIT 1) " +
            "WHERE user_account_name LIKE '%'||:search||'%' OR contact_name LIKE '%'||:search||'%' OR cct.\"from\" LIKE '%'||:search||'%' OR cct.\"to\" LIKE '%'||:search||'%'";

    @Query("SELECT * FROM crypto_coin_transaction")
    LiveData<List<CryptoCoinTransaction>> getAll();

    @Query(transactionsQuery + " ORDER BY date DESC")
    LivePagedListProvider<Integer, CryptoCoinTransactionExtended>  transactionsByDate(String search);

    @Query(transactionsQuery + " ORDER BY amount DESC")
    LivePagedListProvider<Integer, CryptoCoinTransactionExtended>  transactionsByAmount(String search);

    @Query(transactionsQuery + " ORDER BY is_input DESC")
    LivePagedListProvider<Integer, CryptoCoinTransactionExtended>  transactionsByIsInput(String search);

    @Query(transactionsQuery + " ORDER BY `from` DESC")
    LivePagedListProvider<Integer, CryptoCoinTransactionExtended>  transactionsByFrom(String search);

    @Query(transactionsQuery + " ORDER BY `to` DESC")
    LivePagedListProvider<Integer, CryptoCoinTransactionExtended>  transactionsByTo(String search);

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
