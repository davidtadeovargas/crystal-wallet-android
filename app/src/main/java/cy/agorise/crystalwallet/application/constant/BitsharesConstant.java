package cy.agorise.crystalwallet.application.constant;

import android.content.Context;

import java.util.HashSet;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.BitsharesAsset;
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.network.CryptoNetManager;

/**
 * Created by henry on 15/3/2018.
 */

public abstract class BitsharesConstant {
    public final static String BITSHARES_URL[] =
            {
                    "wss://de.palmpay.io/ws",                   // Custom node
                    "wss://bitshares.nu/ws",
                    "wss://dexnode.net/ws",                    // Dallas, USA
                    "wss://bitshares.crypto.fans/ws",          // Munich, Germany
                    "wss://bitshares.openledger.info/ws",      // Openledger node
                    "ws://185.208.208.147:8090"                   // Custom node
            };

    public final static String BITSHARES_TESTNET_URL[] =
            {
                    "http://185.208.208.147:11012",      // Openledger node
            };

    //testnet faucet
    //public final static String FAUCET_URL = "http://185.208.208.147:5010";
    public final static String FAUCET_URL = "https://de.palmpay.io";
    public final static String EQUIVALENT_URL = "wss://bitshares.openledger.info/ws";

    public final static BitsharesAsset[] SMARTCOINS = new BitsharesAsset[]{
            new BitsharesAsset("USD",4,"1.3.121",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("EUR",4,"1.3.120",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("CNY",4,"1.3.113",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("RUBLE",5,"1.3.1325",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("AUD",4,"1.3.117",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("SILVER",4,"1.3.105",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("GOLD",6,"1.3.106",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("JPY",2,"1.3.119",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("CAD",4,"1.3.115",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("MXN",4,"1.3.114",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("GBP",4,"1.3.118",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("ARS",4,"1.3.1017",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("KRW",4,"1.3.102",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("CHF",4,"1.3.116",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("SEK",4,"1.3.111",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("RUB",4,"1.3.110",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("NZD",4,"1.3.112",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("XCD",4,"1.3.2650",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("TRY",4,"1.3.107",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("HKD",4,"1.3.109",BitsharesAsset.Type.SMART_COIN),
            new BitsharesAsset("SGD",4,"1.3.108",BitsharesAsset.Type.SMART_COIN)
    };

    public static void addMainNetUrls(){
        for(String url : BITSHARES_URL){
            CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES,url);
        }
    }

    public static void addTestNetUrls(){
        for(String url : BITSHARES_TESTNET_URL){
            CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES,url);
        }
    }

    public static void addSmartCoins(Context context){
        CrystalDatabase db = CrystalDatabase.getAppDatabase(context);
        for(BitsharesAsset smartcoin : SMARTCOINS){
            if(db.cryptoCurrencyDao().getByName(smartcoin.getName())== null){
                db.cryptoCurrencyDao().insertCryptoCurrency(smartcoin);
            }
            long idCurrency = db.cryptoCurrencyDao().getByName(smartcoin.getName()).getId();
            BitsharesAssetInfo info = new BitsharesAssetInfo(smartcoin);
            info.setCryptoCurrencyId(idCurrency);
            db.bitsharesAssetDao().insertBitsharesAssetInfo(info);
        }

    }

}

