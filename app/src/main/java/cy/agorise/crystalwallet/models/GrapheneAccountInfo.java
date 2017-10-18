package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

/**
 * Created by henry on 24/9/2017.
 */

@Entity(tableName = "graphene_account",
        indices = {@Index("id"),@Index(value = "crypto_net_account_id",unique=true)},
        foreignKeys = @ForeignKey(entity = CryptoNetAccount.class,
        parentColumns = "id",
        childColumns = "crypto_net_account_id"))
public class GrapheneAccountInfo {

    @ColumnInfo(name = "crypto_net_account_id")
    protected String cryptoNetAccountId;

    @ColumnInfo(name = "account_name")
    protected String name;

    @ColumnInfo(name = "account_id")
    protected String accountId;

    public String getCryptoNetAccountId() {
        return cryptoNetAccountId;
    }

    public void setCryptoNetAccountId(String cryptoNetAccountId) {
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
