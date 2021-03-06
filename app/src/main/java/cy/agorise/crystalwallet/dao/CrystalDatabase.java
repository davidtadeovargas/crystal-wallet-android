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
import cy.agorise.crystalwallet.models.BitsharesAssetInfo;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.ContactAddress;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;

/**
 * Manage the Database
 * Created by Henry Varona on 4/9/2017.
 */

@Database(entities = {
        AccountSeed.class,
        CryptoNetAccount.class,
        CryptoCoinTransaction.class,
        Contact.class,
        ContactAddress.class,
        CryptoCurrency.class,
        CryptoCoinBalance.class,
        GrapheneAccountInfo.class,
        BitsharesAssetInfo.class,
        CryptoCurrencyEquivalence.class,
        GeneralSetting.class
}, version = 3)
@TypeConverters({Converters.class})
public abstract class CrystalDatabase extends RoomDatabase {

    private static CrystalDatabase instance;

    public abstract AccountSeedDao accountSeedDao();
    public abstract CryptoNetAccountDao cryptoNetAccountDao();
    public abstract GrapheneAccountInfoDao grapheneAccountInfoDao();
    public abstract TransactionDao transactionDao();
    public abstract ContactDao contactDao();
    public abstract CryptoCoinBalanceDao cryptoCoinBalanceDao();
    public abstract CryptoCurrencyDao cryptoCurrencyDao();
    public abstract BitsharesAssetDao bitsharesAssetDao();
    public abstract CryptoCurrencyEquivalenceDao cryptoCurrencyEquivalenceDao();
    public abstract GeneralSettingDao generalSettingDao();

    public static CrystalDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context,
                            CrystalDatabase.class, "CrystalWallet.db")
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_2_3)
                            .build();
        }
        return instance;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE graphene_account ADD COLUMN upgraded_to_ltm INTEGER NOT NULL DEFAULT 0");
        }
    };
}
