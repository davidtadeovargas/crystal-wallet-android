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
    private int mId;

    /**
     * The id of the seed used by this account
     */
    @ColumnInfo(name = "seed_id")
    private int mSeedId;

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

    public int getId() {
        return mId;
    }

    public void setId(int id){
        this.mId = id;
    }

    public int getSeedId() {
        return mSeedId;
    }

    public void setSeedId(int mSeedId) {
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
}
