package cy.agorise.crystalwallet.manager;

import cy.agorise.crystalwallet.apigenerator.ApiRequest;
import cy.agorise.crystalwallet.apigenerator.ApiRequestListener;
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestsListener;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

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
                    ApiRequest getAccountInfo = new ApiRequest(1,new ApiRequestListener(){
                        @Override
                        public void success(Object answer, int idPetition) {
                                //TODO compare keys
                        }

                        @Override
                        public void fail(int idPetition) {
                            importRequest._fireOnCarryOutEvent();
                        }
                    });
                    GrapheneApiGenerator.getAccountById((String)answer,getAccountInfo);
                }

                @Override
                public void fail(int idPetition) {
                    importRequest._fireOnCarryOutEvent();
                }
            });

            GrapheneApiGenerator.getAccountIdByName(importRequest.getAccountName(),checkAccountName);
        }
    }
}
