package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.BitsharesAccountNameCache;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;

/**
 * Created by Henry Varona on 6/15/2018.
 */

@Dao
public interface BitsharesAccountNameCacheDao {

    @Query("SELECT * FROM bitshares_account_name_cache WHERE account_id = :account_id")
    LiveData<BitsharesAccountNameCache> getLDByAccountId(String account_id);

    @Query("SELECT * FROM bitshares_account_name_cache WHERE account_id = :account_id")
    BitsharesAccountNameCache getByAccountId(String account_id);

    @Query("SELECT * FROM bitshares_account_name_cache WHERE account_name = :name")
    BitsharesAccountNameCache getByAccountName(String account_name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertBitsharesAccountNameCache(BitsharesAccountNameCache... accountsNames);

}
