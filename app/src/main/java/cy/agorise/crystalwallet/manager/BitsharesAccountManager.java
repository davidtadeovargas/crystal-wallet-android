package cy.agorise.crystalwallet.manager;

import cy.agorise.crystalwallet.apigenerator.ApiRequest;
import cy.agorise.crystalwallet.apigenerator.ApiRequestListener;
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestsListener;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.models.AccountProperties;

/**
 * Created by henry on 26/9/2017.
 */

public class BitsharesAccountManager implements CryptoAccountManager, CryptoNetInfoRequestsListener {
    @Override
    public CryptoNetAccount createAccountFromSeed(CryptoNetAccount account) {
        return null;
    }

    @Override
    public CryptoNetAccount importAccountFromSeed(CryptoNetAccount account) {
        return null;
    }

    @Override
    public void loadAccountFromDB(CryptoNetAccount account) {

    }

    @Override
    public void onNewRequest(CryptoNetInfoRequest request) {
        if (request instanceof ValidateImportBitsharesAccountRequest){

            final ValidateImportBitsharesAccountRequest importRequest = (ValidateImportBitsharesAccountRequest) request;
            ApiRequest checkAccountName = new ApiRequest(0, new ApiRequestListener() {
                @Override
                public void success(Object answer, int idPetition) {
                    importRequest.setAccountExists(true);
                    ApiRequest getAccountInfo = new ApiRequest(1,new ApiRequestListener(){
                        @Override
                        public void success(Object answer, int idPetition) {
                            if(answer != null && answer instanceof AccountProperties) {
                                AccountProperties prop = (AccountProperties) answer;
                                //TODO change the way to compare keys

                                BrainKey bk = new BrainKey(importRequest.getMnemonic(), 0);
                                for(PublicKey activeKey : prop.active.getKeyAuthList()){
                                if((new Address(activeKey.getKey(),"BTS")).toString().equals(bk.getPublicAddress("BTS").toString())){
                                    importRequest.setMnemonicIsCorrect(true);
                                    return;
                                }
                                }
                                importRequest.setMnemonicIsCorrect(false);
                            }

                        }

                        @Override
                        public void fail(int idPetition) {
                            //
                        }
                    });
                    GrapheneApiGenerator.getAccountById((String)answer,getAccountInfo);
                }

                @Override
                public void fail(int idPetition) {
                    //
                }
            });

            GrapheneApiGenerator.getAccountIdByName(importRequest.getAccountName(),checkAccountName);
        } else if (request instanceof ValidateExistBitsharesAccountRequest){
            final ValidateExistBitsharesAccountRequest importRequest = (ValidateExistBitsharesAccountRequest) request;
            ApiRequest checkAccountName = new ApiRequest(0, new ApiRequestListener() {
                @Override
                public void success(Object answer, int idPetition) {
                    importRequest.setAccountExists(true);
                }

                @Override
                public void fail(int idPetition) {
                    //TODO verified
                    importRequest.setAccountExists(false);
                }
            });
            GrapheneApiGenerator.getAccountIdByName(importRequest.getAccountName(),checkAccountName);
        }
    }
}
