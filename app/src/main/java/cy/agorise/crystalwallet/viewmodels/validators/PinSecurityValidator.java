package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

import cy.agorise.crystalwallet.viewmodels.validators.validationfields.BitsharesAccountNameDoesntExistsValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.CurrentPinValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinConfirmationValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinValidationField;

/**
 * Created by Henry Varona on 1/28/2018.
 */

public class PinSecurityValidator extends UIValidator {

    public PinSecurityValidator(Context context, EditText currentPinEdit, EditText newPinEdit, EditText newPinConfirmationEdit){
        super(context);
        this.addField(new CurrentPinValidationField(currentPinEdit));
        this.addField(new PinValidationField(newPinEdit));
        this.addField(new PinConfirmationValidationField(newPinEdit,newPinConfirmationEdit));
    }
}
