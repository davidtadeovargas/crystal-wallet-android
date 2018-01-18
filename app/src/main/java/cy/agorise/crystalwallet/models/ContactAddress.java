package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

/**
 * Represents a user contact address
 *
 * Created by Henry Varona on 1/16/2018.
 */

@Entity(tableName="contact_address",
        primaryKeys = {"contact_id","crypto_currency_id"},
        indices = {@Index(value = {"contact_id","crypto_currency_id"}, unique=true)})
public class ContactAddress {

    @ColumnInfo(name = "contact_id")
    private long mContactId;

    @ColumnInfo(name = "crypto_currency_id")
    private long mCryptoCurrencyId;

    @ColumnInfo(name="address")
    private String mAddress;

    public long getContactId() {
        return mContactId;
    }

    public void setContactId(long contactId) {
        this.mContactId = contactId;
    }

    public long getCryptoCurrencyId() {
        return mCryptoCurrencyId;
    }

    public void setCryptoCurrencyId(long cryptoCurrencyId) {
        this.mCryptoCurrencyId = cryptoCurrencyId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

   public static final DiffCallback<ContactAddress> DIFF_CALLBACK = new DiffCallback<ContactAddress>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull ContactAddress oldContactAddress, @NonNull ContactAddress newContactAddress) {
            return (oldContactAddress.getContactId() == newContactAddress.getContactId())
                    && (oldContactAddress.getCryptoCurrencyId() == newContactAddress.getCryptoCurrencyId());
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull ContactAddress oldContactAddress, @NonNull ContactAddress newContactAddress) {
            return oldContactAddress.equals(newContactAddress);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactAddress that = (ContactAddress) o;

        if (mContactId != that.mContactId) return false;
        if (mCryptoCurrencyId != that.mCryptoCurrencyId) return false;
        return mAddress.equals(that.mAddress);
    }
}
