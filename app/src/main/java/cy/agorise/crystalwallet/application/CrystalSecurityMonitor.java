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

import cy.agorise.crystalwallet.activities.PatternRequestActivity;
import cy.agorise.crystalwallet.activities.PinRequestActivity;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.notifiers.CrystalWalletNotifier;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by Henry Varona on 27/1/2018.
 */

public class CrystalSecurityMonitor implements Application.ActivityLifecycleCallbacks {
    private int numStarted = 0;
    private String passwordEncrypted;
    private String patternEncrypted;

    private static CrystalSecurityMonitor instance;
    private GeneralSettingListViewModel generalSettingListViewModel;

    private CrystalSecurityMonitor(final FragmentActivity fragmentActivity){
        //For security reasons, this code is not asynchronous because the pattern or password data
        //must be retrieved before the principal activity starts
        generalSettingListViewModel = ViewModelProviders.of(fragmentActivity).get(GeneralSettingListViewModel.class);

        this.passwordEncrypted = "";
        this.patternEncrypted = "";
        GeneralSetting passwordGeneralSetting = generalSettingListViewModel.getGeneralSettingByName(GeneralSetting.SETTING_PASSWORD);;
        GeneralSetting patternGeneralSetting = generalSettingListViewModel.getGeneralSettingByName(GeneralSetting.SETTING_PATTERN);;

        if (passwordGeneralSetting != null){
            this.passwordEncrypted = passwordGeneralSetting.getValue();
        }
        if (patternGeneralSetting != null){
            this.patternEncrypted = patternGeneralSetting.getValue();
        }
    }

    public static CrystalSecurityMonitor getInstance(final FragmentActivity fragmentActivity){
        if (instance == null){
            instance = new CrystalSecurityMonitor(fragmentActivity);
        }

        return instance;
    }

    public void clearSecurity(){
        this.patternEncrypted = "";
        this.passwordEncrypted = "";

        generalSettingListViewModel.deleteGeneralSettingByName(GeneralSetting.SETTING_PASSWORD);
        generalSettingListViewModel.deleteGeneralSettingByName(GeneralSetting.SETTING_PATTERN);
    }

    public void setPasswordSecurity(String password){
        clearSecurity();
        this.passwordEncrypted = password;
        GeneralSetting passwordGeneralSetting = new GeneralSetting();
        passwordGeneralSetting.setName(GeneralSetting.SETTING_PASSWORD);
        passwordGeneralSetting.setValue(password);

        generalSettingListViewModel.saveGeneralSetting(passwordGeneralSetting);
    }

    public void setPatternEncrypted(String pattern){
        clearSecurity();
        this.patternEncrypted = pattern;
        GeneralSetting patternGeneralSetting = new GeneralSetting();
        patternGeneralSetting.setName(GeneralSetting.SETTING_PATTERN);
        patternGeneralSetting.setValue(pattern);

        generalSettingListViewModel.saveGeneralSetting(patternGeneralSetting);
    }

    public String actualSecurity(){
        if ((this.patternEncrypted != null) && (!this.patternEncrypted.equals(""))){
            return GeneralSetting.SETTING_PATTERN;
        } else if ((this.passwordEncrypted != null) && (!this.passwordEncrypted.equals(""))){
            return GeneralSetting.SETTING_PASSWORD;
        }

        return "";
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            if (!actualSecurity().equals("")){
                callPasswordRequest(activity);
            }
        }
        numStarted++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            if (!actualSecurity().equals("")){
                callPasswordRequest(activity);
            }
        }
    }

    public void callPasswordRequest(Activity activity){
        if ((!activity.getIntent().hasExtra("ACTIVITY_TYPE")) || (!activity.getIntent().getStringExtra("ACTIVITY_TYPE").equals("PASSWORD_REQUEST"))) {
            Intent intent = null;
            if ((this.passwordEncrypted != null) && (!this.passwordEncrypted.equals(""))) {
                intent = new Intent(activity, PinRequestActivity.class);
            } else if ((this.patternEncrypted != null) && (!this.patternEncrypted.equals(""))) {
                intent = new Intent(activity, PatternRequestActivity.class);
            }
            if (intent != null) {
                intent.putExtra("ACTIVITY_TYPE", "PASSWORD_REQUEST");
                activity.startActivity(intent);
            }
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
