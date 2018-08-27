package cy.agorise.crystalwallet.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.idescout.sql.SqlScoutServer;

import java.util.Locale;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.network.CryptoNetManager;
import cy.agorise.crystalwallet.notifiers.CrystalWalletNotifier;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEvents;
import cy.agorise.crystalwallet.service.CrystalWalletService;

/**
 * The main application
 *
 * Created by Henry Varona on 6/9/2017.
 */

public class CrystalApplication extends Application {
    private Locale locale = null;

    public static String BITSHARES_URL[] =
            {
                    "wss://de.palmpay.io/ws",                   // Custom node
                    "wss://bitshares.nu/ws",
                    "wss://dexnode.net/ws",                    // Dallas, USA
                    "wss://bitshares.crypto.fans/ws",          // Munich, Germany
                    "wss://bitshares.openledger.info/ws",      // Openledger node
                    "ws://185.208.208.147:8090"                   // Custom node
            };

    public static String BITSHARES_TESTNET_URL[] =
            {
                    "http://185.208.208.147:11012",      // Openledger node
            };

    //This is for testing the equivalent values on the testnet TODO remove
    public static BitsharesAsset bitUSDAsset = new BitsharesAsset("USD",4,"1.3.121",BitsharesAsset.Type.SMART_COIN);
    //This is for testing the equivalent values on the testnet TODO remove
    public static BitsharesAsset bitEURAsset = new BitsharesAsset("EUR",4,"1.3.120",BitsharesAsset.Type.SMART_COIN);

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize the database
        CrystalDatabase db = CrystalDatabase.getAppDatabase(this.getApplicationContext());
        SqlScoutServer.create(this, getPackageName());

        //Using Bitshares Agorise Testnet
        //CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES,BITSHARES_TESTNET_URL);

        //This is for testing the equivalent values on the testnet TODO remove
        if(db.bitsharesAssetDao().getBitsharesAssetInfoById(bitEURAsset.getBitsharesId())== null){
            if(db.cryptoCurrencyDao().getByName(bitEURAsset.getName())== null){
                db.cryptoCurrencyDao().insertCryptoCurrency(bitEURAsset);
            }
            long idCurrency = db.cryptoCurrencyDao().getByName(bitEURAsset.getName()).getId();
            BitsharesAssetInfo info = new BitsharesAssetInfo(bitEURAsset);
            info.setCryptoCurrencyId(idCurrency);
            db.bitsharesAssetDao().insertBitsharesAssetInfo(info);

        }

        //This is for testing the equivalent values on the testnet TODO remove
        if(db.bitsharesAssetDao().getBitsharesAssetInfoById(bitUSDAsset.getBitsharesId())== null){
            if(db.cryptoCurrencyDao().getByName(bitUSDAsset.getName())== null){
                db.cryptoCurrencyDao().insertCryptoCurrency(bitUSDAsset);
            }
            long idCurrency = db.cryptoCurrencyDao().getByName(bitUSDAsset.getName()).getId();
            BitsharesAssetInfo info = new BitsharesAssetInfo(bitUSDAsset);
            info.setCryptoCurrencyId(idCurrency);
            db.bitsharesAssetDao().insertBitsharesAssetInfo(info);

        }

        //The crystal notifier is initialized
        CrystalWalletNotifier crystalWalletNotifier = new CrystalWalletNotifier(this);
        CryptoNetEvents.getInstance().addListener(crystalWalletNotifier);

        //Next line is for use the bitshares main net
        CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES,BITSHARES_URL);

        GeneralSetting generalSettingPreferredLanguage = db.generalSettingDao().getSettingByName(GeneralSetting.SETTING_NAME_PREFERRED_LANGUAGE);

        if (generalSettingPreferredLanguage != null) {
            Resources resources = getBaseContext().getResources();
            Locale locale = new Locale(generalSettingPreferredLanguage.getValue());
            Locale.setDefault(locale);
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, dm);
        }

        Intent intent = new Intent(getApplicationContext(), CrystalWalletService.class);
        startService(intent);
    }
}
