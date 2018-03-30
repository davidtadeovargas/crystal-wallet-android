package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by henry on 26/3/2018.
 */

public class CreateBackupRequest extends FileServiceRequest {

    public enum StatusCode{
        NOT_STARTED,
        SUCCEEDED,
        FAILED
    }

    private String filePath;
    private StatusCode status;

    public CreateBackupRequest(Context context, String password) {
        super(context,password);
        this.filePath = "";
        this.status = StatusCode.NOT_STARTED;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
        this.validate();
    }

    public void setStatus(StatusCode statusCode){
        this.status = statusCode;
        this.validate();
    }

    public StatusCode getStatus(){
        return this.status;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void validate(){
        if (this.status != StatusCode.NOT_STARTED){
            this._fireOnCarryOutEvent();
        }
    }


}
