package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Created by Henry Varona on 11/9/2017.
 */
@Entity(tableName="crypto_coin_transaction")
public class CryptoCoinTransaction {

    protected CryptoNetAccount account;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    protected int id;
    @ColumnInfo(name="date")
    protected Date date;
    @ColumnInfo(name="is_input")
    protected boolean isInput;
    @ColumnInfo(name="account_id")
    protected int accountId;
    @ColumnInfo(name="amount")
    protected int amount;
    @ColumnInfo(name="crypto_coin")
    protected CryptoCoin coin;
    @ColumnInfo(name="is_confirmed")
    protected boolean isConfirmed;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public CryptoNetAccount getAccount() {
        return account;
    }

    public void setAccount(CryptoNetAccount account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        this.isInput = input;
    }
}
