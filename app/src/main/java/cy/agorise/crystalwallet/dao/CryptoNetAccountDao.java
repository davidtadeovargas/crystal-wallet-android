package cy.agorise.crystalwallet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface CryptoNetAccountDao {

    @Query("SELECT * FROM crypto_net_account")
    List<CryptoNetAccount> getAll();
}
