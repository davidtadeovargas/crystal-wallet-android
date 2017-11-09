package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.BitsharesAssetInfo;

/**
 * Created by henry on 18/10/2017.
 */
@Dao
public interface BitsharesAssetDao {

    @Query("SELECT * FROM bitshares_asset")
    LiveData<List<BitsharesAssetInfo>> getAll();

    @Query("SELECT * FROM bitshares_asset WHERE crypto_curreny_id = :cryptoCurrencyId")
    LiveData<BitsharesAssetInfo> getBitsharesAssetInfo(long cryptoCurrencyId);

    @Query("SELECT * FROM bitshares_asset WHERE crypto_curreny_id = :cryptoCurrencyId")
    BitsharesAssetInfo getBitsharesAssetInfoFromCurrencyId(long cryptoCurrencyId);

    @Query("SELECT * FROM bitshares_asset WHERE bitshares_id = :bitsharesId")
    BitsharesAssetInfo getBitsharesAssetInfoById(String bitsharesId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertBitsharesAssetInfo(BitsharesAssetInfo... accounts);
}
