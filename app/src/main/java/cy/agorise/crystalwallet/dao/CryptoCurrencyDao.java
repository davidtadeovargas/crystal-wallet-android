package cy.agorise.crystalwallet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * Created by henry on 15/10/2017.
 */

@Dao
public interface CryptoCurrencyDao {

    @Query("SELECT * FROM crypto_currency")
    List<CryptoCurrency> getAll();

    @Query("SELECT * FROM crypto_currency WHERE id = :id")
    CryptoCurrency getById(int id);

    @Query("SELECT * FROM crypto_currency WHERE id IN (:ids)")
    List<CryptoCurrency> getByIds(List<Long> ids);

    @Query("SELECT * FROM crypto_currency WHERE name = :name")
    CryptoCurrency getByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCryptoCurrency(CryptoCurrency... currencies);

}
