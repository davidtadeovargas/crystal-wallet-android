package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * Represents a generic CryptoNet Account
 *
 * Created by Henry Varona on 6/9/2017.
 */

@Entity(tableName = "crypto_net_account",
        indices = {@Index("id"),@Index("seed_id"),@Index(value = {"seed_id","crypto_net","account_index"},unique=true)},
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
     * The account index on this wallet
     */
    @ColumnInfo(name = "account_index")
    private int mAccountIndex;

    /**
     * The crypto net of the account
     */
    @ColumnInfo(name = "crypto_net")
    private CryptoNet mCryptoNet;

    public CryptoNetAccount() {
    }

    public CryptoNetAccount(long mId, long mSeedId, int mAccountIndex, CryptoNet mCryptoNet) {
        this.mId = mId;
        this.mSeedId = mSeedId;
        this.mAccountIndex = mAccountIndex;
        this.mCryptoNet = mCryptoNet;
    }

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

    public int getAccountIndex() {
        return mAccountIndex;
    }

    public void setAccountIndex(int mAccountIndex) {
        this.mAccountIndex = mAccountIndex;
    }

    public CryptoNet getCryptoNet() {
        return mCryptoNet;
    }

    public void setCryptoNet(CryptoNet cryptoNet) {
        this.mCryptoNet = cryptoNet;
    }
}
