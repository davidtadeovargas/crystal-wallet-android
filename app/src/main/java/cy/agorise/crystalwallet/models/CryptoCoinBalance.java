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
 * Represents a balance of a specific asset from a CryptoNet
 *
 * Created by Henry Varona on 6/9/2017.
 */

@Entity
public class CryptoCoinBalance {

    @ColumnInfo(name = "coin")
    private CryptoCoin mCoin;

    @ColumnInfo(name = "balance")
    private int mBalance;

    public CryptoCoin getCoin() {
        return mCoin;
    }

    public void setCoin(CryptoCoin coin) {
        this.mCoin = coin;
    }

    public int getBalance() {
        return mBalance;
    }

    public void setBalance(int balance) {
        this.mBalance = balance;
    }

    public static final DiffCallback<CryptoCoinBalance> DIFF_CALLBACK = new DiffCallback<CryptoCoinBalance>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull CryptoCoinBalance oldBalance, @NonNull CryptoCoinBalance newBalance) {
            return oldBalance.getCoin() == newBalance.getCoin();
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

        if (this.mCoin != that.mCoin) return false;
        return  mBalance == that.mBalance;

    }
}
