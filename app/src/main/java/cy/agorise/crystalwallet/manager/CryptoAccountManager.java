package cy.agorise.crystalwallet.manager;

import android.content.Context;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Manager of each crypto coin account
 *
 * Created by henry on 24/9/2017.
 */

public interface CryptoAccountManager {

    /**
     * Creates a CryptoCoin Account, with the values of the account
     * @param account The values to be created,
     * @returnThe CruptoNetAccount created, or null if it couldn't be created
     */
    public CryptoNetAccount createAccountFromSeed(CryptoNetAccount account, Context context);

    /**
     * Imports a CryptoCoin account from a seed
     * @param account A CryptoNetAccount with the parameters to be imported
     * @returnThe CruptoNetAccount imported
     */
    public CryptoNetAccount importAccountFromSeed(CryptoNetAccount account, Context context);

    /**
     * Loads account data from the database
     *
     * @param account The CryptoNetAccount to be loaded
     */
    public void loadAccountFromDB(CryptoNetAccount account, Context context);
}
