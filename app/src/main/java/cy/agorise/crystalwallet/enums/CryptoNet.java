package cy.agorise.crystalwallet.enums;

import java.io.Serializable;

/**
 * Created by Henry Varona on 12/9/2017.
 */

public enum CryptoNet implements Serializable {
    BITCOIN("BITCOIN"), BITCOIN_TEST("BITCOIN(TEST)"), LITECOIN("LITECOIN"), DASH("DASH"), DOGECOIN("DOGECOIN"), BITSHARES("BITSHARES");

    protected String label;

    CryptoNet(String label){
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
