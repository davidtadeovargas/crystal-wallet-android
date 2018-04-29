package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * This event gets fired when a user crypto net account receives funds
 *
 * Created by henry on 4/28/2018.
 */

public class ReceivedFundsCryptoNetEvent extends CryptoNetEvent {

    private CryptoNetAccount account;

    public ReceivedFundsCryptoNetEvent(CryptoCoin coin, CryptoNetAccount account) {
        super(coin);
        this.account = account;
    }

    public CryptoNetAccount getAccount() {
        return this.account;
    }

    public void setAccount(CryptoNetAccount account) {
        this.account = account;
    }
}

