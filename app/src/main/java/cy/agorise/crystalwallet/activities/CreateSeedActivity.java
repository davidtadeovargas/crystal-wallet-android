package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dialogs.ProgressCreatingAccountDialog;
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

        tilPin.setErrorEnabled(true);
        tilPinConfirmation.setErrorEnabled(true);
        tilAccountName.setErrorEnabled(true);

        btnCreate.setEnabled(false);
        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        createSeedValidator = new CreateSeedValidator(this.getApplicationContext(),tietPin,tietPinConfirmation,tietAccountName);
        createSeedValidator.setListener(this);
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
            final ProgressCreatingAccountDialog progressCreatingAccountDialog = new ProgressCreatingAccountDialog(CreateSeedActivity.this);
            progressCreatingAccountDialog.show();
            CreateSeedActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressCreatingAccountDialog.show();
                }
            });
            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {
                    progressCreatingAccountDialog.dismiss();
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
}
