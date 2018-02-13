package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.ContactAddress;
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

    @Query("SELECT * FROM contact_address WHERE contact_id = :contactId")
    LiveData<List<ContactAddress>> getContactAddresses(long contactId);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public void update(Contact... contacts);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long[] add(Contact... contacts);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void addAddresses(ContactAddress... contactAddresses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateAddresses(ContactAddress... contactAddresses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateAddressesFields(ContactAddress... contactAddresses);
}
