package cy.agorise.crystalwallet.models;

/**
 * Created by henry on 24/9/2017.
 */

public class GrapheneAccount extends CryptoNetAccount {

    public static int subclass = 1;
    protected String name;
    protected String accountId;

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
