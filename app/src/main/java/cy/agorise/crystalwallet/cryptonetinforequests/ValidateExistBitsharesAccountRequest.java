package cy.agorise.crystalwallet.cryptonetinforequests;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * This class validates that an account name exist, this can be used to verified the existing accounts
 * or to verified if the name is available to create an Account
 *
 * Created by henry on 8/10/2017.
 */

public class ValidateExistBitsharesAccountRequest extends CryptoNetInfoRequest {
    // The account name to validate
    private String accountName;
    // The result of the validation, or null if there isn't a response
    private Boolean accountExists;

    public ValidateExistBitsharesAccountRequest(CryptoCoin coin, String accountName) {
        super(coin);
        this.accountName = accountName;
    }


    public void setAccountExists(boolean value){
        this.accountExists = value;
        this.validate();
    }

    public void validate(){
        if ((this.accountExists != null)){
            this._fireOnCarryOutEvent();
        }
    }

    public String getAccountName() {
        return accountName;
    }

}
