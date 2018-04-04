package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

/**
 * Created by henry on 26/3/2018.
 */

public class ImportBackupRequest extends FileServiceRequest {

    public enum StatusCode{
        NOT_STARTED,
        SUCCEEDED,
        FAILED
    }

    private String filePath;
    private StatusCode status;

    public ImportBackupRequest(Context context, String password, String filePath) {
        super(context, password);
        this.filePath = filePath;
        this.status = StatusCode.NOT_STARTED;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setStatus(StatusCode statusCode){
        this.status = statusCode;
        this.validate();
    }

    public StatusCode getStatus(){
        return this.status;
    }

    public void validate(){
        if (this.status != StatusCode.NOT_STARTED){
            this._fireOnCarryOutEvent();
        }
    }
}
