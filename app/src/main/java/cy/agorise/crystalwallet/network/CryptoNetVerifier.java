package cy.agorise.crystalwallet.network;

import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * This is used to check if the connection is stable and fast.
 *
 * Also verifies if the connection with the server is valid.
 *
 * Created by henry on 28/2/2018.
 */

public abstract class CryptoNetVerifier {

    static CryptoNetVerifier getNetworkVerify(CryptoNet cryptoNet){
        if(cryptoNet.getLabel().equals(CryptoNet.BITSHARES.getLabel())){
            return new BitsharesCryptoNetVerifier();
        }
        return null;
    }

    public abstract void checkURL(final String url);

    public abstract String getChainId();
}
