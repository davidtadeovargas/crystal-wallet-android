package cy.agorise.crystalwallet.apigenerator;

import com.google.gson.Gson;

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


    private static class CreateAccountPetition{
        Account account;
    }

    private static class Account{
        String name;
        String owner_key;
        String active_key;
        String memo_key;
    }
}
