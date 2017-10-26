package cy.agorise.crystalwallet.viewmodels.validators;

import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public interface UIValidatorListener {

    public void onValidationSucceeded(ValidationField field);
    public void onValidationFailed(ValidationField field);
}
