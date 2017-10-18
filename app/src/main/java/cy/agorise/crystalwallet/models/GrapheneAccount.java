package cy.agorise.crystalwallet.models;

import org.bitcoinj.core.ECKey;

/**
 *
 * Created by henry on 24/9/2017.
 */

public class GrapheneAccount extends CryptoNetAccount {

    public static int subclass = 1;
    protected String name;
    protected String accountId;

    public GrapheneAccount() {
    }

    public GrapheneAccount(CryptoNetAccount account) {
        super(account.getId(),account.getSeedId(),account.getAccountIndex(),account.getCryptoNet());
    }

    public void loadInfo(GrapheneAccountInfo info){
        this.name = info.getName();
        this.accountId = info.getAccountId();
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

    /**
     * Return the owner key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getOwnerKey(){
        //TODO implement
        return null;
    }

    /**
     * Return the active key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getActiveKey(){
        //TODO implement
        return null;
    }

    /**
     * Return the memo key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getMemoKey(){
        //TODO implement
        return null;
    }
}
