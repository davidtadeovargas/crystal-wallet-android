package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by henry on 26/3/2018.
 */

public class CreateBackupRequest extends FileServiceRequest {

    public enum StatusCode{
        PROCESSING(0),
        OK(1),
        FAILED(2);

        protected long code;

        StatusCode(long code) {
            this.code = code;
        }

        public long getCode() {
            return code;
        }
    }

    private StatusCode status = StatusCode.PROCESSING;

    public CreateBackupRequest(Context context, String password) {
        super(context,password);
    }

    public void setStatus(StatusCode status) {
        this.status = status;
        this._fireOnCarryOutEvent();
    }

    public StatusCode getStatus() {
        return status;
    }
}
