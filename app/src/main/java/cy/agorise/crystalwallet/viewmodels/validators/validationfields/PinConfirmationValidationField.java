package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class PinConfirmationValidationField extends ValidationField {

    private EditText pinField;
    private EditText pinConfirmationField;

    public PinConfirmationValidationField(EditText pinField, EditText pinConfirmationField){
        super(pinConfirmationField);
        this.pinField = pinField;
        this.pinConfirmationField = pinConfirmationField;
    }

    public void validate(){
        String newConfirmationValue = pinConfirmationField.getText().toString();
        String newValue = pinField.getText().toString();
        String mixedValue = newValue + "_" + newConfirmationValue;

        if (!newConfirmationValue.equals("")) {
            if (!mixedValue.equals(this.getLastValue())) {
                this.setLastValue(mixedValue);
                this.startValidating();
                if (!newConfirmationValue.equals(newValue)) {
                    this.setMessageForValue(mixedValue, this.validator.getContext().getResources().getString(R.string.mismatch_pin));
                    this.setValidForValue(mixedValue, false);
                } else {
                    this.setValidForValue(mixedValue, true);
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
