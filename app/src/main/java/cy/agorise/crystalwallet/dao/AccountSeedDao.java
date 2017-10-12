package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetBalance;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface AccountSeedDao {

    @Query("SELECT * FROM account_seed")
    LiveData<List<AccountSeed>> getAll();

    @Query("SELECT * FROM account_seed WHERE id = :id")
    LiveData<AccountSeed> findById(long id);

    @Query("SELECT COUNT(*) from account_seed")
    int countAccountSeeds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertAccountSeeds(AccountSeed... seeds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertAccountSeed(AccountSeed seed);
}
