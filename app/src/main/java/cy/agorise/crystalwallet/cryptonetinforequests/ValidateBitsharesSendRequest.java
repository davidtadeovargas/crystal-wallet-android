package cy.agorise.crystalwallet.cryptonetinforequests;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Created by henry on 8/10/2017.
 */

public class ValidateBitsharesSendRequest extends CryptoNetInfoRequest {
    private String mSourceAccount;
    private String mToAccount;
    private long mBaseAmount;
    private String mBaseAsset;
    private long mFeeAmount;
    private String mFeeAsset;
    private String mMemo;


    private Boolean isSend;

    public ValidateBitsharesSendRequest(String sourceAccount, String toAccount,
                                        long baseAmount, String baseAsset, long feeAmount,
                                        String feeAsset, String memo) {
        super(CryptoCoin.BITSHARES);
        this.mSourceAccount = sourceAccount;
        this.mToAccount = toAccount;
        this.mBaseAmount = baseAmount;
        this.mBaseAsset = baseAsset;
        this.mFeeAmount = feeAmount;
        this.mFeeAsset = feeAsset;
        this.mMemo = memo;
    }

    public ValidateBitsharesSendRequest(String sourceAccount, String toAccount,
                                        long baseAmount, String baseAsset, long feeAmount,
                                        String feeAsset) {
        this(sourceAccount,toAccount,baseAmount,baseAsset,feeAmount,feeAsset,null);
    }

    public String getSourceAccount() {
        return mSourceAccount;
    }

    public String getToAccount() {
        return mToAccount;
    }

    public long getBaseAmount() {
        return mBaseAmount;
    }

    public String getBaseAsset() {
        return mBaseAsset;
    }

    public long getFeeAmount() {
        return mFeeAmount;
    }

    public String getFeeAsset() {
        return mFeeAsset;
    }

    public String getMemo() {
        return mMemo;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
        this.validate();
    }

    public void validate(){
        if ((this.isSend != null)){
            this._fireOnCarryOutEvent();
        }
    }
}
