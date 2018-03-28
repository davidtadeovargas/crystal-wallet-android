package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by henry on 26/3/2018.
 */

public class CreateBackupRequest extends FileServiceRequest {

    private AccountSeed seed;

    enum StatusCode{



    }

    public CreateBackupRequest(Context context, Activity activity, AccountSeed seed) {
        super(context, activity);
        this.seed = seed;
    }

    public AccountSeed getSeed() {
        return seed;
    }


}
