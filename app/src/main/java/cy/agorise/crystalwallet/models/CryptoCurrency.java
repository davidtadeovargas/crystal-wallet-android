package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import cy.agorise.crystalwallet.dao.converters.Converters;
import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * Represents each currency in transaction and balances
 *
 * Created by henry Henry Varona on 11/9/2017.
 */
@Entity(tableName="crypto_currency")
public class CryptoCurrency {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    /**
     * The name or tag of this currency
     */
    @ColumnInfo(name = "name")
    private String mName;

    /**
     * CryptoCoin network where this currency belongs to
     */
    @ColumnInfo(name = "crypto_net")
    @TypeConverters(Converters.class)
    private CryptoNet mCryptoNet;


    /**
     * The decimal point
     */
    @ColumnInfo(name = "precision")
    private int mPrecision;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public CryptoNet getCryptoNet() {
        return mCryptoNet;
    }

    public void setCryptoNet(CryptoNet cryptoNet) {
        this.mCryptoNet = cryptoNet;
    }

    public int getPrecision() {
        return mPrecision;
    }

    public void setPrecision(int precision) {
        this.mPrecision = precision;
    }
}
