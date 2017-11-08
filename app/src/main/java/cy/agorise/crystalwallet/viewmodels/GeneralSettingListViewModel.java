package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.GeneralSetting;

/**
 * Created by Henry Varona on 6/11/2017.
 */

public class GeneralSettingListViewModel extends AndroidViewModel {

    private LiveData<List<GeneralSetting>> generalSettingList;
    private CrystalDatabase db;

    public GeneralSettingListViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
        generalSettingList = this.db.generalSettingDao().getAll();
    }

    public LiveData<List<GeneralSetting>> getGeneralSettingList(){
        return this.generalSettingList;
    }

    public void saveGeneralSetting(GeneralSetting generalSetting){
        this.db.generalSettingDao().insertGeneralSetting(generalSetting);
    }

    public void saveGeneralSettings(GeneralSetting... generalSettings){
        this.db.generalSettingDao().insertGeneralSettings(generalSettings);
    }
}
