package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Represents a generic CryptoNet Transaction
 *
 * Created by Henry Varona on 11/9/2017.
 */
@Entity(tableName="crypto_coin_transaction")
public class CryptoCoinTransaction {

    /**
     * The account associated with this transaction
     */
    protected CryptoNetAccount account;

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    protected int id;
    /**
     * The full date of this transaction
     */
    @ColumnInfo(name="date")
    protected Date date;
    /**
     * If this transaction is input of the account associated with it
     */
    @ColumnInfo(name="is_input")
    protected boolean isInput;
    /**
     * The id of the account assoiciated, this is used for the foreign key definition
     */
    @ColumnInfo(name="account_id")
    protected int accountId;
    /**
     * The amount of asset is moved in this transaction
     */
    @ColumnInfo(name="amount")
    protected int amount;
    /**
     * The crypto Coin associated with this transaction
     */
    @ColumnInfo(name="crypto_coin")
    protected CryptoCoin coin;
    /**
     * If this transaction is confirmed
     */
    @ColumnInfo(name="is_confirmed")
    protected boolean isConfirmed;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @ColumnInfo(name="from")
    protected String from;
    @ColumnInfo(name="to")
    protected String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

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
