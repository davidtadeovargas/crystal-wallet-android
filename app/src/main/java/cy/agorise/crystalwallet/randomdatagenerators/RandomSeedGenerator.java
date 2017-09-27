package cy.agorise.crystalwallet.randomdatagenerators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

/**
 * Created by Henry Varona on 20/9/2017.
 */

public class RandomSeedGenerator {

    static public List<AccountSeed> generateSeeds(int numberOfSeeds){
        ArrayList<AccountSeed> result = new ArrayList<AccountSeed>();
        Random randomGenerator = new Random();
        AccountSeed randomSeed;

        for (int i=0;i<numberOfSeeds;i++){
            int randomInt = randomGenerator.nextInt(999999999);

            randomSeed = new AccountSeed();
            randomSeed.setMasterSeed(""+randomInt);
            randomSeed.setName("seed"+randomInt);
            result.add(randomSeed);
        }

        return result;
    }
}
