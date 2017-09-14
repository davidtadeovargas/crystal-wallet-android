package cy.agorise.crystalwallet.enums;

import java.io.Serializable;

/**
 * CryptoNet Enumeration, a Crypto Net is define as the net where a CryptoCoin works, iniside the
 * CrypotNet is where the transaction and balance works, using the CryptoCoin Assets
 *
 * Created by Henry Varona on 12/9/2017.
 */

public enum CryptoNet implements Serializable {
    BITCOIN("BITCOIN",6), BITCOIN_TEST("BITCOIN(TEST)",6), LITECOIN("LITECOIN",6), DASH("DASH",6), DOGECOIN("DOGECOIN",6), BITSHARES("BITSHARES",1), STEEM("STEEN",1);

    protected String label;

    protected int confirmationsNeeded;


    CryptoNet(String label,int confirmationsNeeded){
        this.label = label;
        this.confirmationsNeeded = confirmationsNeeded;
    }

    public String getLabel(){
        return this.label;
    }

    public int getConfirmationsNeeded(){
        return this.confirmationsNeeded;
    }
}
