package cy.agorise.crystalwallet.requestmanagers;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.enums.SeedType;

/**
 * Imports a bitsahres accounts,
 *
 * return true if the account exist, and the mnemonic (brainkey provide is for that account
 * Created by Henry Varona on 1/10/2017.
 */

public class ValidateImportBitsharesAccountRequest extends CryptoNetInfoRequest {

    /**
     * The name of the account
     */
    private String accountName;

    /**
     * The mnemonic words
     */
    private String mnemonic;

    /**
     * Indicates if the account exist
     */
    private Boolean accountExists;
    /**
     * Indicates if the mnemonic provided belongs to that account
     */
    private Boolean mnemonicIsCorrect;

    private SeedType seedType;

    public ValidateImportBitsharesAccountRequest(String accountName, String mnemonic){
        super(CryptoCoin.BITSHARES);
        this.accountName = accountName;
        this.mnemonic = mnemonic;
    }

    public void setAccountExists(boolean value){
        this.accountExists = value;
        this.validate();
    }

    public void setMnemonicIsCorrect(boolean value){
        this.mnemonicIsCorrect = value;
        this.validate();
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public SeedType getSeedType() {
        return seedType;
    }

    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }
}
