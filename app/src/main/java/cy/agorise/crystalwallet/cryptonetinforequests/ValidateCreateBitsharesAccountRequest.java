package cy.agorise.crystalwallet.cryptonetinforequests;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Imports a bitsahres accounts,
 *
 * return true if the account exist, and the mnemonic (brainkey provide is for that account
 * Created by Henry Varona on 1/10/2017.
 */

public class ValidateCreateBitsharesAccountRequest extends CryptoNetInfoRequest {

    /**
     * The name of the account
     */
    private String accountName;

    /**
     * Indicates if the account exist
     */
    private Boolean accountExists;

    private Boolean creationComplete;

    private Context context;

    public ValidateCreateBitsharesAccountRequest(String accountName, String mnemonic, Context context){
        super(CryptoCoin.BITSHARES);
        this.accountName = accountName;
        this.context = context;
    }

    public void setAccountExists(boolean value){
        this.accountExists = value;
        this.validate();
    }

    public void setCreationComple(boolean value){
        this.creationComplete = value;
        this.validate();
    }

    public boolean getAccountExists(){
        return this.accountExists;
    }

    public Boolean getCreationComplete() {
        return creationComplete;
    }

    public void validate(){
        if ((this.accountExists != null) && (this.creationComplete != null)){
            this._fireOnCarryOutEvent();
        }
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
}
