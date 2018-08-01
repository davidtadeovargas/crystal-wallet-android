package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dialogs.ProgressCreatingAccountDialog;
import cy.agorise.crystalwallet.dialogs.material.CreatingAccountMaterialDialog;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.ValidateCreateBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.CreateSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;



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





public class CreateSeedActivity extends AppCompatActivity implements UIValidatorListener {

    AccountSeedViewModel accountSeedViewModel;
    CreateSeedValidator createSeedValidator;

    @BindView(R.id.tilPin)
    TextInputLayout tilPin;

    @BindView(R.id.tietPin)
    TextInputEditText tietPin;

    @BindView(R.id.tilPinConfirmation)
    TextInputLayout tilPinConfirmation;

    @BindView(R.id.tietPinConfirmation)
    TextInputEditText tietPinConfirmation;

    //@BindView(R.id.tvSeedWords)
    //TextView tvSeedWords;

    @BindView(R.id.tilAccountName)
    TextInputLayout tilAccountName;

    @BindView (R.id.tietAccountName)
    TextInputEditText tietAccountName;

    @BindView(R.id.btnCreate)
    Button btnCreate;

    @BindView(R.id.btnCancel)
    Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_seed);
        ButterKnife.bind(this);

        /*This button should not be enabled till all the fields be correctly filled*/
        disableCreate();

        tilPin.setErrorEnabled(true);
        tilPinConfirmation.setErrorEnabled(true);
        tilAccountName.setErrorEnabled(true);

        btnCreate.setEnabled(false);
        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        createSeedValidator = new CreateSeedValidator(this.getApplicationContext(),tietPin,tietPinConfirmation,tietAccountName);
        createSeedValidator.setListener(this);

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
        this.createSeedValidator.validate();
    }

    @OnTextChanged(value = R.id.tietPinConfirmation,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinConfirmationChanged(Editable editable) {
        this.createSeedValidator.validate();
    }

    /*@OnTextChanged(value = R.id.etSeedWords,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSeedWordsChanged(Editable editable) {
        this.createSeedValidator.validate();
    }
*/

    @OnTextChanged(value = R.id.tietAccountName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAccountNameChanged(Editable editable) {
        this.createSeedValidator.validate();
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.finish();
    }

    @OnClick(R.id.btnCreate)
    public void createSeed(){
        if (this.createSeedValidator.isValid()) {
            // Make request to create a bitshare account
            final ValidateCreateBitsharesAccountRequest request =
                    new ValidateCreateBitsharesAccountRequest(tietAccountName.getText().toString(), getApplicationContext());


            //DTVV: Friday 27 July 2018
            //Makes dialog to tell the user that the account is been created
            final CreatingAccountMaterialDialog creatingAccountMaterialDialog = new CreatingAccountMaterialDialog(this);
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
                        createSeedValidator.validate();
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



            //this.finish();
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final CreateSeedActivity activity = this;

        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == tietPin) {
                    tilPin.setError("");
                } else if (field.getView() == tietPinConfirmation){
                    tilPinConfirmation.setError("");
                } else if (field.getView() == tietAccountName){
                    tilAccountName.setError("");
                } //else if (field.getView() == etSeedWords){
                //    tvSeedWordsError.setText("");
                //}

                if (activity.createSeedValidator.isValid()){
                    btnCreate.setEnabled(true);
                } else {
                    btnCreate.setEnabled(false);
                }

            }
        });
    }

    @Override
    public void onValidationFailed(final ValidationField field) {

        disableCreate(); //Can not create account yet

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (field.getView() == tietPin) {
                    tilPin.setError(field.getMessage());
                } else if (field.getView() == tietPinConfirmation){
                    tilPinConfirmation.setError(field.getMessage());
                } else if (field.getView() == tietAccountName){
                    tilAccountName.setError(field.getMessage());
                } //else if (field.getView() == etSeedWords){
                //    tvSeedWordsError.setText(field.getMessage());
                //}
            }
        });
    }


    @OnFocusChange(R.id.tietPin)
    public void onFocusChangePIN(View v, boolean hasFocus){

        /*
        * On lost focus
        * */
        if(!hasFocus){

            /*
             * Validate continue to create account
             * */
            if(validateFieldsToContinue()){
                enableCreate();
            }
            else {
                disableCreate();
            }
        }
    }
    @OnFocusChange(R.id.tietPinConfirmation)
    public void onFocusChangePINConfirmation(View v, boolean hasFocus){

        /*
         * On lost focus
         * */
        if(!hasFocus){

            /*
             * Validate continue to create account
             * */
            if(validateFieldsToContinue()){
                enableCreate();
            }
            else {
                disableCreate();
            }
        }
    }
    @OnFocusChange(R.id.tietAccountName)
    public void onFocusChangeAccountName(View v, boolean hasFocus){

        /*
         * On lost focus
         * */
        if(!hasFocus){

            /*
             * Validate continue to create account
             * */
            if(validateFieldsToContinue()){
                enableCreate();
            }
            else {
                disableCreate();
            }
        }
    }


    /*
    * Validate that all is complete to continue to create
    * */
    private boolean validateFieldsToContinue(){

        /*
        * Get the value of the fields
        * */
        final String pin = tilPin.getEditText().getText().toString().trim();
        final String pinConfirmation = tilPinConfirmation.getEditText().getText().toString().trim();
        final String accountName = tilAccountName.getEditText().getText().toString().trim();

        final String pinError = tilPin.getError()==null?"":tilPin.getError().toString().trim();
        final String pinConfirmationError = tietPinConfirmation.getError()==null?"":tietPinConfirmation.getError().toString().trim();
        final String accountNameError = tietAccountName.getError()==null?"":tietAccountName.getError().toString().trim();

        boolean result = false; //Contains the final result

        if(!pin.isEmpty() && !pinConfirmation.isEmpty() && !accountName.isEmpty()) {
            if(pinError.isEmpty() && pinConfirmationError.isEmpty() && accountNameError.isEmpty()){
                result = true;
            }
        }

        /*
        * If the result is true so the user can continue to the creation of the account
        * */
        return result;
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
