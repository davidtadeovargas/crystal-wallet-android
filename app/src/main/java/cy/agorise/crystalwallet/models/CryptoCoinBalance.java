package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Represents a balance of a specific asset from a CryptoNet
 *
 * Created by Henry Varona on 6/9/2017.
 */

@Entity(tableName="crypto_coin_balance",
        indices = {@Index("id"),@Index("account_id"),@Index(value = {"account_id","crypto_currency_id"}, unique=true)},
        foreignKeys = @ForeignKey(entity = CryptoNetAccount.class,
        parentColumns = "id",
        childColumns = "account_id"))
public class CryptoCoinBalance {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name="account_id")
    private long mAccountId;

    @ColumnInfo(name = "crypto_currency_id")
    private long mCryptoCurrencyId;

    @ColumnInfo(name = "balance")
    private long mBalance;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        this.mAccountId = accountId;
    }

    public long getCryptoCurrencyId() {
        return mCryptoCurrencyId;
    }

    public void setCryptoCurrencyId(long cryptoCurrencyId) {
        this.mCryptoCurrencyId = cryptoCurrencyId;
    }

    public long getBalance() {
        return mBalance;
    }

    public void setBalance(long balance) {
        this.mBalance = balance;
    }

    public static final DiffUtil.ItemCallback<CryptoCoinBalance> DIFF_CALLBACK = new DiffUtil.ItemCallback<CryptoCoinBalance>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull CryptoCoinBalance oldBalance, @NonNull CryptoCoinBalance newBalance) {
            return oldBalance.getCryptoCurrencyId() == newBalance.getCryptoCurrencyId();
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull CryptoCoinBalance oldBalance, @NonNull CryptoCoinBalance newBalance) {
            return oldBalance.equals(newBalance);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CryptoCoinBalance that = (CryptoCoinBalance) o;

        if (mAccountId != that.mAccountId) return false;
        if (mBalance != that.mBalance) return false;
        return mCryptoCurrencyId == that.mCryptoCurrencyId;

    }
}
