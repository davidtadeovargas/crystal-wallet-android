package cy.agorise.crystalwallet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;

/**
 * Created by henry on 15/10/2017.
 */

@Dao
public interface CryptoCurrencyEquivalenceDao {

    @Query("SELECT * FROM crypto_currency_equivalence")
    List<CryptoCurrencyEquivalence> getAll();

    @Query("SELECT * FROM crypto_currency_equivalence WHERE id = :id")
    CryptoCurrencyEquivalence getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertCryptoCurrencyEquivalence(CryptoCurrencyEquivalence... currenciesEquivalences);

}
