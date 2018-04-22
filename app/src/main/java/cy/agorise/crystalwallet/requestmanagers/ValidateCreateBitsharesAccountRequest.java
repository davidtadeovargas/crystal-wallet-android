package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.models.GrapheneAccount;

/**
 * Imports a bitsahres accounts,
 *
 * return true if the account exist, and the mnemonic (brainkey provide is for that account
 * Created by Henry Varona on 1/10/2017.
 */

public class ValidateCreateBitsharesAccountRequest extends CryptoNetInfoRequest {

    /**
     * The status code of this request
     */
    public enum StatusCode{
        NOT_STARTED,
        SUCCEEDED,
        NO_INTERNET,
        NO_SERVER_CONNECTION,
        ACCOUNT_EXIST,
        NO_ACCOUNT_DATA

    }

    /**
     * The name of the account
     */
    private String accountName;


    // The state of this request
    private StatusCode status = StatusCode.NOT_STARTED;

    private GrapheneAccount account;

    private Context context;

    public ValidateCreateBitsharesAccountRequest(String accountName, Context context){
        super(CryptoCoin.BITSHARES);
        this.accountName = accountName;
        this.context = context;
    }

    public void setAccount(GrapheneAccount account){
        this.account = account;
        this.validate();
    }


    public GrapheneAccount getAccount() {
        return account;
    }

    public void validate(){
        if(!status.equals(StatusCode.NOT_STARTED))
            this._fireOnCarryOutEvent();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Context getContext() {
        return context;
    }

    public void setStatus(StatusCode code){
        this.status = code;
        this.validate();
    }

    public StatusCode getStatus() {
        return status;
    }
}
