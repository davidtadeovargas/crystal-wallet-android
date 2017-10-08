package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

import cy.agorise.crystalwallet.R;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class PinValidationField extends ValidationField {

    private EditText pinField;

    public PinValidationField(EditText pinField){
        super(pinField);
        this.pinField = pinField;
    }

    public void validate(){
        String newValue = pinField.getText().toString();
        if (newValue != this.getLastValue()) {
            this.setLastValue(newValue);
            this.startValidating();

            if (newValue.length() < 6) {
                this.setValidForValue(newValue, false);
                this.setMessage(this.validator.getContext().getResources().getString(R.string.pin_number_warning));
                this.validator.validationFailed(this);
            } else {
                this.setValidForValue(newValue, true);
                this.validator.validationSucceeded(this);
            }
        }
    }
}
