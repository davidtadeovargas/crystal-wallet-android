package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Represents a geneeric CryptoNet Account
 *
 * Created by Henry Varona on 6/9/2017.
 */

@Entity(tableName = "crypto_net_account",
        indices = {@Index("id"),@Index("seed_id")},
        foreignKeys = @ForeignKey(entity = AccountSeed.class,
        parentColumns = "id",
        childColumns = "seed_id"))
public class CryptoNetAccount {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    /**
     * The id of the seed used by this account
     */
    @ColumnInfo(name = "seed_id")
    private long mSeedId;

    /**
     * The account number on the bip44 or slip44
     */
    @ColumnInfo(name = "account_number")
    private int mAccountNumber;

    /**
     * The account index on this wallet
     */
    @ColumnInfo(name = "account_index")
    private int mAccountIndex;

    /**
     * The type of the account
     */
    @ColumnInfo(name = "type")
    private int type;

    public long getId() {
        return mId;
    }

    public void setId(long id){
        this.mId = id;
    }

    public long getSeedId() {
        return mSeedId;
    }

    public void setSeedId(long mSeedId) {
        this.mSeedId = mSeedId;
    }

    public int getAccountNumber() {
        return mAccountNumber;
    }

    public void setAccountNumber(int mAccountNumber) {
        this.mAccountNumber = mAccountNumber;
    }

    public int getAccountIndex() {
        return mAccountIndex;
    }

    public void setAccountIndex(int mAccountIndex) {
        this.mAccountIndex = mAccountIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
