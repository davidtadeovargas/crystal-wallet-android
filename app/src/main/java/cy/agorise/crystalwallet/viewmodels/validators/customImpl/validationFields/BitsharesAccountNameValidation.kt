package cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields

import android.app.Activity
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator
import cy.agorise.crystalwallet.dialogs.material.CrystalDialog
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests
import cy.agorise.crystalwallet.requestmanagers.ValidateExistBitsharesAccountRequest
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces.UIValidator
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces.UIValidatorListener
import cy.agorise.crystalwallet.views.natives.CustomTextInputEditText


/*
*
* Validation layer for Account Name
* */
class BitsharesAccountNameValidation : CustomValidationField, UIValidator {

    /*
    * Contains the field to validate
    * */
    private val accountNameField: CustomTextInputEditText

    /*
     * Interface to validate when an account exist an take over control it
     * */
    private var onAccountExist: OnAccountExist? = null





    constructor (   activity: Activity,
                    accountNameField: CustomTextInputEditText,
                    uiValidatorListener: UIValidatorListener) : super(activity) {

        this.accountNameField = accountNameField
        this.uiValidatorListener = uiValidatorListener

        /*
        * The current view for errors will be this
        * */
        this.currentView = this.accountNameField
    }

    override fun validate() {

        val newValue = accountNameField.text.toString()

        /*
        Contains the validation result
        */
        var result = true

        /*
         * Validate empty field
         * */
        if (newValue == "") {

            /*
            * Validation not passed
            * */
            result = false
            accountNameField.fieldValidatorModel.setInvalid()
            accountNameField.fieldValidatorModel.message = this.accountNameField.resources.getString(R.string.create_account_window_err_account_empty)
        } else {

            /*
            * Remove error
            * */
            accountNameField.error = null

            /*
                Validate at least min length
            */
            if (newValue.length < 10) {

                /*
                 * Validation not passed
                 * */
                result = false
                accountNameField.fieldValidatorModel.setInvalid()
                accountNameField.fieldValidatorModel.message = this.accountNameField.resources.getString(R.string.create_account_window_err_min_account_name_len)
            } else {

                /*
                 * Remove error
                 * */
                accountNameField.error = null

                /*
                    Validate at least one character
                */
                if (!newValue.matches(".*[a-zA-Z]+.*".toRegex())) {

                    /*
                     * Validation not passed
                     * */
                    result = false
                    accountNameField.fieldValidatorModel.setInvalid()
                    accountNameField.fieldValidatorModel.message = this.accountNameField.resources.getString(R.string.create_account_window_err_at_least_one_character)
                } else {

                    /*
                     * Remove error
                     * */
                    accountNameField.error = null

                    /*
                        Validate at least one number for the account string
                    */
                    if (!newValue.matches(".*\\d+.*".toRegex())) {

                        /*
                         * Validation not passed
                         * */
                        result = false
                        accountNameField.fieldValidatorModel.setInvalid()
                        accountNameField.fieldValidatorModel.message = this.accountNameField.resources.getString(R.string.create_account_window_err_at_least_one_number)
                    } else {

                        /*
                         * Remove error
                         * */
                        accountNameField.error = null


                        /*
                            Validate at least one middle script
                        */
                        if (!newValue.contains("-")) {

                            /*
                             * Validation not passed
                             * */
                            result = false
                            accountNameField.fieldValidatorModel.setInvalid()
                            accountNameField.fieldValidatorModel.message = this.accountNameField.resources.getString(R.string.create_account_window_err_at_least_one_script)
                        } else {

                            /*
                             * Remove error
                             * */
                            accountNameField.error = null
                        }
                    }
                }
            }
        }

        /*
        * If passed first validations
        * */
        if (!result) {

            /*
             * Deliver result
             * */
            if (uiValidatorListener != null) {
                uiValidatorListener.onValidationFailed(this)
            }
        } else {

            /*
             * Show the dialog for connection with the server
             * */
            val creatingAccountMaterialDialog = CrystalDialog(activity)
            creatingAccountMaterialDialog.setText(activity.resources.getString(R.string.window_create_seed_Server_validation))
            creatingAccountMaterialDialog.build()
            creatingAccountMaterialDialog.show()


            val request = ValidateExistBitsharesAccountRequest(newValue)
            request.setListener {
                /*
    * Dismiss the dialog of loading
    * */
                creatingAccountMaterialDialog.dismiss()

                if (request.accountExists) {

                    /*
         *   The account exists and is not valid
         * */
                    accountNameField.fieldValidatorModel.setInvalid()
                    accountNameField.fieldValidatorModel.message = accountNameField.resources.getString(R.string.account_name_already_exist)

                    /*
         *   Deliver the response
         * */
                    if (uiValidatorListener != null) {
                        uiValidatorListener.onValidationFailed(globalCustomValidationField)
                    }

                    /*
        * Deliver response to local callback
        * */
                    if (onAccountExist != null) {
                        onAccountExist!!.onAccountExists()
                    }

                } else {

                    /*
        * Passed all validations
        * */
                    accountNameField.fieldValidatorModel.setValid()

                    /*
         *   Deliver the response
         * */
                    if (uiValidatorListener != null) {
                        uiValidatorListener.onValidationSucceeded(globalCustomValidationField)
                    }
                }
            }

            /*
                * Listener for websocket error
                * */
            GrapheneApiGenerator.setActivity(activity); //Set the activity to catch errors
            val onErrorWebSocker = object : GrapheneApiGenerator.OnErrorWebSocket {
                override fun onError(exception: java.lang.Exception?) {

                    /*
                    *
                    * Hide loading dialog
                    *
                    * */
                    creatingAccountMaterialDialog.dismiss();

                }

            }
            GrapheneApiGenerator.setOnErrorWebSocket(onErrorWebSocker); //Set the activity to catch errors


            /*
            *
            * Run thread*/
            CryptoNetInfoRequests.getInstance().addRequest(request)
        }/*
        * Passed initial validations, next final validations
        * */
    }


    /*
    * Setters and getters
    * */
    fun setOnAccountExist(onAccountExist: OnAccountExist) {
        this.onAccountExist = onAccountExist
    }
    /*
     * End of setters and getters
     * */

    /*
    * Interface to validate when an account exist an take over control it
    * */
    open interface OnAccountExist {
        fun onAccountExists()
    }
}