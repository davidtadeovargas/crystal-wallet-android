package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user contact
 *
 * Created by Henry Varona on 1/16/2018.
 */

@Entity(tableName="contact",
        indices = {@Index("id"),@Index(value = {"name"}, unique=true)})
public class Contact {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name="name")
    private String mName;

    @ColumnInfo(name = "gravatar")
    private String mGravatar;

    @Ignore
    public List<ContactAddress> mAddresses;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getGravatar() {
        return mGravatar;
    }

    public void setGravatar(String gravatar) {
        this.mGravatar = gravatar;
    }

    public int addressesCount(){
        return this.mAddresses.size();
    }

    public ContactAddress getAddress(int index){
        return this.mAddresses.get(index);
    }

    public void clearAddresses(){
        if (this.mAddresses != null) {
            this.mAddresses.clear();
        }
    }

    public void addAddress(ContactAddress address){
        if (this.mAddresses == null) {
            this.mAddresses = new ArrayList<ContactAddress>();
        }
        this.mAddresses.add(address);
        address.setContactId(this.getId());
    }

    public static final DiffCallback<Contact> DIFF_CALLBACK = new DiffCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull Contact oldContact, @NonNull Contact newContact) {
            return oldContact.getId() == newContact.getId();
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull Contact oldContact, @NonNull Contact newContact) {
            return oldContact.equals(newContact);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (mId != contact.mId) return false;
        if (!mName.equals(contact.mName)) return false;
        if (mGravatar != null ? !mGravatar.equals(contact.mGravatar) : contact.mGravatar != null)
            return false;
        return mAddresses != null ? mAddresses.equals(contact.mAddresses) : contact.mAddresses == null;
    }
}
