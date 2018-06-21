package cy.agorise.crystalwallet.requestmanagers;

import android.content.Context;

import cy.agorise.crystalwallet.enums.CryptoCoin;

/**
 * Created by henry on 6/20/2018.
 */

public class GetBitsharesAccountNameCacheRequest extends CryptoNetInfoRequest {

    private String accountId;
    private String accountName;

    public GetBitsharesAccountNameCacheRequest(Context context, String accountId) {
        super(CryptoCoin.BITSHARES);
        this.accountId = accountId;
        this.accountName = "";
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
        this.validate();
    }

    public void validate(){
        if ((!this.accountName.equals(""))){
            this._fireOnCarryOutEvent();
        }
    }


}
