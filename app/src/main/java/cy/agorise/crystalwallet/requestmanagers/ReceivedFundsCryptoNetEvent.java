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

    private CryptoCurrency currency;

    private long amount;

    public ReceivedFundsCryptoNetEvent(CryptoNetAccount account, CryptoCurrency currency, long amount) {
        super(CryptoCoin.BITSHARES);
        this.account = account;
        this.currency = currency;
        this.amount = amount;
    }

    public CryptoNetAccount getAccount() {
        return this.account;
    }

    public void setAccount(CryptoNetAccount account) {
        this.account = account;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}

