package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dialogs.material.LoadingDialog;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.ValidateCreateBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.BitsharesAccountNameValidation;
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.CustomValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.customImpl.validationFields.PinDoubleConfirmationValidationField;
import cy.agorise.crystalwallet.views.natives.CustomTextInputEditText;



/*
*   Commented code backup
*
*
*   alertBuilder.setTitle("Processing");
    alertBuilder.setMessage("Creating Bitshares Account");

    ------

    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateSeedActivity.this,R.style.AppTheme);
            alertBuilder.setView(R.layout.progress_creating_account);
            final AlertDialog processDialog = alertBuilder.create();
            CreateSeedActivity.this.runOnUiThread(new Runnable() { //Run on UI Thread
                @Override
                public void run() {
                    processDialog.setCancelable(false);
                    processDialog.show();
                    processDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            });

 */





public class CreateSeedActivity extends CustomActivity {

    AccountSeedViewModel accountSeedViewModel;

    @BindView(R.id.tilPin)
    TextInputLayout tilPin;

    @BindView(R.id.tietPin)
    CustomTextInputEditText tietPin;

    @BindView(R.id.tilPinConfirmation)
    TextInputLayout tilPinConfirmation;

    @BindView(R.id.tietPinConfirmation)
    CustomTextInputEditText tietPinConfirmation;

    @BindView(R.id.tilAccountName)
    TextInputLayout tilAccountName;

    @BindView (R.id.tietAccountName)
    CustomTextInputEditText tietAccountName;

    @BindView(R.id.btnCreate)
    Button btnCreate;

    @BindView(R.id.btnCancel)
    Button btnCancel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_seed);

        /*
        * Initialice butterknife MVC
        * */
        ButterKnife.bind(this);

        /*This button should not be enabled till all the fields be correctly filled*/
        disableCreate();

        /*
         * Add the controls to the validator
         * */
        this.fieldsValidator.add(tietPin);
        this.fieldsValidator.add(tietPinConfirmation);
        this.fieldsValidator.add(tietAccountName);

        /*
        * Validations listener
        * */
        final UIValidatorListener uiValidatorListener = new UIValidatorListener() {

            @Override
            public void onValidationSucceeded(final CustomValidationField customValidationField) {

                try{

                    /*
                     * Remove error
                     * */
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            final CustomTextInputEditText customTextInputEditText = (CustomTextInputEditText) customValidationField.getCurrentView();
                            customTextInputEditText.setError(null);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }


                /*
                 * Validate if can continue
                 * */
                validateFieldsToContinue();
            }

            @Override
            public void onValidationFailed(final CustomValidationField customValidationField) {

                /*
                 * Set error label
                 * */
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final CustomTextInputEditText customTextInputEditText = (CustomTextInputEditText) customValidationField.getCurrentView();
                        customTextInputEditText.setError(customTextInputEditText.getFieldValidatorModel().getMessage());
                    }
                });
            }
        };

        /*
        * Create the pin double validation
        * */
        final PinDoubleConfirmationValidationField pinDoubleConfirmationValidationField = new PinDoubleConfirmationValidationField(this,tietPin,tietPinConfirmation,uiValidatorListener);

        /*
        * Listener for the validation for success or fail
        * */
        tietPin.setUiValidator(pinDoubleConfirmationValidationField); //Validator for the field
        tietPinConfirmation.setUiValidator(pinDoubleConfirmationValidationField); //Validator for the field

        /*
        * Account name validator
        * */
        final BitsharesAccountNameValidation bitsharesAccountNameValidation = new BitsharesAccountNameValidation(this,tietAccountName,uiValidatorListener);
        bitsharesAccountNameValidation.setOnAccountExist(new BitsharesAccountNameValidation.OnAccountExist() {
            @Override
            public void onAccountExists() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(globalActivity,getResources().getString(R.string.account_name_already_exist), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        tietAccountName.setUiValidator(bitsharesAccountNameValidation);

        /*
        * This button initially is not enabled til all the field validation be ok
        * */
        disableCreate();

        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);

        /*
        * Set the focus on the fisrt field and show keyboard
        * */
        tilPin.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tilPin, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnTextChanged(value = R.id.tietPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinChanged(Editable editable) {
        this.fieldsValidator.validate();

        /*
         * Validate continue to create account
         * */
        validateFieldsToContinue();
    }
    @OnTextChanged(value = R.id.tietPinConfirmation,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinConfirmationChanged(Editable editable) {
        this.fieldsValidator.validate();

        /*
         * Validate continue to create account
         * */
        validateFieldsToContinue();
    }
    @OnTextChanged(value = R.id.tietAccountName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAccountNameChanged(Editable editable) {
        this.fieldsValidator.validate();

        /*
         * Always disable till the server response comes
         * */
        disableCreate();
    }


    @OnClick(R.id.btnCancel)
    public void cancel(){

        /*
        * Exit of the activity
        * */
        this.finish();
    }

    @OnClick(R.id.btnCreate)
    public void createSeed(){

        // Make request to create a bitshare account
        final ValidateCreateBitsharesAccountRequest request =
                new ValidateCreateBitsharesAccountRequest(tietAccountName.getText().toString(), getApplicationContext());


        //DTVV: Friday 27 July 2018
        //Makes dialog to tell the user that the account is been created
        final LoadingDialog creatingAccountMaterialDialog = new LoadingDialog(this);
        creatingAccountMaterialDialog.setMessage(this.getResources().getString(R.string.window_create_seed_DialogMessage));
        creatingAccountMaterialDialog.build();
        CreateSeedActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                creatingAccountMaterialDialog.show();
            }
        });
        request.setListener(new CryptoNetInfoRequestListener() {
            @Override
            public void onCarryOut() {
                creatingAccountMaterialDialog.dismiss();
                if (request.getStatus().equals(ValidateCreateBitsharesAccountRequest.StatusCode.SUCCEEDED)) {
                    GrapheneAccount accountSeed = request.getAccount();
                    Intent intent = new Intent(getApplicationContext(), BackupSeedActivity.class);
                    intent.putExtra("SEED_ID", accountSeed.getId());
                    startActivity(intent);
                } else {
                    fieldsValidator.validate();
                }
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                CryptoNetInfoRequests.getInstance().addRequest(request);
            }
        };

        thread.start();
    }



    /*
    * Validate that all is complete to continue to create
    * */
    private void validateFieldsToContinue(){

        boolean result = false; //Contains the final result

        boolean pinValid = this.tietPin.getFieldValidatorModel().isValid();
        boolean pinConfirmationValid = this.tietPinConfirmation.getFieldValidatorModel().isValid();
        boolean pinAccountNameValid = this.tietAccountName.getFieldValidatorModel().isValid();

        if(pinValid &&
                pinConfirmationValid &&
                pinAccountNameValid){
            result = true; //Validation is correct
        }



        /*
        * If the result is true so the user can continue to the creation of the account
        * */
        if(result){
            enableCreate();
        }
        else{
            disableCreate();
        }
    }


    /*
    * Enable create button
    * */
    private void enableCreate(){
        btnCreate.setEnabled(true);
        btnCreate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    /*
     * Disable create button
     * */
    private void disableCreate(){
        btnCreate.setEnabled(false);
        btnCreate.setBackground(getResources().getDrawable(R.drawable.disable_style));
    }
}
