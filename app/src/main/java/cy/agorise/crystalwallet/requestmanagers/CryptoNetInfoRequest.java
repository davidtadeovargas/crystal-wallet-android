package cy.agorise.crystalwallet.requestmanagers;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * An request for the managers. Is used for the asyncrhonous petition of each manager
 *
 *
 * Created by Henry Varona on 1/10/2017.
 */

public abstract class CryptoNetInfoRequest {
    /**
     * The cryptocoin this request belongs
     */
    protected CryptoCoin coin;
    /**
     * The listener for the answer of this petition
     */
    protected CryptoNetInfoRequestListener listener;

    protected CryptoNetInfoRequest(CryptoCoin coin){
        this.coin = coin;
    }

    public void setListener(CryptoNetInfoRequestListener listener){
        this.listener = listener;
    }

    protected void _fireOnCarryOutEvent(){
        listener.onCarryOut();
        CryptoNetInfoRequests.getInstance().removeRequest(this);
    }
}
