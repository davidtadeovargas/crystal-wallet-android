package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
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
        String mixedValue = newValue+"_"+newConfirmationValue;
        if (mixedValue != this.getLastValue()) {
            this.setLastValue(mixedValue);
            this.startValidating();


            if (!newConfirmationValue.equals(newValue)){
                this.setValidForValue(mixedValue,false);
                this.setMessage(this.validator.getContext().getResources().getString(R.string.mismatch_pin));
                this.validator.validationFailed(this);
            } else {
                this.setValidForValue(mixedValue, true);
                this.validator.validationSucceeded(this);
            }
        }
    }
}
