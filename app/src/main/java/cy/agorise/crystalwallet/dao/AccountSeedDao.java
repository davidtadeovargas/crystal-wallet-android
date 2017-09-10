package cy.agorise.crystalwallet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface AccountSeedDao {

    @Query("SELECT * FROM account_seed")
    List<AccountSeed> getAll();
}
