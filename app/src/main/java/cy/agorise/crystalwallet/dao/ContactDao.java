package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 1/17/2018.
 */
@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    LiveData<List<Contact>> getAll();

    @Query("SELECT * FROM contact ORDER BY name ASC")
    LivePagedListProvider<Integer, Contact>  contactsByName();

    @Query("SELECT * FROM contact WHERE id = :id")
    LiveData<Contact> getById(long id);

    @Query("SELECT count(*) FROM contact WHERE name = :name")
    boolean existsByName(String name);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long[] add(Contact... contacts);
}
