package cy.agorise.crystalwallet.service;


import android.app.Service;
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

import java.util.List;

import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.manager.BitsharesAccountManager;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 3/10/2017.
 */


public class CrystalWalletService extends LifecycleService {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BitsharesAccountManager  bitsharesAccountManager;
    private Thread LoadAccountTransactionsThread;
    private boolean keepLoadingAccountTransactions;
    private CryptoNetInfoRequests cryptoNetInfoRequests;

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

    public void loadAccountTransactions(){
        this.keepLoadingAccountTransactions = true;
        final CrystalWalletService thisService = this;

        CrystalDatabase db = CrystalDatabase.getAppDatabase(this);
        final LiveData<List<CryptoNetAccount>> cryptoNetAccountList = db.cryptoNetAccountDao().getAll();
        cryptoNetAccountList.observe(this, new Observer<List<CryptoNetAccount>>() {
            @Override
            public void onChanged(@Nullable List<CryptoNetAccount> cryptoNetAccounts) {
                for(CryptoNetAccount nextAccount : cryptoNetAccountList.getValue()) {
                    bitsharesAccountManager.loadAccountFromDB(nextAccount,thisService);
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
        //Creates a instance for the cryptoNetInfoRequest and the managers
        this.cryptoNetInfoRequests = CryptoNetInfoRequests.getInstance();
        this.bitsharesAccountManager = new BitsharesAccountManager();

        //Add the managers as listeners of the CryptoNetInfoRequest so
        //they can carry out the info requests from the ui
        this.cryptoNetInfoRequests.addListener(this.bitsharesAccountManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (LoadAccountTransactionsThread == null) {
            LoadAccountTransactionsThread = new Thread() {
                public void run() {
                    loadAccountTransactions();
                }
            };
            LoadAccountTransactionsThread.start();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("Crystal Service", "Destroying service");
    }
}
