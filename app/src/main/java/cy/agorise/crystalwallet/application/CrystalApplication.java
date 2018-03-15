package cy.agorise.crystalwallet.application;

import android.app.Application;
import android.content.Intent;

import com.idescout.sql.SqlScoutServer;

import cy.agorise.crystalwallet.application.constant.BitsharesConstant;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.network.CryptoNetManager;
import cy.agorise.crystalwallet.service.CrystalWalletService;

/**
 * The main application
 *
 * Created by Henry Varona on 6/9/2017.
 */

public class CrystalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize the database
        CrystalDatabase db = CrystalDatabase.getAppDatabase(this.getApplicationContext());
        SqlScoutServer.create(this, getPackageName());

        //BitsharesConstant.addMainNetUrls();
        BitsharesConstant.addTestNetUrls();
        BitsharesConstant.addSmartCoins(this.getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), CrystalWalletService.class);
        startService(intent);
    }
}
