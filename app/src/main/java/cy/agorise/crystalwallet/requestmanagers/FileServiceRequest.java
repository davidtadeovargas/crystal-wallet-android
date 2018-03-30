package cy.agorise.crystalwallet.requestmanagers;

import android.app.Activity;
import android.content.Context;

/**
 * Created by henry on 26/3/2018.
 */

public abstract class FileServiceRequest {

    protected Context context;
    //protected Activity activity;
    protected String password;

    protected FileServiceRequest(Context context, String password) {
        this.context = context;
        //this.activity = activity;
        this.password = password;
    }

    protected FileServiceRequestListener listener;

    public void setListener(FileServiceRequestListener listener){
        this.listener = listener;
    }

    public Context getContext() {
        return context;
    }

    public String getPassword() {
        return password;
    }

    protected void _fireOnCarryOutEvent(){
        listener.onCarryOut();
    }
}
