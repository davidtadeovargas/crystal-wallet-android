package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.application.CrystalSecurityMonitor;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.util.PasswordManager;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

public class PatternRequestActivity extends AppCompatActivity {
    private String patternEncrypted;

    @Override
    public void onBackPressed() {
        //Do nothing to prevent the user to use the back button
    }

    @BindView(R.id.patternLockView)
    PatternLockView patternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_request);
        ButterKnife.bind(this);

        GeneralSettingListViewModel generalSettingListViewModel = ViewModelProviders.of(this).get(GeneralSettingListViewModel.class);
        LiveData<List<GeneralSetting>> generalSettingsLiveData = generalSettingListViewModel.getGeneralSettingList();

        final PatternRequestActivity thisActivity = this;

        generalSettingsLiveData.observe(this, new Observer<List<GeneralSetting>>() {
            @Override
            public void onChanged(@Nullable List<GeneralSetting> generalSettings) {
                patternEncrypted = "";

                if (generalSettings != null){
                    for (GeneralSetting generalSetting:generalSettings) {
                        if (generalSetting.getName().equals(GeneralSetting.SETTING_PATTERN)){
                            if (!generalSetting.getValue().isEmpty()){
                                patternEncrypted = generalSetting.getValue();

                                patternLockView.addPatternLockListener(new PatternLockViewListener() {
                                    @Override
                                    public void onStarted() {

                                    }

                                    @Override
                                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                                    }

                                    @Override
                                    public void onComplete(List<PatternLockView.Dot> pattern) {
                                        if (PasswordManager.checkPassword(patternEncrypted,patternToString(pattern))){
                                            if (CrystalSecurityMonitor.getInstance(null).is2ndFactorSet()) {
                                                CrystalSecurityMonitor.getInstance(null).call2ndFactor(thisActivity);
                                            } else {
                                                thisActivity.finish();
                                            }
                                        } else {
                                            patternLockView.clearPattern();
                                            patternLockView.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void onCleared() {

                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    public String patternToString(List<PatternLockView.Dot> pattern){
        String patternString = "";
        for (PatternLockView.Dot nextDot : pattern){
            patternString = patternString+(nextDot.getRow()*3+nextDot.getColumn());
        }

        return patternString;
    }
}


