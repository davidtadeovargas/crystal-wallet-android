package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Represents a geneeric CryptoNet Account Balance
 *
 * Created by Henry Varona on 6/9/2017.
 */

@Entity(tableName = "crypto_net_account",
        indices = {@Index("id"),@Index("seed_id")},
        foreignKeys = @ForeignKey(entity = AccountSeed.class,
        parentColumns = "id",
        childColumns = "seed_id"))
public class CryptoNetBalance {

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

    private CryptoCoin mCryptoCoin;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getSeedId() {
        return mSeedId;
    }

    public void setSeedId(long seedId) {
        this.mSeedId = seedId;
    }

    public int getAccountNumber() {
        return mAccountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.mAccountNumber = accountNumber;
    }

    public int getAccountIndex() {
        return mAccountIndex;
    }

    public void setAccountIndex(int accountIndex) {
        this.mAccountIndex = accountIndex;
    }

    public CryptoCoin getCryptoCoin() {
        return mCryptoCoin;
    }

    public void setCryptoCoin(CryptoCoin cryptoCoin) {
        this.mCryptoCoin = cryptoCoin;
    }

    public static final DiffCallback<CryptoNetBalance> DIFF_CALLBACK = new DiffCallback<CryptoNetBalance>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull CryptoNetBalance oldBalance, @NonNull CryptoNetBalance newBalance) {
            return oldBalance.getId() == newBalance.getId();
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull CryptoNetBalance oldBalance, @NonNull CryptoNetBalance newBalance) {
            return oldBalance.equals(newBalance);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CryptoNetBalance that = (CryptoNetBalance) o;

        if (mId != that.mId) return false;
        if (mSeedId != that.mSeedId) return false;
        if (mAccountNumber != that.mAccountNumber) return false;
        return mAccountIndex == that.mAccountIndex;

    }
}
