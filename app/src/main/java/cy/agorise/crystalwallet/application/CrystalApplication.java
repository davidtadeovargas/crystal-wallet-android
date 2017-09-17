package cy.agorise.crystalwallet.application;

import android.app.Application;

import com.idescout.sql.SqlScoutServer;

import cy.agorise.crystalwallet.dao.CrystalDatabase;

/**
 * Created by Henry Varona on 6/9/2017.
 */

public class CrystalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize the database
        CrystalDatabase db = CrystalDatabase.getAppDatabase(this.getApplicationContext());
        SqlScoutServer.create(this, getPackageName());
    }
}
