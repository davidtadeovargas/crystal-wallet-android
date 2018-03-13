package cy.agorise.crystalwallet.application;

import android.app.Activity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import cy.agorise.crystalwallet.activities.PinRequestActivity;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by Henry Varona on 27/1/2018.
 */

public class CrystalSecurityMonitor implements Application.ActivityLifecycleCallbacks {
    private int numStarted = 0;
    private String passwordEncrypted;

    public CrystalSecurityMonitor(final FragmentActivity fragmentActivity){
        GeneralSettingListViewModel generalSettingListViewModel = ViewModelProviders.of(fragmentActivity).get(GeneralSettingListViewModel.class);
        LiveData<List<GeneralSetting>> generalSettingsLiveData = generalSettingListViewModel.getGeneralSettingList();

        generalSettingsLiveData.observe(fragmentActivity, new Observer<List<GeneralSetting>>() {
            @Override
            public void onChanged(@Nullable List<GeneralSetting> generalSettings) {
                boolean founded = false;
                passwordEncrypted = "";

                if (generalSettings != null){
                    for (GeneralSetting generalSetting:generalSettings) {
                        if (generalSetting.getName().equals(GeneralSetting.SETTING_PASSWORD)){
                            founded = true;
                            if (!generalSetting.getValue().isEmpty()){
                                passwordEncrypted = generalSetting.getValue();
                                callPasswordRequest(fragmentActivity);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            if ((this.passwordEncrypted != null) && (!this.passwordEncrypted.equals(""))) {
                callPasswordRequest(activity);
            }
        }
        numStarted++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            if ((this.passwordEncrypted != null) && (!this.passwordEncrypted.equals(""))) {
                callPasswordRequest(activity);
            }
        }
    }

    public void callPasswordRequest(Activity activity){
        if ((!activity.getIntent().hasExtra("ACTIVITY_TYPE")) || (!activity.getIntent().getStringExtra("ACTIVITY_TYPE").equals("PASSWORD_REQUEST"))) {
            Intent intent = new Intent(activity, PinRequestActivity.class);
            intent.putExtra("ACTIVITY_TYPE", "PASSWORD_REQUEST");
            activity.startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        //
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //
    }



}
