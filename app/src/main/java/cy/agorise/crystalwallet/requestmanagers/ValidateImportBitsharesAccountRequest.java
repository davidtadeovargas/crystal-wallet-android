package cy.agorise.crystalwallet.requestmanagers;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.enums.SeedType;

/**
 * Imports a bitshares accounts,
 *
 * return true if the account exist, and the mnemonic (brainkey provide is for that account
 * Created by Henry Varona on 1/10/2017.
 */

public class ValidateImportBitsharesAccountRequest extends CryptoNetInfoRequest {

    /**
     * The status code of this request
     */
    public enum StatusCode{
        NOT_STARTED,
        SUCCEEDED,
        NO_INTERNET,
        NO_SERVER_CONNECTION,
        ACCOUNT_DOESNT_EXIST,
        BAD_SEED,
        NO_ACCOUNT_DATA,
        PETITION_FAILED
    }

    /**
     * The name of the account
     */
    private final String accountName;

    /**
     * The mnemonic words
     */
    private final String mnemonic;

    /**
     * If this seed is BIP39 or Brainkey
     */
    private SeedType seedType;

    /**
     * The status of this request
     */
    private StatusCode status = StatusCode.NOT_STARTED;

    public ValidateImportBitsharesAccountRequest(String accountName, String mnemonic){
        super(CryptoCoin.BITSHARES);
        this.accountName = accountName;
        this.mnemonic = mnemonic;
    }

    public void validate(){
        if (!(this.status.equals(StatusCode.NOT_STARTED))){
            this._fireOnCarryOutEvent();
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public SeedType getSeedType() {
        return seedType;
    }

    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
        this._fireOnCarryOutEvent();
    }

    public StatusCode getStatus() {
        return status;
    }
}
