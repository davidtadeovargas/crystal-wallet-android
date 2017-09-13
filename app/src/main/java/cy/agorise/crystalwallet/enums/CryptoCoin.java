package cy.agorise.crystalwallet.enums;

import java.io.Serializable;

/**
 * Created by Henry Varona on 12/9/2017.
 */

public enum CryptoCoin implements Serializable {
    BITCOIN(CryptoNet.BITCOIN,"BTC",8,6),
    BITCOIN_TEST(CryptoNet.BITCOIN_TEST,"BTC",8,6),
    LITECOIN(CryptoNet.LITECOIN,"LTC",8,6),
    DASH(CryptoNet.DASH,"DASH",8,6),
    DOGECOIN(CryptoNet.DOGECOIN,"DOGE",8,6),
    BITSHARES(CryptoNet.BITSHARES,"BTS",8,6);

    protected CryptoNet cryptoNet;
    protected String label;
    protected int precision;
    protected int confirmationsNeeded;

    CryptoCoin(CryptoNet cryptoNet, String label, int precision, int confirmationsNeeded){
        this.cryptoNet = cryptoNet;
        this.label = label;
        this.precision = precision;
        this.confirmationsNeeded = confirmationsNeeded;
    }

    public CryptoNet getCryptoNet(){
        return this.cryptoNet;
    }
    public String getLabel(){
        return this.label;
    }
    public int getPrecision(){
        return this.precision;
    }
    public int getConfirmationsNeeded(){
        return this.confirmationsNeeded;
    }
}
