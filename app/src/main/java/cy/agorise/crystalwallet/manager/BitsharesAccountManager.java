package cy.agorise.crystalwallet.manager;

import android.arch.lifecycle.LiveData;

import com.google.common.primitives.UnsignedLong;

import org.bitcoinj.core.ECKey;

import java.util.ArrayList;

import cy.agorise.crystalwallet.apigenerator.ApiRequest;
import cy.agorise.crystalwallet.apigenerator.ApiRequestListener;
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestsListener;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.operations.TransferOperationBuilder;

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
            this.validateImportAccount((ValidateImportBitsharesAccountRequest) request);
        } else if (request instanceof ValidateExistBitsharesAccountRequest){
            this.validateExistAcccount((ValidateExistBitsharesAccountRequest) request);
        }
    }

    private void validateImportAccount(final ValidateImportBitsharesAccountRequest importRequest){
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
    }

    private void validateExistAcccount(final ValidateExistBitsharesAccountRequest validateRequest){
        ApiRequest checkAccountName = new ApiRequest(0, new ApiRequestListener() {
            @Override
            public void success(Object answer, int idPetition) {
                validateRequest.setAccountExists(true);
            }

            @Override
            public void fail(int idPetition) {
                //TODO verified
                validateRequest.setAccountExists(false);
            }
        });
        GrapheneApiGenerator.getAccountIdByName(validateRequest.getAccountName(),checkAccountName);
    }

    private void validateSendRequest(final ValidateBitsharesSendRequest sendRequest){
        Asset feeAsset = new Asset(sendRequest.getFeeAsset());
        TransferOperationBuilder builder = new TransferOperationBuilder()
                .setSource(new UserAccount(sendRequest.getSourceAccount().getAccountId()))
                .setDestination(new UserAccount(sendRequest.getToAccount()))
                .setTransferAmount(new AssetAmount(UnsignedLong.valueOf(sendRequest.getBaseAmount()), new Asset(sendRequest.getBaseAsset())))
                .setFee(new AssetAmount(UnsignedLong.valueOf(sendRequest.getFeeAmount()), feeAsset));
        //TODO memo
        ArrayList<BaseOperation> operationList = new ArrayList();
        operationList.add(builder.build());

        //TODO get privateKey with seed model
        LiveData<AccountSeed> seed = CrystalDatabase.getAppDatabase(sendRequest.getContext()).accountSeedDao().findById(sendRequest.getSourceAccount().getSeedId());

        ECKey privateKey = new BrainKey(seed.getValue().getMasterSeed(),0).getPrivateKey();
        Transaction transaction = new Transaction(privateKey, null, operationList);

        ApiRequest transactionRequest = new ApiRequest(0, new ApiRequestListener() {
            @Override
            public void success(Object answer, int idPetition) {
                sendRequest.setSend(true);
            }

            @Override
            public void fail(int idPetition) {
                sendRequest.setSend(false);
            }
        });

        GrapheneApiGenerator.broadcastTransaction(transaction,feeAsset, transactionRequest);
    }
}
