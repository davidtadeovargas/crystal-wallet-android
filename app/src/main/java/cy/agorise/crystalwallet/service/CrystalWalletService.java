package cy.agorise.crystalwallet.service;


import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.crystalwallet.manager.FileBackupManager;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.manager.BitsharesAccountManager;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequests;

/**
 * Created by Henry Varona on 3/10/2017.
 */


public class CrystalWalletService extends LifecycleService {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BitsharesAccountManager  bitsharesAccountManager;
    private Thread LoadAccountTransactionsThread;
    private EquivalencesThread LoadEquivalencesThread;
    private boolean keepLoadingAccountTransactions;
    private boolean keepLoadingEquivalences;
    private CryptoNetInfoRequests cryptoNetInfoRequests;
    private FileBackupManager fileBackupManager;
    private FileServiceRequests fileServiceRequests;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    public void loadEquivalentsValues(){
        this.keepLoadingEquivalences = true;
        final LifecycleService service = this;

        //getting the preferred currency of the user
        final LiveData<GeneralSetting> preferredCurrencySetting =
                CrystalDatabase.getAppDatabase(service).generalSettingDao().getByName(GeneralSetting.SETTING_NAME_PREFERRED_CURRENCY);

        preferredCurrencySetting.observe(service, new Observer<GeneralSetting>() {
            @Override
            public void onChanged(final @Nullable GeneralSetting generalSetting) {
                if (generalSetting != null) {
                    CryptoCurrency preferredCurrency = CrystalDatabase.getAppDatabase(service).cryptoCurrencyDao().getByNameAndCryptoNet("EUR", CryptoNet.BITSHARES.name());

                    if (preferredCurrency != null) {
                        BitsharesAssetInfo preferredCurrencyBitsharesInfo = CrystalDatabase.getAppDatabase(service).bitsharesAssetDao().getBitsharesAssetInfoFromCurrencyId(preferredCurrency.getId());

                        if (preferredCurrencyBitsharesInfo != null) {
                            final BitsharesAsset preferredCurrencyBitshareAsset = new BitsharesAsset(preferredCurrency);
                            preferredCurrencyBitshareAsset.loadInfo(preferredCurrencyBitsharesInfo);

                            //Loading "from" currencies
                            final LiveData<List<BitsharesAssetInfo>> bitsharesAssetInfo =
                                    CrystalDatabase.getAppDatabase(service).bitsharesAssetDao().getAll();

                            bitsharesAssetInfo.observe(service, new Observer<List<BitsharesAssetInfo>>() {
                                @Override
                                public void onChanged(@Nullable List<BitsharesAssetInfo> bitsharesAssetInfos) {
                                    List<BitsharesAsset> bitsharesAssets = new ArrayList<BitsharesAsset>();
                                    List<Long> currenciesIds = new ArrayList<Long>();
                                    for (BitsharesAssetInfo bitsharesAssetInfo : bitsharesAssetInfos) {
                                        currenciesIds.add(bitsharesAssetInfo.getCryptoCurrencyId());
                                    }
                                    ;
                                    List<CryptoCurrency> bitsharesCurrencies = CrystalDatabase.getAppDatabase(service).cryptoCurrencyDao().getByIds(currenciesIds);

                                    BitsharesAsset nextAsset;
                                    for (int i = 0; i < bitsharesCurrencies.size(); i++) {
                                        CryptoCurrency nextCurrency = bitsharesCurrencies.get(i);
                                        BitsharesAssetInfo nextBitsharesInfo = bitsharesAssetInfos.get(i);
                                        nextAsset = new BitsharesAsset(nextCurrency);
                                        nextAsset.loadInfo(nextBitsharesInfo);
                                        bitsharesAssets.add(nextAsset);
                                    }

                                    if (LoadEquivalencesThread != null) {
                                        LoadEquivalencesThread.stopLoadingEquivalences();
                                    };
                                    LoadEquivalencesThread = new EquivalencesThread(service, generalSetting.getValue(), bitsharesAssets);
                                    LoadEquivalencesThread.start();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void loadAccountTransactions(){
        this.keepLoadingAccountTransactions = true;
        final CrystalWalletService thisService = this;

        final CrystalDatabase db = CrystalDatabase.getAppDatabase(this);
        //final LiveData<List<CryptoNetAccount>> cryptoNetAccountList = db.cryptoNetAccountDao().getAll();
        final LiveData<List<GrapheneAccountInfo>> grapheneAccountInfoList = db.grapheneAccountInfoDao().getAll();
        grapheneAccountInfoList.observe(this, new Observer<List<GrapheneAccountInfo>>() {
            @Override
            public void onChanged(@Nullable List<GrapheneAccountInfo> grapheneAccountInfos) {
                GrapheneAccount nextGrapheneAccount;
                for(GrapheneAccountInfo nextGrapheneAccountInfo : grapheneAccountInfos) {
                    CryptoNetAccount nextAccount = db.cryptoNetAccountDao().getById(nextGrapheneAccountInfo.getCryptoNetAccountId());
                    //GrapheneAccountInfo grapheneAccountInfo = db.grapheneAccountInfoDao().getByAccountId(nextAccount.getId());
                    nextGrapheneAccount = new GrapheneAccount(nextAccount);
                    nextGrapheneAccount.loadInfo(nextGrapheneAccountInfo);


                    bitsharesAccountManager.loadAccountFromDB(nextGrapheneAccount,thisService);
                }
            }
        });



        /*while(this.keepLoadingAccountTransactions){
            try{
                Log.i("Crystal Service","Searching for transactions...");
                this.bitsharesAccountManager.loadAccountFromDB();
                Thread.sleep(60000);//Sleep for 1 minutes
                // TODO search for accounts and make managers find new transactions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Creates a instance for the cryptoNetInfoRequest and the managers
        this.cryptoNetInfoRequests = CryptoNetInfoRequests.getInstance();
        this.fileServiceRequests = FileServiceRequests.getInstance();
        this.bitsharesAccountManager = new BitsharesAccountManager();
        this.fileBackupManager = new FileBackupManager();

        //Add the managers as listeners of the CryptoNetInfoRequest so
        //they can carry out the info requests from the ui
        this.cryptoNetInfoRequests.addListener(this.bitsharesAccountManager);

        this.fileServiceRequests.addListener(this.fileBackupManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);

        if (LoadAccountTransactionsThread == null) {
            LoadAccountTransactionsThread = new Thread() {
                @Override
                public void run() {
                    loadAccountTransactions();
                }
            };
            LoadAccountTransactionsThread.start();
        }

        //if (LoadEquivalencesThread == null) {
        //    LoadEquivalencesThread = new EquivalencesThread() {
        //        @Override
        //        public void run() {
                    loadEquivalentsValues();
        //        }
        //    };
        //    LoadEquivalencesThread.start();
       // }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Crystal Service", "Destroying service");
    }
}
