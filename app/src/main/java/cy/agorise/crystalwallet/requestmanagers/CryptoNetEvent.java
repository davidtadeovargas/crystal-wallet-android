package cy.agorise.crystalwallet.requestmanagers;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * An event fired from the service
 *
 *
 * Created by Henry Varona on 4/28/2018.
 */

public abstract class CryptoNetEvent {
    /**
     * The cryptocoin this events belongs to
     */
    protected CryptoCoin coin;

    protected CryptoNetEvent(CryptoCoin coin){
        this.coin = coin;
    }
}
