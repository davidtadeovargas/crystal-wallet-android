package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

/**
 * Created by henry on 24/9/2017.
 */

@Entity(tableName = "graphene_account",
        primaryKeys = {"crypto_net_account_id"},
        foreignKeys = @ForeignKey(entity = CryptoNetAccount.class,
        parentColumns = "id",
        childColumns = "crypto_net_account_id"))
public class GrapheneAccountInfo {

    @ColumnInfo(name = "crypto_net_account_id")
    protected long cryptoNetAccountId;

    @ColumnInfo(name = "account_name")
    protected String name;

    @ColumnInfo(name = "account_id")
    protected String accountId;

    public GrapheneAccountInfo(long cryptoNetAccountId) {
        this.cryptoNetAccountId = cryptoNetAccountId;
    }

    public GrapheneAccountInfo(GrapheneAccount account) {
        this.cryptoNetAccountId = account.getId();
        this.name = account.getName();
        this.accountId = account.getAccountId();
    }

    public long getCryptoNetAccountId() {
        return cryptoNetAccountId;
    }

    public void setCryptoNetAccountId(long cryptoNetAccountId) {
        this.cryptoNetAccountId = cryptoNetAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
