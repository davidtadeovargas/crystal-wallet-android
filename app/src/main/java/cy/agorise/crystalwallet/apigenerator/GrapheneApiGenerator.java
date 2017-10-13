package cy.agorise.crystalwallet.apigenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.network.WebSocketThread;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.ObjectType;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetAccounts;
import cy.agorise.graphenej.api.GetKeyReferences;
import cy.agorise.graphenej.api.GetRelativeAccountHistory;
import cy.agorise.graphenej.api.ListAssets;
import cy.agorise.graphenej.api.LookupAssetSymbols;
import cy.agorise.graphenej.api.SubscriptionMessagesHub;
import cy.agorise.graphenej.api.TransactionBroadcastSequence;
import cy.agorise.graphenej.interfaces.NodeErrorListener;
import cy.agorise.graphenej.interfaces.SubscriptionListener;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.AccountBalanceUpdate;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.BroadcastedTransaction;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.SubscriptionResponse;
import cy.agorise.graphenej.models.WitnessResponse;


/**
 * This class manage all the api request directly to the Graphene Servers
 * Created by henry on 26/9/2017.
 */

public class GrapheneApiGenerator {

    //TODO network connections
    //TODO make to work with all Graphene stype, not only bitshares
    private static String url = "http://128.0.69.157:8090";

    // The meesage broker for bitshares
    private static SubscriptionMessagesHub bitsharesSubscriptionHub = new SubscriptionMessagesHub("", "", true, new NodeErrorListener() {
        @Override
        public void onError(BaseResponse.Error error) {
            //TODO subcription hub error
        }
    });

    private static WebSocketThread subscriptionThread = new WebSocketThread(bitsharesSubscriptionHub,url);

