package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.*;

/**
 * Created by Henry Varona on 6/9/2017.
 */

@Entity (tableName = "crypto_net_account",
        indices = {@Index("id"),@Index("seed_id")},
        foreignKeys = @ForeignKey(entity = AccountSeed.class,
        parentColumns = "id",
        childColumns = "seed_id"))
public class CryptoNetAccount {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "seed_id")
    private int mSeedId;

    @ColumnInfo(name = "account_number")
    private int mAccountNumber;

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
