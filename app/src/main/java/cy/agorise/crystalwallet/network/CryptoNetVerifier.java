package cy.agorise.crystalwallet.network;

import android.app.Activity;

import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * This is used to check if the connection is stable and fast.
 *
 * Also verifies if the connection with the server is valid.
 *
 * Created by henry on 28/2/2018.
 */

public abstract class CryptoNetVerifier {

    /*
    * Contains the worker connection thread
    */
    protected WebSocketThread thread;




    static CryptoNetVerifier getNetworkVerify(CryptoNet cryptoNet){
        if(cryptoNet.getLabel().equals(CryptoNet.BITSHARES.getLabel())){
            return new BitsharesCryptoNetVerifier();
        }
        return null;
    }

    public abstract void checkURL(final String url);

    public abstract String getChainId();


    public WebSocketThread getThread() {
        return thread;
    }
}
