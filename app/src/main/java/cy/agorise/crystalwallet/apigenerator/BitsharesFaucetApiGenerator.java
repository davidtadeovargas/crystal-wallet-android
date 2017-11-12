package cy.agorise.crystalwallet.apigenerator;

import com.google.gson.Gson;

/**
 * This maanges the calls for the creation of accounts using the bitshares faucet
 *
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
     * @param url The url of the faucet
     * @return The bitshares id of the registered account, or null
     */
    public static String registerBitsharesAccount(String accountName, String ownerKey,
                                                  String activeKey, String memoKey, String url){
        CreateAccountPetition petition = new CreateAccountPetition();
        Account account = new Account();
        account.name=accountName;
        account.owner_key=ownerKey;
        account.active_key=activeKey;
        account.memo_key=memoKey;
        petition.account=account;
        Gson gson = new Gson();
        String jsonPetition = gson.toJson(petition);
        System.out.println("create account petition :" + jsonPetition);

        //TODO faucet function
        return null;
    }

    /**
     * Class used for the json serializer. this represents a peitition
     */
    private static class CreateAccountPetition{
        // The account to be created
        Account account;
    }

    /**
     * Class used for the json serializer. This represents the account on the petition
     */
    private static class Account{
        /**
         * The name of the account
         */
        String name;
        /**
         * The owner key address
         */
        String owner_key;
        /**
         * The active key address
         */
        String active_key;
        /**
         * The memo key address
         */
        String memo_key;
    }
}
