package cy.agorise.crystalwallet.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Henry Varona on 6/9/2017.
 */

public class DatabaseConnection {
    private static final DatabaseConnection instance = new DatabaseConnection();
    private CrystalDatabase db;

    private DatabaseConnection(){
    }

    public static CrystalDatabase getConnection(Context context){
        if (instance.db == null){
            instance.db = Room.databaseBuilder(context,
                    CrystalDatabase.class, "CrystalWallet.db")
                    .addMigrations(CrystalDatabase.MIGRATION_1_2)
                    .build();
        }

        return instance.db;
    }
}
