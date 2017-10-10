package cy.agorise.crystalwallet.cryptonetinforequests;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.GrapheneAccount;

/**
 * Class used to make a send amount request.
 *
 * Created by henry on 8/10/2017.
 */

public class ValidateBitsharesSendRequest extends CryptoNetInfoRequest {
    // The app context
    private Context mContext;
    // The source account used to transfer fund from
    private GrapheneAccount mSourceAccount;
    // The destination account id
    private String mToAccount;
    // The amount of the transaction
    private long mBaseAmount;
    // The asset id of the transaction
    private String mBaseAsset;
    // The fee amount
    private long mFeeAmount;
    // The fee asset id
    private String mFeeAsset;
    // The memo, can be null
    private String mMemo;
    // The state of this request
    private Boolean isSend;

    public ValidateBitsharesSendRequest(Context context, GrapheneAccount sourceAccount,
                                        String toAccount, long baseAmount, String baseAsset,
                                        long feeAmount,String feeAsset, String memo) {
        super(CryptoCoin.BITSHARES);
        this.mContext = context;
        this.mSourceAccount = sourceAccount;
        this.mToAccount = toAccount;
        this.mBaseAmount = baseAmount;
        this.mBaseAsset = baseAsset;
        this.mFeeAmount = feeAmount;
        this.mFeeAsset = feeAsset;
        this.mMemo = memo;
    }

    public ValidateBitsharesSendRequest(Context context, GrapheneAccount sourceAccount,
                                        String toAccount, long baseAmount, String baseAsset,
                                        long feeAmount,String feeAsset) {
        this(context, sourceAccount,toAccount,baseAmount,baseAsset,feeAmount,feeAsset,null);
    }

    public Context getContext() {
        return mContext;
    }

    public GrapheneAccount getSourceAccount() {
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
