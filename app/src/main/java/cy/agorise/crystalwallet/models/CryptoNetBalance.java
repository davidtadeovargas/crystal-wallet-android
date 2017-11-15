package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.enums.CryptoNet;

import static android.arch.persistence.room.ColumnInfo.INTEGER;

/**
 * Created by Henry Varona on 6/9/2017.
 *
 * Represents a generic Account Balance from a specific CryptoNet
 */

@Entity
public class CryptoNetBalance {

    /**
     * The id of the account of this balance
     */
    @ColumnInfo(name = "account_id")
    private long mAccountId;

    /**
     * The crypto net of the account
     */
    @ColumnInfo(name = "crypto_net")
    private CryptoNet mCryptoNet;

    public long getAccountId() {
        return mAccountId;
    }

    public CryptoNet getCryptoNet() {
        return mCryptoNet;
    }

    public void setAccountId(long accountId) {
        this.mAccountId = accountId;
    }

    public void setCryptoNet(CryptoNet cryptoNet) {
        this.mCryptoNet = cryptoNet;
    }

    public static final DiffCallback<CryptoNetBalance> DIFF_CALLBACK = new DiffCallback<CryptoNetBalance>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull CryptoNetBalance oldBalance, @NonNull CryptoNetBalance newBalance) {
            return oldBalance.getAccountId() == newBalance.getAccountId();
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
        return mAccountId == that.mAccountId;

    }
}
