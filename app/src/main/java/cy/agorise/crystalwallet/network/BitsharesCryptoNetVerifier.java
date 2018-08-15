package cy.agorise.crystalwallet.network;

import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
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
    //private final String CHAIN_ID = "4018d7844c78f6a6c41c6a552b898022310fc5dec06da467ee7905a8dad512c8";//mainnet






    public BitsharesCryptoNetVerifier(){

        /**/
        final long startTime = System.currentTimeMillis();
        thread = new WebSocketThread(new GetChainId(new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if(response.result instanceof String) {
                    if(response.result.equals(CHAIN_ID)) {
                        CryptoNetManager.verifiedCryptoNetURL(cryptoNet, null, System.currentTimeMillis() - startTime);
                    }else{
                        System.out.println(" BitsharesCryptoNetVerifier Error we are not in the net current chain id " + response.result + " excepted " + CHAIN_ID);
                        //TODO handle error bad chain
                    }
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                //TODO handle error
            }
        }),null);

    }


    @Override
    public void checkURL(final String url) {
        thread.setmUrl(url); //Set the url
        thread.start(); //Run the thread connection
    }

    @Override
    public String getChainId() {
        return CHAIN_ID;
    }
}
