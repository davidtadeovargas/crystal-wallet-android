package cy.agorise.crystalwallet.cryptonetinforequests;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Created by Henry Varona on 1/10/2017.
 */

public class ValidateImportBitsharesAccountRequest extends CryptoNetInfoRequest {

    private String accountName;
    private String mnemonic;

    private Boolean accountExists;
    private Boolean mnemonicIsCorrect;

    public ValidateImportBitsharesAccountRequest(String accountName, String mnemonic){
        super(CryptoCoin.BITSHARES);
        this.accountName = accountName;
        this.mnemonic = mnemonic;
    }

    public void setAccountExists(boolean value){
        this.accountExists = value;
    }

    public void setMnemonicIsCorrect(boolean value){
        this.mnemonicIsCorrect = value;
    }

    public boolean getAccountExists(){
        return this.accountExists;
    }

    public boolean getMnemonicIsCorrect(){
        return this.mnemonicIsCorrect;
    }

    public void validate(){
        if ((this.accountExists != null) && (this.mnemonicIsCorrect != null)){
            this._fireOnCarryOutEvent();
        }
    }
}
