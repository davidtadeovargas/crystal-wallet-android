package cy.agorise.crystalwallet.manager;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Context;

import com.google.common.primitives.UnsignedLong;

import org.bitcoinj.core.ECKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cy.agorise.crystalwallet.apigenerator.ApiRequest;
import cy.agorise.crystalwallet.apigenerator.ApiRequestListener;
import cy.agorise.crystalwallet.apigenerator.BitsharesFaucetApiGenerator;
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.application.constant.BitsharesConstant;
import cy.agorise.crystalwallet.models.seed.BIP39;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEquivalentRequest;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequest;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestsListener;
import cy.agorise.crystalwallet.requestmanagers.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.requestmanagers.ValidateCreateBitsharesAccountRequest;
import cy.agorise.crystalwallet.requestmanagers.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.requestmanagers.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.dao.TransactionDao;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;
import cy.agorise.crystalwallet.network.CryptoNetManager;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BlockHeader;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.operations.TransferOperationBuilder;

/**
 * The manager for the Bitshare CryptoCoin
 *
 * Created by henry on 26/9/2017.
 */
public class BitsharesAccountManager implements CryptoAccountManager, CryptoNetInfoRequestsListener {

    //private final static String BITSHARES_TESTNET_CHAIN_ID= "9cf6f255a208100d2bb275a3c52f4b1589b7ec9c9bfc2cb2a5fe6411295106d8";

    private final static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String DEFAULT_TIME_ZONE = "GMT";

    @Override
    public void createAccountFromSeed(CryptoNetAccount account, final ManagerRequest request, final Context context) {
        if(account instanceof  GrapheneAccount) {

            final GrapheneAccount grapheneAccount = (GrapheneAccount) account;
            ApiRequest creationRequest = new ApiRequest(1, new ApiRequestListener() {
                @Override
                public void success(Object answer, int idPetition) {
                    getAccountInfoByName(grapheneAccount.getName(), new ManagerRequest() {
                        @Override
                        public void success(Object answer) {
                            GrapheneAccount fetch = (GrapheneAccount) answer;
                            fetch.setSeedId(grapheneAccount.getSeedId());
                            fetch.setCryptoNet(grapheneAccount.getCryptoNet());
                            fetch.setAccountIndex(grapheneAccount.getAccountIndex());

                            CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
                            long idAccount = db.cryptoNetAccountDao().insertCryptoNetAccount(fetch)[0];
                            fetch.setId(idAccount);
                            db.grapheneAccountInfoDao().insertGrapheneAccountInfo(new GrapheneAccountInfo(fetch));
                            subscribeBitsharesAccount(fetch.getId(),fetch.getAccountId(),context);
                            request.success(fetch);
                        }

                        @Override
                        public void fail() {
                            //TODO get account data fail
                        }
                    });

                }

                @Override
                public void fail(int idPetition) {
                    request.fail();
                }
            });
            BitsharesFaucetApiGenerator.registerBitsharesAccount(grapheneAccount.getName(),
                    new Address(ECKey.fromPublicOnly(grapheneAccount.getOwnerKey(context).getPubKey())).toString(),
                    new Address(ECKey.fromPublicOnly(grapheneAccount.getActiveKey(context).getPubKey())).toString(),
                    new Address(ECKey.fromPublicOnly(grapheneAccount.getMemoKey(context).getPubKey())).toString(),
                    BitsharesConstant.FAUCET_URL, creationRequest);
        }
    }

