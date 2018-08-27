package cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces

import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.CustomValidationField


/*
* Listener to deliver response to controls from inner validations
* */
interface UIValidatorListener {


    fun onValidationFailed(customValidationField: CustomValidationField)
    fun onValidationSucceeded(customValidationField: CustomValidationField)
}