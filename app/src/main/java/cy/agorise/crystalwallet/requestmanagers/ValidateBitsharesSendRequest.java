package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.GrapheneAccount;

/**
 * Class used to make a send amount request.
 *
 * Created by henry on 8/10/2017.
 */

public class ValidateBitsharesSendRequest extends CryptoNetInfoRequest {
    /**
     * The status code of this request
     */
    public enum StatusCode{
        NOT_STARTED,
        SUCCEEDED,
        NO_INTERNET,
        NO_SERVER_CONNECTION,
        NO_ASSET_INFO_DB,
        NO_ASSET_INFO,
        NO_TO_USER_INFO,
        PETITION_FAILED
    }

    // The app context
    private Context mContext;
    // The source account used to transfer fund from
    private GrapheneAccount mSourceAccount;
    // The destination account id
    private String mToAccount;
    // The amount of the transaction
    private long mAmount;
    // The asset id of the transaction
    private String mAsset;
    // The memo, can be null
    private String mMemo;
    // The state of this request
    private StatusCode status = StatusCode.NOT_STARTED;

    public ValidateBitsharesSendRequest(Context context, GrapheneAccount sourceAccount,
                                        String toAccount, long amount, String asset, String memo) {
        super(CryptoCoin.BITSHARES);
        this.mContext = context;
        this.mSourceAccount = sourceAccount;
        this.mToAccount = toAccount;
        this.mAmount = amount;
        this.mAsset = asset;
        this.mMemo = memo;
    }

    public ValidateBitsharesSendRequest(Context context, GrapheneAccount sourceAccount,
                                        String toAccount, long amount, String asset) {
        this(context, sourceAccount,toAccount,amount,asset,null);
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

    public long getAmount() {
        return mAmount;
    }

    public String getAsset() {
        return mAsset;
    }


    public String getMemo() {
        return mMemo;
    }

    public void validate(){
        if ((this.status != StatusCode.NOT_STARTED)){
            this._fireOnCarryOutEvent();
        }
    }

    public void setStatus(StatusCode code){
        this.status = code;
        this._fireOnCarryOutEvent();
    }

    public StatusCode getStatus() {
        return status;
    }
}
