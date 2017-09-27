package cy.agorise.crystalwallet.randomdatagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 20/9/2017.
 */

public class RandomCryptoNetAccountGenerator {

    static public List<CryptoNetAccount> generateAccounts(int numberOfAccounts,List<AccountSeed> seeds){
        ArrayList<CryptoNetAccount> result = new ArrayList<CryptoNetAccount>();
        Random randomGenerator = new Random();
        CryptoNetAccount randomAccount;

        for (int i=0;i<numberOfAccounts;i++){
            int randomSeedIndex = randomGenerator.nextInt(seeds.size());
            AccountSeed randomSelectedSeed = seeds.get(randomSeedIndex);
            int randomAccountIndex = randomGenerator.nextInt(1000);
            int randomAccountNumber = randomGenerator.nextInt(1000);

            randomAccount = new CryptoNetAccount();
            randomAccount.setSeedId(randomSelectedSeed.getId());
            randomAccount.setAccountIndex(randomAccountIndex);
            randomAccount.setAccountNumber(randomAccountNumber);
            result.add(randomAccount);
        }

        return result;
    }
}
