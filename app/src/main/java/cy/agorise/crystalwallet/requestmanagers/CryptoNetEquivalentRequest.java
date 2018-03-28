package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * This a request for a simple one on one asset equivalent value
 *
 * Created by henry on 6/11/2017.
 */

public class CryptoNetEquivalentRequest extends CryptoNetInfoRequest {
    /**
     * The android context of this application
     */
    private Context context;
    /**
     * The base currency
     */
    private CryptoCurrency fromCurrency;
    /**
     * The to currency
     */
    private CryptoCurrency toCurrency;
    /**
     * The answer, less than 0 is an error, or no answer
     */
    private long price = -1;

    /**
     * Basic Constructor
     */
    public CryptoNetEquivalentRequest(CryptoCoin coin, Context context, CryptoCurrency fromCurrency, CryptoCurrency toCurrency) {
        super(coin);
        this.context = context;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CryptoCurrency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(CryptoCurrency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public CryptoCurrency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CryptoCurrency toCurrency) {
        this.toCurrency = toCurrency;
    }

    /**
     * Answer of the apigenerator
     * @param price The fetched equivalent value
     */
    public void setPrice(long price) {
        this.price = price;
        this._fireOnCarryOutEvent();
    }
}

