package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class ImportSeedValidator extends UIValidator {

    public ImportSeedValidator(Context context, EditText pinEdit, EditText pinConfirmationEdit, EditText bitsharesAccountNameEdit, EditText mnemonicEdit){
        super(context);
        this.addField(new PinValidationField(pinEdit));
        this.addField(new PinConfirmationValidationField(pinEdit,pinConfirmationEdit));
        this.addField(new BitsharesAccountNameValidationField(bitsharesAccountNameEdit));
        this.addField(new BitsharesAccountMnemonicValidationField(mnemonicEdit,bitsharesAccountNameEdit));
    }
}