    @Override
    public void importAccountFromSeed(CryptoNetAccount account, final Context context) {
        if(account instanceof GrapheneAccount) {
            final GrapheneAccount grapheneAccount = (GrapheneAccount) account;

            if(grapheneAccount.getAccountId() == null){
                 this.getAccountInfoByName(grapheneAccount.getName(), new ManagerRequest() {
                    @Override
                    public void success(Object answer) {
                        GrapheneAccount fetch = (GrapheneAccount) answer;
                        grapheneAccount.setAccountId(fetch.getAccountId());
                        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
                        long[] idAccount = db.cryptoNetAccountDao().insertCryptoNetAccount(grapheneAccount);
                        grapheneAccount.setId(idAccount[0]);
                        db.grapheneAccountInfoDao().insertGrapheneAccountInfo(new GrapheneAccountInfo(grapheneAccount));
                        subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
                    }

                    @Override
                    public void fail() {
                        //TODO get account data fail
                    }
                });

            }else if(grapheneAccount.getName() == null){
                this.getAccountInfoById(grapheneAccount.getAccountId(), new ManagerRequest() {
                    @Override
                    public void success(Object answer) {
                        GrapheneAccount fetch = (GrapheneAccount) answer;
                        grapheneAccount.setName(fetch.getName());
                        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
                        db.cryptoNetAccountDao().insertCryptoNetAccount(grapheneAccount);
                        db.grapheneAccountInfoDao().insertGrapheneAccountInfo(new GrapheneAccountInfo(grapheneAccount));
                        subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
                    }

                    @Override
                    public void fail() {
                        //TODO get account data fail
                    }
                });
            }else {
                CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
                db.cryptoNetAccountDao().insertCryptoNetAccount(grapheneAccount);
                db.grapheneAccountInfoDao().insertGrapheneAccountInfo(new GrapheneAccountInfo(grapheneAccount));
                subscribeBitsharesAccount(grapheneAccount.getId(), grapheneAccount.getAccountId(), context);
            }
        }
    }

    @Override
    public void loadAccountFromDB(CryptoNetAccount account, final Context context) {
        if(account instanceof GrapheneAccount){
            final GrapheneAccount grapheneAccount = (GrapheneAccount) account;
            final CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
            final GrapheneAccountInfo info = db.grapheneAccountInfoDao().getByAccountId(account.getId());
            grapheneAccount.loadInfo(info);
            if(grapheneAccount.getAccountId() == null){
                this.getAccountInfoByName(grapheneAccount.getName(), new ManagerRequest() {
                    @Override
                    public void success(Object answer) {
                        GrapheneAccount fetch = (GrapheneAccount) answer;
                        info.setAccountId(fetch.getAccountId());
                        grapheneAccount.setAccountId(fetch.getAccountId());
                        db.grapheneAccountInfoDao().insertGrapheneAccountInfo(info);
                        subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
                    }

                    @Override
                    public void fail() {
                        //TODO account data retrieve failed
                    }
                });
            }else if(grapheneAccount.getName() == null){
                this.getAccountInfoById(grapheneAccount.getAccountId(), new ManagerRequest() {
                    @Override
                    public void success(Object answer) {
                        GrapheneAccount fetch = (GrapheneAccount) answer;
                        info.setName(fetch.getName());
                        grapheneAccount.setName(fetch.getName());
                        db.grapheneAccountInfoDao().insertGrapheneAccountInfo(info);
                        subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
                    }

                    @Override
                    public void fail() {
                        //TODO account data retrieve failed
                    }
                });
            }else{
                subscribeBitsharesAccount(grapheneAccount.getId(),grapheneAccount.getAccountId(),context);
            }
        }
    }

    private void subscribeBitsharesAccount(long accountId, String accountBitsharesID, Context context){
        GrapheneApiGenerator.subscribeBitsharesAccount(accountId,accountBitsharesID,context);
        BitsharesAccountManager.refreshAccountTransactions(accountId,context);
        GrapheneApiGenerator.getAccountBalance(accountId,accountBitsharesID,context);
    }

    /**
     * Process the bitshares manager request
     * @param request The request Object
     */
    @Override
    public void onNewRequest(CryptoNetInfoRequest request) {
        if (request instanceof ValidateImportBitsharesAccountRequest){
            this.validateImportAccount((ValidateImportBitsharesAccountRequest) request);
        } else if (request instanceof ValidateExistBitsharesAccountRequest){
            this.validateExistAcccount((ValidateExistBitsharesAccountRequest) request);
        } else if (request instanceof ValidateBitsharesSendRequest){
            this.validateSendRequest((ValidateBitsharesSendRequest) request);
        }else if (request instanceof CryptoNetEquivalentRequest){
            this.getEquivalentValue((CryptoNetEquivalentRequest) request);
        }else if (request instanceof ValidateCreateBitsharesAccountRequest){
            this.validateCreateAccount((ValidateCreateBitsharesAccountRequest) request);
        }else{
            //TODO not implemented
            System.out.println("Error request not implemented " + request.getClass().getName());
        }
    }

