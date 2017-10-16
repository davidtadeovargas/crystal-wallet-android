package cy.agorise.crystalwallet.manager;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.google.common.primitives.UnsignedLong;

import org.bitcoinj.core.ECKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.operations.TransferOperationBuilder;

/**
 * Created by henry on 26/9/2017.
 */

public class BitsharesAccountManager implements CryptoAccountManager, CryptoNetInfoRequestsListener {
    @Override
    public CryptoNetAccount createAccountFromSeed(CryptoNetAccount account, Context context) {
        //TODO generate account keys
        //TODO register account faucet api
        //TODO save account on DB
        //TODO subscribe account
        return null;
    }

    @Override
    public CryptoNetAccount importAccountFromSeed(CryptoNetAccount account, Context context) {

        if(account instanceof GrapheneAccount) {
            GrapheneAccount grapheneAccount = (GrapheneAccount) account;

            if(grapheneAccount.getAccountId() == null){
                grapheneAccount = this.getAccountInfoByName(grapheneAccount.getName());
            }else if(grapheneAccount.getName() == null){
                grapheneAccount = this.getAccountInfoById(grapheneAccount.getAccountId());
            }
            //TODO grapaheneAccount null, error fetching

            CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
            //TODO save account on DB

            GrapheneApiGenerator.subscribeBitsharesAccount(grapheneAccount.getId(), grapheneAccount.getAccountId(), context);
            this.refreshAccountTransactions(account.getId(), context);
            GrapheneApiGenerator.getAccountBalance(grapheneAccount.getId(), grapheneAccount.getAccountId(), context);
            return grapheneAccount;
        }
        return null;
    }

