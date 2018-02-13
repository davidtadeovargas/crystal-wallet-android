package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ContactNameValidationField;

/**
 * Created by Henry Varona on 2/11/2018.
 */

public class ModifyContactValidator extends UIValidator {

    public ModifyContactValidator(Context context, Contact contact, EditText nameEdit){
        super(context);
        this.addField(new ContactNameValidationField(nameEdit, contact));
    }
}
