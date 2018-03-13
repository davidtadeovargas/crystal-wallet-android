package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

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
        if (!newValue.equals("")) {
            if (!newValue.equals(this.getLastValue())) {
                this.setLastValue(newValue);
                this.startValidating();

                if (newValue.length() < 6) {
                    this.setMessageForValue(newValue, this.validator.getContext().getResources().getString(R.string.pin_number_warning));
                    this.setValidForValue(newValue, false);
                } else {
                    this.setValidForValue(newValue, true);
                }
            }
        } else {
            this.setLastValue("");
            this.startValidating();
            this.setMessageForValue("", "");
            this.setValidForValue("", false);
        }
    }
}
