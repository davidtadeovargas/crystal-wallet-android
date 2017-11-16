package cy.agorise.crystalwallet.apigenerator;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cy.agorise.crystalwallet.dao.BitsharesAssetDao;
import cy.agorise.crystalwallet.dao.CryptoCoinBalanceDao;
import cy.agorise.crystalwallet.dao.CryptoCurrencyDao;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.manager.BitsharesAccountManager;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.network.WebSocketThread;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.Converter;
import cy.agorise.graphenej.LimitOrder;
import cy.agorise.graphenej.ObjectType;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountBalances;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetAccounts;
import cy.agorise.graphenej.api.GetBlockHeader;
import cy.agorise.graphenej.api.GetKeyReferences;
import cy.agorise.graphenej.api.GetLimitOrders;
import cy.agorise.graphenej.api.GetRelativeAccountHistory;
import cy.agorise.graphenej.api.LookupAssetSymbols;
import cy.agorise.graphenej.api.SubscriptionMessagesHub;
import cy.agorise.graphenej.api.TransactionBroadcastSequence;
import cy.agorise.graphenej.interfaces.NodeErrorListener;
import cy.agorise.graphenej.interfaces.SubscriptionListener;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.AccountBalanceUpdate;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.SubscriptionResponse;
import cy.agorise.graphenej.models.WitnessResponse;


/**
 * This class manage all the api request directly to the Graphene Servers
 * Created by henry on 26/9/2017.
 */

public abstract class GrapheneApiGenerator {

    //TODO network connections
    //TODO make to work with all Graphene type, not only bitshares
    //public static String url = "http://128.0.69.157:8090";
    //private static String equivalentUrl = "http://128.0.69.157:8090";
    public static String url = "wss://bitshares.openledger.info/ws";
    private static String equivalentUrl = "wss://bitshares.openledger.info/ws";


    // The message broker for bitshares
    private static SubscriptionMessagesHub bitsharesSubscriptionHub = new SubscriptionMessagesHub("", "", true, new NodeErrorListener() {
        @Override
        public void onError(BaseResponse.Error error) {
            //TODO subcription hub error
        }
    });

    /**
     * The subscription thread for the real time updates
     */
    private static WebSocketThread subscriptionThread = new WebSocketThread(bitsharesSubscriptionHub,url);
    /**
     * This is used for manager each listener in the subscription thread
     */
    private static HashMap<Long,SubscriptionListener> currentBitsharesListener = new HashMap<>();

