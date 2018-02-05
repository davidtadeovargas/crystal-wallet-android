package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 1/17/2018.
 */

public class ContactListViewModel extends AndroidViewModel {

    private LiveData<PagedList<Contact>> contactList;
    private CrystalDatabase db;

    public ContactListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        contactList = this.db.contactDao().contactsByName().create(0,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPageSize(10)
                        .setPrefetchDistance(10)
                        .build()
        );
    }

    public LiveData<PagedList<Contact>> getContactList(){
        return this.contactList;
    }

    public  boolean contactExists(String name){
        return this.db.contactDao().existsByName(name);
    }
}