    /**
     * Process the import account request
     */
    private void validateImportAccount(final ValidateImportBitsharesAccountRequest importRequest){
        //TODO check internet and server status
        ApiRequest checkAccountName = new ApiRequest(0, new ApiRequestListener() {
            @Override
            public void success(Object answer, int idPetition) {
                ApiRequest getAccountInfo = new ApiRequest(1,new ApiRequestListener(){
                    @Override
                    public void success(Object answer, int idPetition) {
                        if(answer != null && answer instanceof AccountProperties) {
                            AccountProperties prop = (AccountProperties) answer;
                            BrainKey bk = new BrainKey(importRequest.getMnemonic(), 0);
                            for(PublicKey activeKey : prop.owner.getKeyAuthList()){
                                if((new Address(activeKey.getKey(),"BTS")).toString().equals(bk.getPublicAddress("BTS").toString())){
                                    importRequest.setSeedType(SeedType.BRAINKEY);
                                    importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.SUCCEEDED);
                                    return;
                                }
                            }
                            BIP39 bip39 = new BIP39(-1, importRequest.getMnemonic());
                            for(PublicKey activeKey : prop.active.getKeyAuthList()){
                                if((new Address(activeKey.getKey(),"BTS")).toString().equals(new Address(ECKey.fromPublicOnly(bip39.getBitsharesActiveKey(0).getPubKey())).toString())){
                                    importRequest.setSeedType(SeedType.BIP39);
                                    importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.SUCCEEDED);
                                    return;
                                }
                            }
                            importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.BAD_SEED);
                        }
                        importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.PETITION_FAILED);

                    }

                    @Override
                    public void fail(int idPetition) {
                        //
                        importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.NO_ACCOUNT_DATA);
                    }
                });
                GrapheneApiGenerator.getAccountById((String)answer,getAccountInfo);
            }

            @Override
            public void fail(int idPetition) {
                //
                importRequest.setStatus(ValidateImportBitsharesAccountRequest.StatusCode.ACCOUNT_DOESNT_EXIST);
            }
        });

        GrapheneApiGenerator.getAccountIdByName(importRequest.getAccountName(),checkAccountName);
    }

    private void validateCreateAccount(final ValidateCreateBitsharesAccountRequest createRequest){
        //TODO test for internet and server connection
        // Generate seed or find key
        Context context = createRequest.getContext();
        AccountSeed seed = AccountSeed.getAccountSeed(SeedType.BIP39, context);
        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        long idSeed = db.accountSeedDao().insertAccountSeed(seed);
        assert seed != null;
        seed.setId(idSeed);
        seed.setName(createRequest.getAccountName());
        GrapheneAccount account = new GrapheneAccount();
        account.setName(createRequest.getAccountName());
        account.setSeedId(idSeed);
            account.setAccountIndex(0);
            account.setCryptoNet(CryptoNet.BITSHARES);
            this.createAccountFromSeed(account, new ManagerRequest() {

                @Override
                public void success(Object answer) {
                    createRequest.setAccount((GrapheneAccount) answer);
                    createRequest.setStatus(ValidateCreateBitsharesAccountRequest.StatusCode.SUCCEEDED);
                }

                @Override
                public void fail() {
                    createRequest.setStatus(ValidateCreateBitsharesAccountRequest.StatusCode.ACCOUNT_EXIST);
                }
            }, context);


    }

    /**
     * Process the account exist request, it consults the bitshares api for the account name.
     *
     * This can be used to know if the name is avaible, or the account to be send fund exists
     */
    private void validateExistAcccount(final ValidateExistBitsharesAccountRequest validateRequest){
        ApiRequest checkAccountName = new ApiRequest(0, new ApiRequestListener() {
            @Override
            public void success(Object answer, int idPetition) {
                if (answer != null) {
                    validateRequest.setAccountExists(true);
                } else {
                    validateRequest.setAccountExists(false);
                }
            }

            @Override
            public void fail(int idPetition) {
                //TODO verified
                validateRequest.setAccountExists(false);
            }
        });
        GrapheneApiGenerator.getAccountIdByName(validateRequest.getAccountName(),checkAccountName);
    }

    /**
     * Broadcast a transaction request
     */
    private void validateSendRequest(final ValidateBitsharesSendRequest sendRequest) {
        //TODO check internet, server connection
        //TODO feeAsset
        CrystalDatabase db = CrystalDatabase.getAppDatabase(sendRequest.getContext());
        CryptoCurrency currency = db.cryptoCurrencyDao().getByNameAndCryptoNet(sendRequest.getAsset(), CryptoNet.BITSHARES.name());
        if (currency == null){
            getAssetInfoByName(sendRequest.getAsset(), new ManagerRequest() {
                @Override
                public void success(Object answer) {
                    validateSendRequest(sendRequest, ((BitsharesAsset) answer).getBitsharesId());
                }

                @Override
                public void fail() {
                    sendRequest.setStatus(ValidateBitsharesSendRequest.StatusCode.NO_ASSET_INFO_DB);
                }
            });
        }else{
            BitsharesAssetInfo info = db.bitsharesAssetDao().getBitsharesAssetInfo(currency.getId());
            if (info == null || info.getBitsharesId() == null || info.getBitsharesId().isEmpty()){
                getAssetInfoByName(sendRequest.getAsset(), new ManagerRequest() {
                    @Override
                    public void success(Object answer) {
                        validateSendRequest(sendRequest, ((BitsharesAsset) answer).getBitsharesId());
                    }

                    @Override
                    public void fail() {
                        sendRequest.setStatus(ValidateBitsharesSendRequest.StatusCode.NO_ASSET_INFO);
                    }
                });
            }else {
                this.validateSendRequest(sendRequest, info.getBitsharesId());
            }
        }
    }

    private void validateSendRequest(final ValidateBitsharesSendRequest sendRequest , final String idAsset){
        final Asset feeAsset = new Asset(idAsset);
        final UserAccount fromUserAccount =new UserAccount(sendRequest.getSourceAccount().getAccountId());

        //TODO cached to accounts
        this.getAccountInfoByName(sendRequest.getToAccount(), new ManagerRequest() {

            @Override
            public void success(Object answer) {
                GrapheneAccount toUserGrapheneAccount = (GrapheneAccount) answer;
                UserAccount toUserAccount = new UserAccount(toUserGrapheneAccount.getAccountId());
                TransferOperationBuilder builder = new TransferOperationBuilder()
                        .setSource(fromUserAccount)
                        .setDestination(toUserAccount)
                        .setTransferAmount(new AssetAmount(UnsignedLong.valueOf(sendRequest.getAmount()), new Asset(idAsset)))
                        .setFee(new AssetAmount(UnsignedLong.valueOf(0), feeAsset));
                if(sendRequest.getMemo() != null) {
                    //builder.setMemo(new Memo(fromUserAccount,toUserAccount,0,sendRequest.getMemo().getBytes()));
                    //TODO memo
                    System.out.println("transaction has memo");
                }
                ArrayList<BaseOperation> operationList = new ArrayList<>();
                operationList.add(builder.build());

                ECKey privateKey = sendRequest.getSourceAccount().getActiveKey(sendRequest.getContext());

                Transaction transaction = new Transaction(privateKey, null, operationList);
                transaction.setChainId(CryptoNetManager.getChaindId(CryptoNet.BITSHARES));

                ApiRequest transactionRequest = new ApiRequest(0, new ApiRequestListener() {
                    @Override
                    public void success(Object answer, int idPetition) {
                        sendRequest.setStatus(ValidateBitsharesSendRequest.StatusCode.SUCCEEDED);
                    }

                    @Override
                    public void fail(int idPetition) {
                        sendRequest.setStatus(ValidateBitsharesSendRequest.StatusCode.PETITION_FAILED);
                    }
                });

                GrapheneApiGenerator.broadcastTransaction(transaction,feeAsset, transactionRequest);
            }

            @Override
            public void fail() {
                sendRequest.setStatus(ValidateBitsharesSendRequest.StatusCode.NO_TO_USER_INFO);
            }
        });

    }

    /**
     * Returns the account info from a graphene id
     * @param grapheneId The graphene id of the account
     */
    private void getAccountInfoById(String grapheneId, ManagerRequest request){

        AccountIdOrNameListener listener = new AccountIdOrNameListener(request);

        ApiRequest accountRequest = new ApiRequest(0, listener);
        GrapheneApiGenerator.getAccountById(grapheneId,accountRequest);
    }

    /**
     * Gets account info by its name
     * @param grapheneName The name of the account to retrieve
     */
    private void getAccountInfoByName(String grapheneName, ManagerRequest request){

        AccountIdOrNameListener listener = new AccountIdOrNameListener(request);

        ApiRequest accountRequest = new ApiRequest(0, listener);
        GrapheneApiGenerator.getAccountByName(grapheneName,accountRequest);

    }

    //TODO expand function to be more generic
    private void getAssetInfoByName(String assetName, ManagerRequest request){

        AssetIdOrNameListener nameListener = new AssetIdOrNameListener(request);
        ApiRequest assetRequest = new ApiRequest(0, nameListener);
        ArrayList<String> assetNames = new ArrayList<>();
        assetNames.add(assetName);
        GrapheneApiGenerator.getAssetByName(assetNames, assetRequest);

    }

    /**
     * Refresh the transactions of an account, important to notice, it return nothing, to get the changes tuse the LiveData
     * @param idAccount database id of the account
     * @param context The android context of this application
     */
    private static void refreshAccountTransactions(long idAccount, Context context){
        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        List<CryptoCoinTransaction> transactions = db.transactionDao().getByIdAccount(idAccount);
        CryptoNetAccount account = db.cryptoNetAccountDao().getById(idAccount);
        if(account.getCryptoNet() == CryptoNet.BITSHARES) {

            GrapheneAccount grapheneAccount = new GrapheneAccount(account);
            grapheneAccount.loadInfo(db.grapheneAccountInfoDao().getByAccountId(idAccount));

            int start = transactions.size();
            int limit = 50;
            int stop = start + limit;

            ApiRequest transactionRequest = new ApiRequest(0, new TransactionRequestListener(start, stop, limit, grapheneAccount, db));
            GrapheneApiGenerator.getAccountTransaction(grapheneAccount.getAccountId(), start, stop, limit, transactionRequest);
        }
    }

    /**
     * Class that handles the transactions request
     */
    private static class TransactionRequestListener implements ApiRequestListener{

        /**
         * Start index
         */
        int start;
        /**
         * End index
         */
        int stop;
        /**
         * Limit of transasction to fetch
         */
        int limit;
        /**
         * The grapheneaccount with all data CryptoCurrnecy + info
         */
        GrapheneAccount account;
        /**
         * The database
         */
        CrystalDatabase db;

        /**
         * Basic consturctor
         */
        TransactionRequestListener(int start, int stop, int limit, GrapheneAccount account, CrystalDatabase db) {
            this.start = start;
            this.stop = stop;
            this.limit = limit;
            this.account = account;
            this.db = db;
        }

        /**
         * Handles the success request of the transaction, if the amount of transaction is equal to the limit, ask for more transaction
         * @param answer The answer, this object depends on the kind of request is made to the api
         * @param idPetition the id of the ApiRequest petition
         */
        @Override
        public void success(Object answer, int idPetition) {
            List<HistoricalTransfer> transfers = (List<HistoricalTransfer>) answer ;
            for(final HistoricalTransfer transfer : transfers) {
                if (transfer.getOperation() != null){
                    final CryptoCoinTransaction transaction = new CryptoCoinTransaction();
                    transaction.setAccountId(account.getId());
                    transaction.setAmount(transfer.getOperation().getAssetAmount().getAmount().longValue());
                    BitsharesAssetInfo info = db.bitsharesAssetDao().getBitsharesAssetInfoById(transfer.getOperation().getAssetAmount().getAsset().getObjectId());

                if (info == null) {
                    //The cryptoCurrency is not in the database, queringfor its data
                    ApiRequest assetRequest = new ApiRequest(0, new ApiRequestListener() {
                        @Override
                        public void success(Object answer, int idPetition) {
                            ArrayList<BitsharesAsset> assets = (ArrayList<BitsharesAsset>) answer;
                            for(BitsharesAsset asset : assets){
                                long idCryptoCurrency = db.cryptoCurrencyDao().insertCryptoCurrency(asset)[0];
                                BitsharesAssetInfo info = new BitsharesAssetInfo(asset);
                                info.setCryptoCurrencyId(idCryptoCurrency);
                                asset.setId((int)idCryptoCurrency);
                                db.bitsharesAssetDao().insertBitsharesAssetInfo(info);
                                saveTransaction(transaction,info,transfer);
                            }

                        }

                        @Override
                        public void fail(int idPetition) {
                            //TODO Error
                        }
                    });
                    ArrayList<String> assets = new ArrayList<>();
                    assets.add(transfer.getOperation().getAssetAmount().getAsset().getObjectId());
                    GrapheneApiGenerator.getAssetById(assets,assetRequest);

                }else{
                    saveTransaction(transaction,info,transfer);
                }

            }
            }
            if(transfers.size()>= limit){
                // The amount of transaction in the answer is equal to the limit, we need to query to see if there is more transactions
                int newStart= start + limit;
                int newStop= stop + limit;
                ApiRequest transactionRequest = new ApiRequest(newStart/limit, new TransactionRequestListener(newStart,newStop,limit,account,db));
                GrapheneApiGenerator.getAccountTransaction(account.getAccountId(),newStart,newStop,limit,transactionRequest);
            }
        }

        @Override
        public void fail(int idPetition) {

        }

        private void saveTransaction(CryptoCoinTransaction transaction, BitsharesAssetInfo info, HistoricalTransfer transfer){
            transaction.setIdCurrency((int)info.getCryptoCurrencyId());
            transaction.setConfirmed(true); //graphene transaction are always confirmed
            transaction.setFrom(transfer.getOperation().getFrom().getObjectId());
            transaction.setInput(!transfer.getOperation().getFrom().getObjectId().equals(account.getAccountId()));
            transaction.setTo(transfer.getOperation().getTo().getObjectId());

            GrapheneApiGenerator.getBlockHeaderTime(transfer.getBlockNum(), new ApiRequest(0, new GetTransactionDate(transaction, db.transactionDao())));
        }
    }

    /**
     * Gets the current change from two assets
     */
    private void getEquivalentValue(final CryptoNetEquivalentRequest request){
        if(request.getFromCurrency() instanceof  BitsharesAsset && request.getToCurrency() instanceof  BitsharesAsset) {
            BitsharesAsset fromAsset = (BitsharesAsset) request.getFromCurrency();
            BitsharesAsset toAsset = (BitsharesAsset) request.getToCurrency();
            ApiRequest getEquivalentRequest = new ApiRequest(0, new ApiRequestListener() {
                @Override
                public void success(Object answer, int idPetition) {
                    request.setPrice((Long)answer);
                }

                @Override
                public void fail(int idPetition) {
                    //TODO error
                }
            });
            GrapheneApiGenerator.getEquivalentValue(fromAsset.getBitsharesId(),toAsset.getBitsharesId(), getEquivalentRequest);
        }else{
            //TODO error
            System.out.println("Equivalen Value error ");
        }
    }

    /**
     * Class to retrieve the account id or the account name, if one of those is missing
     */
    private class AccountIdOrNameListener implements ApiRequestListener{
        final ManagerRequest request;

        GrapheneAccount account;

        AccountIdOrNameListener(ManagerRequest request) {
            this.request = request;
        }

        @Override
        public void success(Object answer, int idPetition) {
            if(answer instanceof  AccountProperties){
                AccountProperties props = (AccountProperties) answer;
                account = new GrapheneAccount();
                account.setAccountId(props.id);
                account.setName(props.name);
            }

            request.success(account);
        }

        @Override
        public void fail(int idPetition) {
            request.fail();
        }
    }

    /**
     * Class to retrieve the asset id or the asset name, if one of those is missing
     */
    private class AssetIdOrNameListener implements ApiRequestListener{
        final ManagerRequest request;

        BitsharesAsset asset;

        AssetIdOrNameListener(ManagerRequest request) {
            this.request = request;
        }

        @Override
        public void success(Object answer, int idPetition) {
            if(answer instanceof ArrayList) {
                if (((ArrayList) answer).get(0) instanceof BitsharesAsset) {
                    asset = (BitsharesAsset) ((ArrayList) answer).get(0);
                    request.success(asset);
                }
            }
        }

        @Override
        public void fail(int idPetition) {
            //TODO fail asset retrieve
        }
    }

    /**
     * Class to retrieve the transaction date
     */
    public static class GetTransactionDate implements ApiRequestListener{
        /**
         * The transaction to retrieve
         */
        private CryptoCoinTransaction transaction;
        /**
         * The DAO to insert or update the transaction
         */
        TransactionDao transactionDao;

        GetTransactionDate(CryptoCoinTransaction transaction, TransactionDao transactionDao) {
            this.transaction = transaction;
            this.transactionDao = transactionDao;
        }

        @Override
        public void success(Object answer, int idPetition) {
            if(answer instanceof BlockHeader){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
                try {
                    transaction.setDate(dateFormat.parse(((BlockHeader) answer).timestamp));
                    if (transactionDao.getByTransaction(transaction.getDate(),transaction.getFrom(),transaction.getTo(),transaction.getAmount()) == null) {
                        transactionDao.insertTransaction(transaction);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void fail(int idPetition) {

        }
    }

}
