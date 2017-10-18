package cy.agorise.crystalwallet.apigenerator;

/**
 * Created by henry on 15/10/2017.
 */

public abstract class BitsharesFaucetApiGenerator {

    /**
     * Class to register a new Bitshares Account
     *
     * @param accountName The name of the Account to be register
     * @param ownerKey The owner key public address
     * @param activeKey The active key public address
     * @param memoKey the memo key public address
     * @return The bitshares id of the registered account, or null
     */
    public static String registerBitsharesAccount(String accountName, String ownerKey, String activeKey, String memoKey){
        //TODO faucet function
        return null;
    }
}
