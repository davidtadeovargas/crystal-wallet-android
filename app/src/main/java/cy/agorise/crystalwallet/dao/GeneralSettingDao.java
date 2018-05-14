package cy.agorise.crystalwallet.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.GeneralSetting;

/**
 * Created by Henry Varona on 10/9/2017.
 */

@Dao
public interface GeneralSettingDao {

    @Query("SELECT * FROM general_setting")
    LiveData<List<GeneralSetting>> getAll();

    @Query("SELECT * FROM general_setting WHERE name = :name")
    LiveData<GeneralSetting> getByName(String name);

    @Query("SELECT * FROM general_setting WHERE name = :name")
    GeneralSetting getSettingByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertGeneralSettings(GeneralSetting... generalSettings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGeneralSetting(GeneralSetting generalSetting);

    @Delete
    public void deleteGeneralSettings(GeneralSetting... generalSettings);

    @Query("DELETE FROM general_setting WHERE name = :name")
    public void deleteByName(String name);
}
