package cy.agorise.crystalwallet.network;

import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 *
 * Created by henry on 28/2/2018.
 */

public class BitsharesCryptoNetVerifier extends CryptoNetVerifier {

    /**
     * TODO We need to change this to a type of subCryptoNet
     */
    private final CryptoNet cryptoNet = CryptoNet.BITSHARES;
    /**
     * Todo info need to be on the SubCryptoNet
     */
    private final String CHAIN_ID = "9cf6f255a208100d2bb275a3c52f4b1589b7ec9c9bfc2cb2a5fe6411295106d8";//testnet


    @Override
    public void checkURL(final String url) {
        final long startTime = System.currentTimeMillis();
        WebSocketThread thread = new WebSocketThread(new GetChainId(new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if(response.result instanceof String) {
                    if(response.result.equals(CHAIN_ID)) {
                        CryptoNetManager.verifiedCryptoNetURL(cryptoNet, url, System.currentTimeMillis() - startTime);
                    }else{
                        System.out.println("Error we are not in the net current chain id " + response.result + " excepted " + CHAIN_ID);
                    }
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {

            }
        }),url);
    }
}
