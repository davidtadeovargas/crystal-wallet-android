package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.util.PasswordManager;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.PinSecurityValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

/**
 * Created by xd on 1/18/18.
 */

public class PatternSecurityFragment extends Fragment {

    GeneralSettingListViewModel generalSettingListViewModel;
    GeneralSetting patternGeneralSetting;
    //PatternSecurityValidator patternSecurityValidator;

    @BindView(R.id.patternLockView)
    PatternLockView patternLockView;
    @BindView(R.id.tvPatternText)
    TextView tvPatternText;

    private PatternLockViewListener actualPatternListener;
    private String patternEntered;

    public PatternSecurityFragment() {
        // Required empty public constructor
    }

    public static PatternSecurityFragment newInstance() {
        PatternSecurityFragment fragment = new PatternSecurityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pattern_security, container, false);
        ButterKnife.bind(this, v);

        generalSettingListViewModel = ViewModelProviders.of(this).get(GeneralSettingListViewModel.class);
        LiveData<List<GeneralSetting>> generalSettingsLiveData = generalSettingListViewModel.getGeneralSettingList();

        generalSettingsLiveData.observe(this, new Observer<List<GeneralSetting>>() {
            @Override
            public void onChanged(@Nullable List<GeneralSetting> generalSettings) {
                boolean founded = false;

                if (generalSettings != null){
                    for (GeneralSetting generalSetting:generalSettings) {
                        if (generalSetting.getName().equals(GeneralSetting.SETTING_PATTERN)){
                            founded = true;
                            if (!generalSetting.getValue().isEmpty()){
                                patternGeneralSetting = generalSetting;
                                showEnterPatternUI();
                            } else {
                                showNewPatternUI();
                            }
                            break;
                        }
                    }
                    if (!founded){
                        showNewPatternUI();
                    }
                } else {
                    showNewPatternUI();
                }
            }
        });

        return v;
    }

    public String patternToString(List<PatternLockView.Dot> pattern){
        String patternString = "";
        for (PatternLockView.Dot nextDot : pattern){
            patternString = patternString+(nextDot.getRow()*3+nextDot.getColumn());
        }

        return patternString;
    }

    public void removePatternListener(){
        if (actualPatternListener != null){
            patternLockView.removePatternLockListener(actualPatternListener);
            actualPatternListener = null;
        }
    }

    public void showNewPatternUI(){
        removePatternListener();
        patternLockView.clearPattern();
        tvPatternText.setText("Enter new pattern");

        actualPatternListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                patternEntered = patternToString(pattern);
                showConfirmPatternUI();
            }

            @Override
            public void onCleared() {

            }
        };
        patternLockView.addPatternLockListener(actualPatternListener);
    }

    public void showConfirmPatternUI(){
        removePatternListener();
        patternLockView.clearPattern();
        patternLockView.requestFocus();
        tvPatternText.setText("Confirm new pattern");

        actualPatternListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (patternEntered.equals(patternToString(pattern))){
                    savePattern(patternEntered);
                    showEnterPatternUI();
                }
            }

            @Override
            public void onCleared() {

            }
        };
        patternLockView.addPatternLockListener(actualPatternListener);
    }

    public void savePattern(String pattern){
        String patternEncripted = PasswordManager.encriptPassword(pattern);

        if (patternGeneralSetting == null) {
            patternGeneralSetting = new GeneralSetting();
            patternGeneralSetting.setName(GeneralSetting.SETTING_PATTERN);
        }

        patternGeneralSetting.setValue(patternEncripted);
        generalSettingListViewModel.saveGeneralSetting(patternGeneralSetting);
    }

    public void showEnterPatternUI(){
        removePatternListener();
        patternLockView.clearPattern();
        tvPatternText.setText("Enter old pattern");

        actualPatternListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (PasswordManager.checkPassword(patternGeneralSetting.getValue(),patternToString(pattern))){
                    showNewPatternUI();
                }
            }

            @Override
            public void onCleared() {

            }
        };
        patternLockView.addPatternLockListener(actualPatternListener);
    }
}
