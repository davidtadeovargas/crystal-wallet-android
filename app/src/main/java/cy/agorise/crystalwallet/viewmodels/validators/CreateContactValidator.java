package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

import cy.agorise.crystalwallet.viewmodels.validators.validationfields.BitsharesAccountNameDoesntExistsValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ContactNameValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinConfirmationValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinValidationField;

/**
 * Created by Henry Varona on 2/2/2018.
 */

public class CreateContactValidator extends UIValidator {

    public CreateContactValidator(Context context, EditText nameEdit){
        super(context);
        this.addField(new ContactNameValidationField(nameEdit));
    }
}
