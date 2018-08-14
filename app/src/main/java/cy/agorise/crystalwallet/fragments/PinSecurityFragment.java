package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.application.CrystalSecurityMonitor;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.util.PasswordManager;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.PinSecurityValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

/**
 * Created by xd on 1/18/18.
 */

public class PinSecurityFragment extends Fragment implements UIValidatorListener {

    @BindView(R.id.etNewPin)
    EditText etNewPin;
    @BindView(R.id.etConfirmPin)
    EditText etConfirmPin;

    @BindView(R.id.tvNewPinError)
    TextView tvNewPinError;
    @BindView(R.id.tvConfirmPinError)
    TextView tvConfirmPinError;

    GeneralSettingListViewModel generalSettingListViewModel;
    GeneralSetting passwordGeneralSetting;
    PinSecurityValidator pinSecurityValidator;

    public PinSecurityFragment() {
        // Required empty public constructor
    }

    public static PinSecurityFragment newInstance() {
        PinSecurityFragment fragment = new PinSecurityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pin_security, container, false);
        ButterKnife.bind(this, v);

        generalSettingListViewModel = ViewModelProviders.of(this).get(GeneralSettingListViewModel.class);
        LiveData<List<GeneralSetting>> generalSettingsLiveData = generalSettingListViewModel.getGeneralSettingList();

        pinSecurityValidator = new PinSecurityValidator(this.getContext(), etNewPin, etConfirmPin);
        pinSecurityValidator.setListener(this);

        return v;
    }

    @OnTextChanged(value = R.id.etNewPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterNewPinChanged(Editable editable) {
        this.pinSecurityValidator.validate();
    }

    @OnTextChanged(value = R.id.etConfirmPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterConfirmPinChanged(Editable editable) {
        this.pinSecurityValidator.validate();
    }

    public void clearFields(){
        if (!this.etNewPin.getText().toString().equals("")) {
            this.etNewPin.setText("");
        }
        if (!this.etConfirmPin.getText().toString().equals("")) {
            this.etConfirmPin.setText("");
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final PinSecurityFragment fragment = this;

        this.getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == etNewPin){
                    tvNewPinError.setText("");
                } else if (field.getView() == etConfirmPin){
                    tvConfirmPinError.setText("");
                }

                if (pinSecurityValidator.isValid()){
                    CharSequence text = "Your password has been sucessfully changed!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();

                    savePassword(etNewPin.getText().toString());


                    clearFields();
                }
            }
        });
    }

    public void savePassword(String password) {
        String passwordEncripted = PasswordManager.encriptPassword(password);
        CrystalSecurityMonitor.getInstance(null).setPasswordSecurity(passwordEncripted);
        CrystalSecurityMonitor.getInstance(null).callPasswordRequest(this.getActivity());
    }

    @Override
    public void onValidationFailed(final ValidationField field) {
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (field.getView() == etNewPin){
                    tvNewPinError.setText(field.getMessage());
                } else if (field.getView() == etConfirmPin){
                    tvConfirmPinError.setText(field.getMessage());
                }
            }
        });
    }
}
