package cy.agorise.crystalwallet.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.*;
import android.arch.persistence.room.migration.Migration;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 4/9/2017.
 */

@Database(entities = {AccountSeed.class, CryptoNetAccount.class}, version = 2)
public abstract class CrystalDatabase extends RoomDatabase {


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'account_seed' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'name' TEXT, 'master_seed' NUMERIC)");
            database.execSQL("CREATE TABLE 'crypto_net_account' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'seed_id', "
                    + "'account_number' INT, 'account_index' INT,"
                    + "FOREIGN_KEY(seed_id) REFERENCES seed(id))");
        }
    };
}
