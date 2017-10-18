package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface GrapheneAccountInfoDao {

    @Query("SELECT * FROM graphene_account WHERE subclass = 1")
    LiveData<List<GrapheneAccountInfo>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertGrapheneAccountInfo(GrapheneAccountInfo... accounts);

}