    /**
     * Retrieves the data of an account searching by it's id
     *
     * @param accountId The accountId to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountById(String accountId, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccounts(accountId, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if (response.result.getClass() == ArrayList.class) {
                    List list = (List) response.result;
                    if (list.size() > 0) {
                        if (list.get(0).getClass() == AccountProperties.class) {
                            request.getListener().success(list.get(0),request.getId());
                            return;
                            //TODO answer a crystal model
                        }
                    }
                }
                request.getListener().fail(request.getId());
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();
    }

    /**
     * Gets the account ID from an owner or active key
     *
     * @param address The address to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountByOwnerOrActiveAddress(Address address, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetKeyReferences(address, true, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    final List<List<UserAccount>> resp = (List<List<UserAccount>>) response.result;
                    if(resp.size() > 0){
                        List<UserAccount> accounts = resp.get(0);
                        if(accounts.size() > 0){
                            for(UserAccount account : accounts) {
                                request.getListener().success(account,request.getId());}}}
                    request.getListener().fail(request.getId());
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);

            thread.start();
    }

    /**
     * Gets the Transaction for an Account
     *
     * @param accountId The account id to search
     * @param start The start index of the transaction list
     * @param stop The stop index of the transaction list
     * @param limit the maximun transactions to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountTransaction(String accountId, int start, int stop, int limit, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetRelativeAccountHistory(new UserAccount(accountId), stop, limit, start, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    WitnessResponse<List<HistoricalTransfer>> resp = response;
                    request.getListener().success(resp,request.getId());
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);
            thread.start();
    }

    /**
     * Retrieves the account id by the name of the account
     *
     * @param accountName The account Name to find
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountIdByName(String accountName, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccountByName(accountName, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    AccountProperties accountProperties = ((WitnessResponse<AccountProperties>) response).result;
                    if(accountProperties == null){
                        request.getListener().success(null,request.getId());
                    }else{
                        request.getListener().success(accountProperties.id,request.getId());
                    }
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);
        thread.start();
    }

    public static void broadcastTransaction(Transaction transaction, Asset feeAsset, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new TransactionBroadcastSequence(transaction, feeAsset, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                request.getListener().success(true,request.getId());
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();
    }

    public static void getAssetByName(ArrayList<String> assetNames, final ApiRequest request){
        //TODO the graphenej library needs a way to creates the Asset object only with the symbol
        ArrayList<Asset> assets = new ArrayList<>();
        for(String assetName : assetNames){
            Asset asset = new Asset("",assetName,-1);
            assets.add(asset);
        }
        //TODO the graphenj library needs to add the lookupAssetSymbols to be able to search the asset by symbol
        // this can be done with the same lookupassetsymbol, but passing only the symbol not the objetcid
        WebSocketThread thread = new WebSocketThread(new LookupAssetSymbols(assets, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<Asset> assets = (List<Asset>) response.result;
                if(assets.size() <= 0){
                    request.getListener().fail(request.getId());
                }else{
                    ArrayList<BitsharesAsset> responseAssets = new ArrayList<>();
                    for(Asset asset: assets){
                        BitsharesAsset.Type assetType = null;
                        if(asset.getAssetType().equals(Asset.AssetType.CORE_ASSET)){
                            assetType = BitsharesAsset.Type.CORE;
                        }else if(asset.getAssetType().equals(Asset.AssetType.SMART_COIN)){
                            assetType = BitsharesAsset.Type.SMART_COIN;
                        }else if(asset.getAssetType().equals(Asset.AssetType.UIA)){
                            assetType = BitsharesAsset.Type.UIA;
                        }else if(asset.getAssetType().equals(Asset.AssetType.PREDICTION_MARKET)){
                            assetType = BitsharesAsset.Type.PREDICTION_MARKET;
                        }
                        BitsharesAsset responseAsset = new BitsharesAsset(asset.getSymbol(),asset.getPrecision(),asset.getObjectId(),assetType);
                        responseAssets.add(responseAsset);
                    }
                    request.getListener().success(responseAssets,request.getId());
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();
    }

    public static void getAssetById(ArrayList<String> assetIds, final ApiRequest request){
        //TODO the graphenej library needs a way to creates the Asset object only with the symbol
        ArrayList<Asset> assets = new ArrayList<>();
        for(String assetId : assetIds){
            Asset asset = new Asset(assetId);
            assets.add(asset);
        }
        //TODO the graphenj library needs to add the lookupAssetSymbols to be able to search the asset by symbol
        // this can be done with the same lookupassetsymbol, but passing only the symbol not the objetcid
        WebSocketThread thread = new WebSocketThread(new LookupAssetSymbols(assets, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<Asset> assets = (List<Asset>) response.result;
                if(assets.size() <= 0){
                    request.getListener().fail(request.getId());
                }else{
                    ArrayList<BitsharesAsset> responseAssets = new ArrayList<>();
                    for(Asset asset: assets){
                        BitsharesAsset.Type assetType = null;
                        if(asset.getAssetType().equals(Asset.AssetType.CORE_ASSET)){
                            assetType = BitsharesAsset.Type.CORE;
                        }else if(asset.getAssetType().equals(Asset.AssetType.SMART_COIN)){
                            assetType = BitsharesAsset.Type.SMART_COIN;
                        }else if(asset.getAssetType().equals(Asset.AssetType.UIA)){
                            assetType = BitsharesAsset.Type.UIA;
                        }else if(asset.getAssetType().equals(Asset.AssetType.PREDICTION_MARKET)){
                            assetType = BitsharesAsset.Type.PREDICTION_MARKET;
                        }
                        BitsharesAsset responseAsset = new BitsharesAsset(asset.getSymbol(),asset.getPrecision(),asset.getObjectId(),assetType);
                        responseAssets.add(responseAsset);
                    }
                    request.getListener().success(responseAssets,request.getId());
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();
    }

    public static void subscribeBitsharesAccount(final String accountId){
        SubscriptionListener balanceListener = new SubscriptionListener() {
            @Override
            public ObjectType getInterestObjectType() {
                return ObjectType.BALANCE_OBJECT;
            }

            @Override
            public void onSubscriptionUpdate(SubscriptionResponse response) {
                List<Serializable> updatedObjects = (List<Serializable>) response.params.get(1);
                if(updatedObjects.size() > 0){
                    for(Serializable update : updatedObjects){
                        if(update instanceof AccountBalanceUpdate){
                            AccountBalanceUpdate balanceUpdate = (AccountBalanceUpdate) update;
                            if(balanceUpdate.owner.equals(accountId)){
                                //TODO balance function
                                //TODO refresh transactions
                            }
                        }
                    }
                }
            }
        };

        bitsharesSubscriptionHub.addSubscriptionListener(balanceListener);

        if(!subscriptionThread.isConnected()){
            subscriptionThread.start();
        }else if(!bitsharesSubscriptionHub.isSubscribed()){
            bitsharesSubscriptionHub.resubscribe();
        }
    }

    public static void cancelBitsharesAccountSubcriptions(){
        bitsharesSubscriptionHub.cancelSubscriptions();
    }

}