    /**
     * Retrieves the data of an account searching by it's id
     *
     * @param accountId The accountId to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountById(String accountId, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccounts(accountId,
                new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if (response.result.getClass() == ArrayList.class) {
                    List list = (List) response.result;
                    if (list.size() > 0) {
                        if (list.get(0).getClass() == AccountProperties.class) {
                            request.getListener().success(list.get(0),request.getId());
                            return;
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
        WebSocketThread thread = new WebSocketThread(new GetKeyReferences(address, true,
                new WitnessResponseListener() {
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
     * @param accountGrapheneId The account id to search
     * @param start The start index of the transaction list
     * @param stop The stop index of the transaction list
     * @param limit the maximun transactions to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountTransaction(String accountGrapheneId, int start, int stop,
                                             int limit, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetRelativeAccountHistory(new UserAccount(accountGrapheneId),
                start, limit, stop, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    request.getListener().success(response.result,request.getId());
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
    public static void getAccountByName(String accountName, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccountByName(accountName,
                new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                AccountProperties accountProperties = (AccountProperties)response.result;
                if(accountProperties == null){
                    request.getListener().fail(request.getId());
                }else{
                    request.getListener().success(accountProperties,request.getId());
                }
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
        WebSocketThread thread = new WebSocketThread(new GetAccountByName(accountName,
                new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    AccountProperties accountProperties = (AccountProperties)response.result;
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

    /**
     * Broadcast a transaction, this is use for sending funds
     *
     * @param transaction The graphene transaction
     * @param feeAsset The feeAseet, this needs only the id of the asset
     * @param request the api request object, to answer this petition
     */
    public static void broadcastTransaction(Transaction transaction, Asset feeAsset,
                                            final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new TransactionBroadcastSequence(transaction,
                feeAsset, new WitnessResponseListener() {
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

    /**
     * This gets the asset information using only the asset name
     *
     * @param assetNames The list of the names of the assets to be retrieve
     * @param request the api request object, to answer this petition
     */
    public static void getAssetByName(ArrayList<String> assetNames, final ApiRequest request){

        WebSocketThread thread = new WebSocketThread(new LookupAssetSymbols(assetNames,true,
                new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<Asset> assets = (List<Asset>) response.result;
                if(assets.size() <= 0){
                    request.getListener().fail(request.getId());
                }else{
                    ArrayList<BitsharesAsset> responseAssets = new ArrayList<>();
                    for(Asset asset: assets){
                        //TODO asset type
                        BitsharesAsset.Type assetType = BitsharesAsset.Type.UIA;
                        /*if(asset.getAssetType().equals(Asset.AssetType.CORE_ASSET)){
                            assetType = BitsharesAsset.Type.CORE;
                        }else if(asset.getAssetType().equals(Asset.AssetType.SMART_COIN)){
                            assetType = BitsharesAsset.Type.SMART_COIN;
                        }else if(asset.getAssetType().equals(Asset.AssetType.UIA)){
                            assetType = BitsharesAsset.Type.UIA;
                        }else if(asset.getAssetType().equals(Asset.AssetType.PREDICTION_MARKET)){
                            assetType = BitsharesAsset.Type.PREDICTION_MARKET;
                        }*/
                        BitsharesAsset responseAsset = new BitsharesAsset(asset.getSymbol(),
                                asset.getPrecision(),asset.getObjectId(),assetType);
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

    /**
     * Gets the asset ifnormation using the id of the net
     * @param assetIds The list of the ids to retrieve
     * @param request the api request object, to answer this petition
     */
    public static void getAssetById(ArrayList<String> assetIds, final ApiRequest request){
        ArrayList<Asset> assets = new ArrayList<>();
        for(String assetId : assetIds){
            Asset asset = new Asset(assetId);
            assets.add(asset);
        }

        WebSocketThread thread = new WebSocketThread(new LookupAssetSymbols(assets,true, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<Asset> assets = (List<Asset>) response.result;
                if(assets.size() <= 0){
                    request.getListener().fail(request.getId());
                }else{
                    ArrayList<BitsharesAsset> responseAssets = new ArrayList<>();
                    for(Asset asset: assets){
                        //TODO asset type
                        BitsharesAsset.Type assetType = BitsharesAsset.Type.UIA;
                        /*if(asset.getAssetType().equals(Asset.AssetType.CORE_ASSET)){
                            assetType = BitsharesAsset.Type.CORE;
                        }else if(asset.getAssetType().equals(Asset.AssetType.SMART_COIN)){
                            assetType = BitsharesAsset.Type.SMART_COIN;
                        }else if(asset.getAssetType().equals(Asset.AssetType.UIA)){
                            assetType = BitsharesAsset.Type.UIA;
                        }else if(asset.getAssetType().equals(Asset.AssetType.PREDICTION_MARKET)){
                            assetType = BitsharesAsset.Type.PREDICTION_MARKET;
                        }*/
                        BitsharesAsset responseAsset = new BitsharesAsset(asset.getSymbol(),
                                asset.getPrecision(),asset.getObjectId(),assetType);
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

    /**
     * Subscribe a bitshares account to receive real time updates
     *
     * @param accountId The id opf the database of the account
     * @param accountBitsharesId  The bitshares id of the account
     * @param context The android context of this application
     */
    public static void subscribeBitsharesAccount(final long accountId, final String accountBitsharesId,
                                                 final Context context){
        if(!currentBitsharesListener.containsKey(accountId)){
        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        final CryptoCoinBalanceDao balanceDao = db.cryptoCoinBalanceDao();
        final BitsharesAssetDao bitsharesAssetDao = db.bitsharesAssetDao();
        final CryptoCurrencyDao cryptoCurrencyDao = db.cryptoCurrencyDao();
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
                            if(balanceUpdate.owner.equals(accountBitsharesId)){
                                final CryptoCoinBalance balance = new CryptoCoinBalance();
                                balance.setAccountId(accountId);
                                balance.setBalance(((AccountBalanceUpdate) update).balance);
                                BitsharesAssetInfo assetInfo = bitsharesAssetDao.getBitsharesAssetInfoById(((AccountBalanceUpdate) update).asset_type);
                                if(assetInfo == null ){
                                    final String assetType = ((AccountBalanceUpdate) update).asset_type;
                                    ArrayList<String> idAssets = new ArrayList<>();
                                    idAssets.add(assetType);
                                    ApiRequest getAssetRequest = new ApiRequest(1, new ApiRequestListener() {
                                        @Override
                                        public void success(Object answer, int idPetition) {
                                            if(answer instanceof  BitsharesAsset){
                                                BitsharesAssetInfo info = new BitsharesAssetInfo((BitsharesAsset) answer);
                                                long cryptoCurrencyId = cryptoCurrencyDao.insertCryptoCurrency((CryptoCurrency)answer )[0];
                                                info.setCryptoCurrencyId(cryptoCurrencyId);
                                                bitsharesAssetDao.insertBitsharesAssetInfo(info);
                                                balance.setCryptoCurrencyId(cryptoCurrencyId);
                                                balanceDao.insertCryptoCoinBalance(balance);
                                            }
                                        }

                                        @Override
                                        public void fail(int idPetition) {

                                        }
                                    });
                                    getAssetById(idAssets,getAssetRequest);
                                }else {

                                    balance.setCryptoCurrencyId(assetInfo.getCryptoCurrencyId());
                                    balanceDao.insertCryptoCoinBalance(balance);
                                }
                                BitsharesAccountManager.refreshAccountTransactions(accountId,context);

                            }
                        }
                    }

                }
            }
        };

        currentBitsharesListener.put(accountId,balanceListener);
        bitsharesSubscriptionHub.addSubscriptionListener(balanceListener);

        if(!subscriptionThread.isConnected()){
            subscriptionThread.start();
        }else if(!bitsharesSubscriptionHub.isSubscribed()){
            bitsharesSubscriptionHub.resubscribe();
        }
        }
    }

    /**
     * Cancels all bitshares account subscriptions
     */
    public static void cancelBitsharesAccountSubscriptions(){
        bitsharesSubscriptionHub.cancelSubscriptions();
    }

    /**
     * Retrieve the account balance of an account
     *
     * @param accountId The dataabase id of the account
     * @param accountGrapheneId The bitshares id of the account
     * @param context The android context of this application
     */
    public static void getAccountBalance(final long accountId, final String accountGrapheneId,
                                         final Context context){

        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        final CryptoCoinBalanceDao balanceDao = db.cryptoCoinBalanceDao();
        final BitsharesAssetDao bitsharesAssetDao = db.bitsharesAssetDao();
        final CryptoCurrencyDao cryptoCurrencyDao = db.cryptoCurrencyDao();
        WebSocketThread thread = new WebSocketThread(new GetAccountBalances(new UserAccount(accountGrapheneId),
                new ArrayList<Asset>(), new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<AssetAmount> balances = (List<AssetAmount>) response.result;
                for(final AssetAmount balance : balances){
                    final CryptoCoinBalance ccBalance = new CryptoCoinBalance();
                    ccBalance.setAccountId(accountId);
                    ccBalance.setBalance(balance.getAmount().longValue());
                    BitsharesAssetInfo assetInfo = bitsharesAssetDao.getBitsharesAssetInfoById(balance.getAsset().getObjectId());
                    if(assetInfo == null ){
                        ArrayList<String> idAssets = new ArrayList<>();
                        idAssets.add(balance.getAsset().getObjectId());
                        ApiRequest getAssetRequest = new ApiRequest(1, new ApiRequestListener() {
                            @Override
                            public void success(Object answer, int idPetition) {
                                    List<BitsharesAsset> assets = (List<BitsharesAsset>) answer;
                                    for(BitsharesAsset asset : assets) {
                                        BitsharesAssetInfo info = new BitsharesAssetInfo(asset);
                                        long[] cryptoCurrencyId = cryptoCurrencyDao.insertCryptoCurrency((CryptoCurrency) asset);
                                        info.setCryptoCurrencyId(cryptoCurrencyId[0]);
                                        bitsharesAssetDao.insertBitsharesAssetInfo(info);
                                        ccBalance.setCryptoCurrencyId(cryptoCurrencyId[0]);
                                        balanceDao.insertCryptoCoinBalance(ccBalance);
                                    }
                            }

                            @Override
                            public void fail(int idPetition) {
                            }
                        });
                        getAssetById(idAssets,getAssetRequest);

                    }else {

                        ccBalance.setCryptoCurrencyId(assetInfo.getCryptoCurrencyId());
                        balanceDao.insertCryptoCoinBalance(ccBalance);
                    }
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {

            }
        }),url);

        thread.start();

    }

    /**
     * Gets the date time of a block header
     *
     * @param blockHeader The block header to retrieve the date time
     * @param request the api request object, to answer this petition
     */
    public static void getBlockHeaderTime(long blockHeader, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetBlockHeader(blockHeader, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if(response == null){
                    request.getListener().fail(request.getId());
                }else {
                    request.getListener().success(response.result, request.getId());
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();

    }

    /**
     * Gets a single equivalent value
     *
     * @param baseId The base asset bistshares id
     * @param quoteId the quote asset bitshares id
     * @param request the api request object, to answer this petition
     */
    public static void getEquivalentValue(final String baseId, String quoteId, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetLimitOrders(baseId, quoteId, 10,
                new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<LimitOrder> orders = (List<LimitOrder>) response.result;
                if(orders.size()<= 0){
                    //TODO indirect equivalent value
                }
                for(LimitOrder order : orders){
                    if(order.getSellPrice().base.getAsset().getBitassetId().equals(baseId)) {
                        Converter converter = new Converter();
                        double equiValue = converter.getConversionRate(order.getSellPrice(),
                                Converter.BASE_TO_QUOTE);
                        request.getListener().success(equiValue, request.getId());
                        break;
                    }
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),equivalentUrl);
        thread.start();
    }

    /**
     * Gets equivalent value and store it on the database
     *
     * @param baseAsset The baset asset as a bitshares asset, it needs the CryptoCurrency and thge BitsharesInfo
     * @param quoteAssets The list of the qutoe assets as a full bitshares asset object
     * @param context The android context of this application
     */
    public static void getEquivalentValue(BitsharesAsset baseAsset,
                                          final List<BitsharesAsset> quoteAssets, final Context context){
            for(BitsharesAsset quoteAsset : quoteAssets){
                WebSocketThread thread = new WebSocketThread(new GetLimitOrders(baseAsset.getBitsharesId(),
                        quoteAsset.getBitsharesId(), 10, new EquivalentValueListener(baseAsset,
                        quoteAsset,context)),equivalentUrl);
                thread.start();
            }
    }

    public static void getEquivalenValue(String baseAssetName, final  List<BitsharesAsset> quoteAssets, final Context context){
        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        final CryptoCurrencyDao cryptoCurrencyDao = db.cryptoCurrencyDao();
        final BitsharesAssetDao bitsharesAssetDao = db.bitsharesAssetDao();
        CryptoCurrency baseCurrency = cryptoCurrencyDao.getByName(baseAssetName);
        BitsharesAssetInfo info = null;
        if(baseCurrency != null){
            info = db.bitsharesAssetDao().getBitsharesAssetInfo(baseCurrency.getId());
        }
        if(baseCurrency == null || info == null){
            ApiRequest getAssetRequest = new ApiRequest(1, new ApiRequestListener() {
                @Override
                public void success(Object answer, int idPetition) {
                    if(answer instanceof  BitsharesAsset){
                        BitsharesAssetInfo info = new BitsharesAssetInfo((BitsharesAsset) answer);
                        long cryptoCurrencyId = cryptoCurrencyDao.insertCryptoCurrency((CryptoCurrency)answer )[0];
                        info.setCryptoCurrencyId(cryptoCurrencyId);
                        bitsharesAssetDao.insertBitsharesAssetInfo(info);
                        GrapheneApiGenerator.getEquivalentValue((BitsharesAsset) answer, quoteAssets, context);
                    }
                }

                @Override
                public void fail(int idPetition) {
                //TODO fail asset petition, the base asset is not an asset in bitshares, or there is no connection to the server
                }
            });
            ArrayList<String> names = new ArrayList<>();
            names.add(baseAssetName);
            GrapheneApiGenerator.getAssetByName(names,getAssetRequest);

        }else {
            BitsharesAsset baseAsset = new BitsharesAsset(baseCurrency);
            baseAsset.loadInfo(info);
            getEquivalentValue(baseAsset,quoteAssets,context);
        }


    }

    /**
     * Listener of the equivalent value the answer is stored in the database, for use in conjuntion with LiveData
     */
    private static class EquivalentValueListener implements WitnessResponseListener{
        /**
         * The base asset
         */
        private BitsharesAsset baseAsset;
        /**
         * The quote asset
         */
        private BitsharesAsset quoteAsset;
        /**
         * The android context of this application
         */
        private Context context;

        public EquivalentValueListener(BitsharesAsset baseAsset, BitsharesAsset quoteAsset, Context context) {
            this.baseAsset = baseAsset;
            this.quoteAsset = quoteAsset;
            this.context = context;
        }

        @Override
        public void onSuccess(WitnessResponse response) {
            List<LimitOrder> orders = (List<LimitOrder>) response.result;
            if(orders.size()<= 0){
                //TODO indirect equivalent value
            }
            for(LimitOrder order : orders){
                try {
                    //if (order.getSellPrice().base.getAsset().getBitassetId().equals(baseAsset.getBitsharesId())) {
                        Converter converter = new Converter();
                        order.getSellPrice().base.getAsset().setPrecision(baseAsset.getPrecision());
                        order.getSellPrice().quote.getAsset().setPrecision(quoteAsset.getPrecision());
                        double equiValue = converter.getConversionRate(order.getSellPrice(), Converter.BASE_TO_QUOTE);
                        CryptoCurrencyEquivalence equivalence = new CryptoCurrencyEquivalence(baseAsset.getId(), quoteAsset.getId(), (int) (Math.pow(10, baseAsset.getPrecision()) * equiValue), new Date());
                        CrystalDatabase.getAppDatabase(context).cryptoCurrencyEquivalenceDao().insertCryptoCurrencyEquivalence(equivalence);
                        break;
                    //}
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(BaseResponse.Error error) {

        }
    }

}
