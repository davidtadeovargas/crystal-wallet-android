package cy.agorise.crystalwallet.cryptonetinforequests;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * Created by henry on 6/11/2017.
 */

public class CryptoNetEquivalentRequest extends CryptoNetInfoRequest {

    private Context context;
    private CryptoCurrency fromCurrency;
    private CryptoCurrency toCurrency;
    private long price = -1;

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

    public void setPrice(long price) {
        this.price = price;
        this._fireOnCarryOutEvent();
    }
}

