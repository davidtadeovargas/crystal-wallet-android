package cy.agorise.crystalwallet.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.dialogs.material.CrystalDialog
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests
import cy.agorise.crystalwallet.requestmanagers.ValidateCreateBitsharesAccountRequest
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces.UIValidatorListener
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.BitsharesAccountNameValidation
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.BitsharesAccountNameValidation.OnAccountExist
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.CustomValidationField
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.PinDoubleConfirmationValidationField
import cy.agorise.crystalwallet.views.natives.CustomTextInputEditText
import kotlinx.android.synthetic.main.create_seed.*


/*
* This activity creates a new account with some security concerns
* */
class CreateSeedActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Assign the view to this controller
        * */
        setContentView(R.layout.create_seed)

        /*
        * Initialice butterknife MVC
        * */
        ButterKnife.bind(this)

        /*
         * Add the controls to the validator
         * */
        this.fieldsValidator.add(tietPin)
        this.fieldsValidator.add(tietPinConfirmation)
        this.fieldsValidator.add(tietAccountName)

        /*
        * Validations listener
        * */
        val uiValidatorListener = object : UIValidatorListener {

            override fun onValidationSucceeded(customValidationField: CustomValidationField) {

                try {

                    /*
                     * Remove error
                     * */
                    runOnUiThread {
                        val customTextInputEditText = customValidationField.currentView as CustomTextInputEditText
                        customTextInputEditText.error = null
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }


                /*
                 * Validate if can continue
                 * */
                validateFieldsToContinue()
            }

            override fun onValidationFailed(customValidationField: CustomValidationField) {

                /*
                 * Set error label
                 * */
                runOnUiThread {
                    val customTextInputEditText = customValidationField.currentView as CustomTextInputEditText
                    customTextInputEditText.error = customTextInputEditText.fieldValidatorModel.message
                }
            }
        }

        /*
        * Create the pin double validation
        * */
        val pinDoubleConfirmationValidationField = PinDoubleConfirmationValidationField(this, tietPin, tietPinConfirmation, uiValidatorListener)

        /*
        * Listener for the validation for success or fail
        * */
        tietPin?.setUiValidator(pinDoubleConfirmationValidationField) //Validator for the field
        tietPinConfirmation?.setUiValidator(pinDoubleConfirmationValidationField) //Validator for the field

        /*
        * Account name validator
        * */
        val bitsharesAccountNameValidation = BitsharesAccountNameValidation(this, tietAccountName, uiValidatorListener)
        val onAccountExist = object : OnAccountExist {
            override fun onAccountExists() {
                runOnUiThread { Toast.makeText(globalActivity, resources.getString(R.string.account_name_already_exist), Toast.LENGTH_LONG).show() }
            }

        }
        bitsharesAccountNameValidation.setOnAccountExist(onAccountExist)
        tietAccountName?.setUiValidator(bitsharesAccountNameValidation)

        /*This button should not be enabled till all the fields be correctly filled*/
        disableCreate()

        /*
        * Set the focus on the fisrt field and show keyboard
        * */
        tilPin?.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(tilPin, InputMethodManager.SHOW_IMPLICIT)
    }


    @OnTextChanged(value = R.id.tietPin, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    internal fun afterPinChanged(editable: Editable) {
        this.fieldsValidator.validate()

        /*
         * Validate continue to create account
         * */
        validateFieldsToContinue()
    }

    @OnTextChanged(value = R.id.tietPinConfirmation, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    internal fun afterPinConfirmationChanged(editable: Editable) {
        this.fieldsValidator.validate()

        /*
         * Validate continue to create account
         * */
        validateFieldsToContinue()
    }

    @OnTextChanged(value = R.id.tietAccountName, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    internal fun afterAccountNameChanged(editable: Editable) {
        this.fieldsValidator.validate()

        /*
         * Always disable till the server response comes
         * */
        disableCreate()
    }

    @OnClick(R.id.btnCancel)
    fun cancel() {

        /*
        * Exit of the activity
        * */
        this.finish()
    }

    @OnClick(R.id.btnCreate)
    fun createSeed() {

        // Make request to create a bitshare account
        val request = ValidateCreateBitsharesAccountRequest(tietAccountName?.getText().toString(), applicationContext)

        //DTVV: Friday 27 July 2018
        //Makes dialog to tell the user that the account is been created
        val creatingAccountMaterialDialog = CrystalDialog(this)
        creatingAccountMaterialDialog.setText(this.resources.getString(R.string.window_create_seed_DialogMessage))
        creatingAccountMaterialDialog.build()
        this@CreateSeedActivity.runOnUiThread { creatingAccountMaterialDialog.show() }
        request.setListener {
            creatingAccountMaterialDialog.dismiss()
            if (request.status == ValidateCreateBitsharesAccountRequest.StatusCode.SUCCEEDED) {
                val accountSeed = request.account
                val intent = Intent(applicationContext, BackupSeedActivity::class.java)
                intent.putExtra("SEED_ID", accountSeed.id)
                startActivity(intent)
            } else {
                fieldsValidator.validate()
            }
        }

        val thread = object : Thread() {
            override fun run() {

                /*
                *
                * Run thread*/
                CryptoNetInfoRequests.getInstance().addRequest(request)
            }
        }

        thread.start()
    }

    /*
    * Validate that all is complete to continue to create
    * */
    private fun validateFieldsToContinue() {

        var result = false //Contains the final result

        val pinValid: Boolean? = this.tietPin?.getFieldValidatorModel()?.isValid
        val pinConfirmationValid = this.tietPinConfirmation?.getFieldValidatorModel()?.isValid
        val pinAccountNameValid = this.tietAccountName?.getFieldValidatorModel()?.isValid

        if (pinValid!! &&
                pinConfirmationValid!! &&
                pinAccountNameValid!!) {
            result = true //Validation is correct
        }


        /*
        * If the result is true so the user can continue to the creation of the account
        * */
        if (result) {
            enableCreate()
        } else {
            disableCreate()
        }
    }

    /*
    * Enable create button
    * */
    private fun enableCreate() {
        btnCreate?.setEnabled(true)
        btnCreate?.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }

    /*
     * Disable create button
     * */
    private fun disableCreate() {
        btnCreate?.setEnabled(false)
        btnCreate?.setBackground(resources.getDrawable(R.drawable.disable_style))
    }
}