package cy.agorise.crystalwallet.apigenerator;

import java.util.List;

import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.graphenej.models.HistoricalTransfer;

/**
 * Created by henry on 26/9/2017.
 */

public class GrapheneApiGenerator {

    /**
     * Retrieves the data of an account searching by it's id
     *
     * @param accountId The accountId to retrieve
     * @return The Account properties retrieved from the net, or null if fails
     */
    public GrapheneAccount getAccountById(String accountId){
        //TODO implement
        return null;
    }

    /**
     * Gets the account ID from an owner or active key
     *
     * @param address The address to retrieve
     * @return The id of the account, of null
     */
    public String getAccountByOwnerOrActiveAddress(String address){
        //TODO implement
        return null;
    }

    /**
     * Return the Transaction for an Account
     *
     * @param accountId The account id to search
     * @param start The start index of the transaction list
     * @param stop The stop index of the transaction list
     * @param limit the maximun transactions to retrieve
     * @return The list of trnsaction of the account, or null if the call fail
     */
    public List<HistoricalTransfer> getAccountTransaction(String accountId, int start, int stop, int limit){
        //TODO implement
        return null;
    }

    /**
     * Returns if an Account Name is avaible to be used for a new account
     *
     * @param accountName The account Name to find
     * @return If the account name isn't used in any bitshares account
     */
    public boolean isAccountNameAvaible(String accountName){
        //TODO implement
        return false;
    }
}
