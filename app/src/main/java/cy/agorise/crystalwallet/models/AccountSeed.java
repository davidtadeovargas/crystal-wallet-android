package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import cy.agorise.crystalwallet.enums.SeedType;

/**
 * Represents a type of crypto seed for HD wallets
 *
 * Created by Henry Varona on 6/9/2017.
 */
@Entity(tableName = "account_seed")
public class AccountSeed {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    /**
     * The name or tag of this seed
     */
    @ColumnInfo(name = "name")
    private String mName;

    /**
     * The bytes of the master seed
     */
    @ColumnInfo(name = "master_seed")
    private String mMasterSeed;

    /**
     * The type of this seed: BIP39, BRAINKEY
     */
    private SeedType type;

    public long getId() {
        return mId;
    }

    public void setId(long id){
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getMasterSeed() {
        return mMasterSeed;
    }

    public void setMasterSeed(String mMasterSeed) {
        this.mMasterSeed = mMasterSeed;
    }

    public SeedType getType() {
        return type;
    }

    public void setType(SeedType type) {
        this.type = type;
    }

    public static final DiffCallback<AccountSeed> DIFF_CALLBACK = new DiffCallback<AccountSeed>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull AccountSeed oldAccountSeed, @NonNull AccountSeed newAccountSeed) {
            return oldAccountSeed.getId() == newAccountSeed.getId();
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull AccountSeed oldAccountSeed, @NonNull AccountSeed newAccountSeed) {
            return oldAccountSeed.equals(newAccountSeed);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountSeed that = (AccountSeed) o;

        if (mId != that.mId) return false;
        return  mMasterSeed.equals(that.mMasterSeed);

    }
}
