package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import cy.agorise.crystalwallet.viewmodels.validators.validationfields.BitsharesAccountMnemonicValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.BitsharesAccountNameDoesntExistsValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.BitsharesAccountNameValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinConfirmationValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.PinValidationField;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class CreateSeedValidator extends UIValidator {

    public CreateSeedValidator(Context context, EditText pinEdit, EditText pinConfirmationEdit, EditText bitsharesAccountNameEdit){
        super(context);
        this.addField(new PinValidationField(pinEdit));
        this.addField(new PinConfirmationValidationField(pinEdit,pinConfirmationEdit));
        this.addField(new BitsharesAccountNameDoesntExistsValidationField(bitsharesAccountNameEdit));
    }
}
