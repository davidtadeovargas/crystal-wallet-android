package cy.agorise.crystalwallet.cryptonetinforequests;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Created by Henry Varona on 1/10/2017.
 */

public abstract class CryptoNetInfoRequest {
    protected CryptoCoin coin;
    protected CryptoNetInfoRequestListener listener;

    public CryptoNetInfoRequest(CryptoCoin coin){
        this.coin = coin;
    }

    public void setListener(CryptoNetInfoRequestListener listener){
        this.listener = listener;
    }

    public void _fireOnCarryOutEvent(){
        listener.onCarryOut();
    }
}