    @Override
    public void loadAccountFromDB(CryptoNetAccount account, Context context) {
        if(account instanceof GrapheneAccount){
            GrapheneAccount grapheneAccount = (GrapheneAccount) account;
            if(grapheneAccount.getAccountId() == null){
                grapheneAccount = this.getAccountInfoByName(grapheneAccount.getName());
            }else if(grapheneAccount.getName() == null){
                grapheneAccount = this.getAccountInfoById(grapheneAccount.getAccountId());
            }
            //TODO grapaheneAccount null, error fetching

            GrapheneApiGenerator.subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
            this.refreshAccountTransactions(account.getId(),context);
            GrapheneApiGenerator.getAccountBalance(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
        }
    }

    @Override
    public void onNewRequest(CryptoNetInfoRequest request) {
        if (request instanceof ValidateImportBitsharesAccountRequest){
            this.validateImportAccount((ValidateImportBitsharesAccountRequest) request);
        } else if (request instanceof ValidateExistBitsharesAccountRequest){
            this.validateExistAcccount((ValidateExistBitsharesAccountRequest) request);
        } else if (request instanceof ValidateBitsharesSendRequest){
            this.validateSendRequest((ValidateBitsharesSendRequest) request);
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

    private GrapheneAccount getAccountInfoById(String grapheneId){
        final Object SYNC = new Object();
        long timeout = 60000;

        AccountIdOrNameListener listener = new AccountIdOrNameListener(SYNC);

        ApiRequest request = new ApiRequest(0, listener);
        GrapheneApiGenerator.getAccountById(grapheneId,request);

        long cTime = System.currentTimeMillis();

        while(!listener.ready && (System.currentTimeMillis()-cTime) < timeout){
            synchronized (SYNC){
                try {
                    SYNC.wait(100);
                } catch (InterruptedException e) {}
            }
        }

        return listener.account;
    }

    private GrapheneAccount getAccountInfoByName(String grapheneName){
        final Object SYNC = new Object();
        long timeout = 60000;

        AccountIdOrNameListener listener = new AccountIdOrNameListener(SYNC);

        ApiRequest request = new ApiRequest(0, listener);
        GrapheneApiGenerator.getAccountByName(grapheneName,request);

        long cTime = System.currentTimeMillis();

        while(!listener.ready && (System.currentTimeMillis()-cTime) < timeout){
            synchronized (SYNC){
                try {
                    SYNC.wait(100);
                } catch (InterruptedException e) {}
            }
        }

        return listener.account;
    }

    public void refreshAccountTransactions(final long idAccount, Context context){
        final CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        final LiveData<List<CryptoCoinTransaction>> transactions = db.transactionDao().getByIdAccount(idAccount);
        final LiveData<GrapheneAccount> account = null;
        //TODO find account
        int start = transactions.getValue().size();
        int limit = 50;
        int stop = start + limit;

        ApiRequest transactionRequest = new ApiRequest(0, new TransactionRequestListener(start,stop,limit,account.getValue(),db));


        GrapheneApiGenerator.getAccountTransaction(account.getValue().getName(),start,stop,limit,transactionRequest);
    }

    private class TransactionRequestListener implements ApiRequestListener{

        int start;
        int stop;
        int limit;
        GrapheneAccount account;
        CrystalDatabase db;

        public TransactionRequestListener(int start, int stop, int limit, GrapheneAccount account, CrystalDatabase db) {
            this.start = start;
            this.stop = stop;
            this.limit = limit;
            this.account = account;
            this.db = db;
        }

        @Override
        public void success(Object answer, int idPetition) {
            List<HistoricalTransfer> transfers = (List<HistoricalTransfer>) answer ;
            for(HistoricalTransfer transfer : transfers){
                CryptoCoinTransaction transaction = new CryptoCoinTransaction();
                transaction.setAccountId(account.getId());
                transaction.setAmount(transfer.getOperation().getAssetAmount().getAmount().longValue());
                BitsharesAsset currency = null;
                //TODO find currency by symbol db.cryptoCurrencyDao().getBySymbol(transfer.getOperation().getAssetAmount().getAsset().getSymbol())
                if(currency == null){
                    //CryptoCurrency not in database
                    Asset asset = transfer.getOperation().getAssetAmount().getAsset();
                    BitsharesAsset.Type assetType = null;
                    if(asset.getAssetType() == Asset.AssetType.CORE_ASSET){
                        assetType = BitsharesAsset.Type.CORE;
                    }else if(asset.getAssetType() == Asset.AssetType.SMART_COIN){
                        assetType = BitsharesAsset.Type.SMART_COIN;
                    }else if(asset.getAssetType() == Asset.AssetType.PREDICTION_MARKET){
                        assetType = BitsharesAsset.Type.PREDICTION_MARKET;
                    }else if(asset.getAssetType() == Asset.AssetType.UIA){
                        assetType = BitsharesAsset.Type.UIA;
                    }
                    currency = new BitsharesAsset(asset.getSymbol(),asset.getPrecision(),asset.getObjectId(), assetType);
                    db.cryptoCurrencyDao().insertCryptoCurrency(currency);

                }
                transaction.setIdCurrency(currency.getId());
                transaction.setConfirmed(true); //graphene transaction are always confirmed
                //TODO date of the transaction transaction.setDate(transfer.);
                transaction.setFrom(transfer.getOperation().getFrom().getName());
                transaction.setInput(!transfer.getOperation().getFrom().getName().equals(account.getName()));
                transaction.setTo(transfer.getOperation().getTo().getName());
                db.transactionDao().insertTransaction(transaction);
            }
            if(transfers.size()>= limit){
                int newStart= start + limit;
                int newStop= stop + limit;
                ApiRequest transactionRequest = new ApiRequest(newStart/limit, new TransactionRequestListener(newStart,newStop,limit,account,db));
                GrapheneApiGenerator.getAccountTransaction(account.getName(),newStart,newStop,limit,transactionRequest);
            }
        }

        @Override
        public void fail(int idPetition) {

        }
    }

    private class AccountIdOrNameListener implements ApiRequestListener{
        Object SYNC;
        boolean ready = false;

        GrapheneAccount account;

        public AccountIdOrNameListener(Object SYNC) {
            this.SYNC = SYNC;
        }

        @Override
        public void success(Object answer, int idPetition) {
            if(answer instanceof  AccountProperties){
                AccountProperties props = (AccountProperties) answer;
                account = new GrapheneAccount();
                account.setAccountId(props.id);
                account.setName(props.name);
            }

            synchronized (SYNC){
                ready = true;
                SYNC.notifyAll();
            }
        }

        @Override
        public void fail(int idPetition) {
            synchronized (SYNC){
                ready = true;
                SYNC.notifyAll();
            }
        }
    }

}
