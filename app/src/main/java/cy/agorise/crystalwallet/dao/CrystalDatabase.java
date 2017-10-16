package cy.agorise.crystalwallet.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import cy.agorise.crystalwallet.dao.converters.Converters;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Manage the Database
 * Created by Henry Varona on 4/9/2017.
 */

@Database(entities = {AccountSeed.class, CryptoNetAccount.class, CryptoCoinTransaction.class, CryptoCurrency.class, CryptoCoinBalance.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class CrystalDatabase extends RoomDatabase {

    private static CrystalDatabase instance;

    public abstract AccountSeedDao accountSeedDao();
    public abstract CryptoNetAccountDao cryptoNetAccountDao();
    public abstract TransactionDao transactionDao();
    public abstract CryptoCoinBalanceDao cryptoCoinBalanceDao();
    public abstract CryptoCurrencyDao cryptoCurrencyDao();

    public static CrystalDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context,
                            CrystalDatabase.class, "CrystalWallet.db")
                            .allowMainThreadQueries()
                            //.addMigrations(MIGRATION_1_2)
                            .build();
        }
        return instance;
    }



    /*static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'account_seed' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'name' TEXT, 'master_seed' NUMERIC)");
            database.execSQL("CREATE TABLE 'crypto_net_account' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'seed_id' INTEGER, "
                    + "'account_number' INT, 'account_index' INT,"
                    + "FOREIGN_KEY(seed_id) REFERENCES seed(id))");
            database.execSQL("CREATE TABLE 'crypto_coin_transaction' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'account_id' INTEGER, "
                    + "'date' INT, 'is_input' INT, amount INT, crypto_coin TEXT, is_confirmed INT, "
                    + "FOREIGN_KEY(account_id) REFERENCES crypto_net_account(id))");
        }
    };*/
}
