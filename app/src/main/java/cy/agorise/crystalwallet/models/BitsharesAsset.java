package cy.agorise.crystalwallet.models;

import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * Created by henry on 8/10/2017.
 */

public class BitsharesAsset extends CryptoCurrency {

    // The bitshares internal id
    private String bitsharesId;
    // The bitshares type see the enum below
    private Type assetType;

    /**
     * For representing each type of asset in the bitshares network
     */
    public enum Type{
        // The core asset aka BTS
        CORE(0),
        // the smartcoin assets, like bitEUR, bitUSD
        SMART_COIN(1),
        // The UIA assets type
        UIA(2),
        //THe prediction market type
        PREDICTION_MARKET(3);

        private int code;

        /**
         * The code is used for be stored in the database
         */
        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public BitsharesAsset(String symbol, int precision, String bitsharesId, Type assetType) {
        this.bitsharesId = bitsharesId;
        this.assetType = assetType;
        this.setCryptoNet(CryptoNet.BITSHARES);
        this.setName(symbol);
        this.setPrecision(precision);
    }

    public BitsharesAsset(CryptoCurrency currency){
        this.setId(currency.getId());
        this.setPrecision(currency.getPrecision());
        this.setCryptoNet(currency.getCryptoNet());
        this.setName(currency.getName());
    }

    public void loadInfo(BitsharesAssetInfo info){
        this.bitsharesId = info.getBitsharesId();
        this.assetType = info.getAssetType();
    }

    public String getBitsharesId() {
        return bitsharesId;
    }

    public void setBitsharesId(String bitsharesId) {
        this.bitsharesId = bitsharesId;
    }

    public Type getAssetType() {
        return assetType;
    }

    public void setAssetType(Type assetType) {
        this.assetType = assetType;
    }
}
