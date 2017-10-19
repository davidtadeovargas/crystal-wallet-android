package cy.agorise.crystalwallet.randomdatagenerators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cy.agorise.crystalwallet.dao.converters.Converters;
import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 20/9/2017.
 */

public class RandomCryptoCoinBalanceGenerator {

    static public List<CryptoCoinBalance> generateCryptoCoinBalances(List<CryptoNetAccount> accounts, int numberOfBalances, int minAmount, int maxAmount){
        ArrayList<CryptoCoinBalance> result = new ArrayList<CryptoCoinBalance>();
        Random randomGenerator = new Random();
        int randomAmount;
        CryptoCoinBalance randomBalance;
        CryptoCoin randomCryptoCoin;
        List<CryptoCoin> accountCryptoCoins;
        Converters converters = new Converters();

        for (int i=0;i<numberOfBalances;i++){
            int randomAccountIndex = randomGenerator.nextInt(accounts.size());
            CryptoNetAccount randomSelectedAccount = accounts.get(randomAccountIndex);
            randomAmount = randomGenerator.nextInt((maxAmount - minAmount) + 1) + minAmount;
            accountCryptoCoins = CryptoCoin.getByCryptoNet(randomSelectedAccount.getCryptoNet());
            randomCryptoCoin = accountCryptoCoins.get(randomGenerator.nextInt(accountCryptoCoins.size()));

            randomBalance = new CryptoCoinBalance();
            randomBalance.setAccountId(randomSelectedAccount.getId());
            randomBalance.setBalance(randomAmount);
            //randomBalance.setCoin(randomCryptoCoin);
            result.add(randomBalance);
        }

        return result;
    }
}
