package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

/**
 * Created by henry on 26/3/2018.
 */

public class ImportBackupRequest extends FileServiceRequest {

    public ImportBackupRequest(Context context, String password) {
        super(context, password);
    }
}
