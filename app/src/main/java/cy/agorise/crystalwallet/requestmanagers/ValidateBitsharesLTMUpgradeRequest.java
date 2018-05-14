package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.GrapheneAccount;

/**
 * Class used to make a send amount request.
 *
 * Created by henry on 8/10/2017.
 */

public class ValidateBitsharesLTMUpgradeRequest extends CryptoNetInfoRequest {
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
        NO_FUNDS,
        PETITION_FAILED
    }

    // The app context
    private Context mContext;
    // The source account used to transfer fund from
    private GrapheneAccount mSourceAccount;
    // The state of this request
    private StatusCode status = StatusCode.NOT_STARTED;

    public ValidateBitsharesLTMUpgradeRequest(Context context, GrapheneAccount sourceAccount) {
        super(CryptoCoin.BITSHARES);
        this.mContext = context;
        this.mSourceAccount = sourceAccount;

    }

    public Context getContext() {
        return mContext;
    }

    public GrapheneAccount getSourceAccount() {
        return mSourceAccount;
    }

    public void validate(){
        if ((this.status != StatusCode.NOT_STARTED)){
            this._fireOnCarryOutEvent();
        }
    }

    public void setStatus(StatusCode code){
        this.status = code;
        this.validate();
    }

    public StatusCode getStatus() {
        return status;
    }
}
