package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateCreateBitsharesAccountRequest;
import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccountInfo;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountViewModel;
import cy.agorise.crystalwallet.viewmodels.GrapheneAccountInfoViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.CreateSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.ImportSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

import static cy.agorise.crystalwallet.enums.SeedType.BIP39;

public class CreateSeedActivity extends AppCompatActivity implements UIValidatorListener {

    AccountSeedViewModel accountSeedViewModel;
    CreateSeedValidator createSeedValidator;

    @BindView(R.id.etPin)
    EditText etPin;
    @BindView(R.id.tvPinError)
    TextView tvPinError;

    @BindView(R.id.etPinConfirmation)
    EditText etPinConfirmation;
    @BindView(R.id.tvPinConfirmationError)
    TextView tvPinConfirmationError;

    //@BindView(R.id.tvSeedWords)
    //TextView tvSeedWords;

    @BindView (R.id.etAccountName)
    EditText etAccountName;
    @BindView(R.id.tvAccountNameError)
    TextView tvAccountNameError;

    @BindView(R.id.btnCreate)
    Button btnCreate;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_seed);
        ButterKnife.bind(this);

        btnCreate.setEnabled(false);
        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        createSeedValidator = new CreateSeedValidator(this.getApplicationContext(),etPin,etPinConfirmation,etAccountName);
        createSeedValidator.setListener(this);
    }

    @OnTextChanged(value = R.id.etPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinChanged(Editable editable) {
        this.createSeedValidator.validate();
    }

    @OnTextChanged(value = R.id.etPinConfirmation,
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

    @OnTextChanged(value = R.id.etAccountName,
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
                    new ValidateCreateBitsharesAccountRequest(etAccountName.getText().toString(), getApplicationContext());


            //Makes dialog to tell the user that the account is been created
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateSeedActivity.this,R.style.AppTheme);
            alertBuilder.setView(R.layout.progress_creating_account);
            //alertBuilder.setTitle("Processing");
            //alertBuilder.setMessage("Creating Bitshares Account");
            final AlertDialog processDialog = alertBuilder.create();
            CreateSeedActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    processDialog.show();
                    processDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            });

            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {
                    processDialog.dismiss();

                    if (request.getAccount() != null){
                        finish();
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

                if (field.getView() == etPin) {
                    tvPinError.setText("");
                } else if (field.getView() == etPinConfirmation){
                    tvPinConfirmationError.setText("");
                } else if (field.getView() == etAccountName){
                    tvAccountNameError.setText("");
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
    public void onValidationFailed(ValidationField field) {
        if (field.getView() == etPin) {
            tvPinError.setText(field.getMessage());
        } else if (field.getView() == etPinConfirmation){
            tvPinConfirmationError.setText(field.getMessage());
        } else if (field.getView() == etAccountName){
            tvAccountNameError.setText(field.getMessage());
        } //else if (field.getView() == etSeedWords){
        //    tvSeedWordsError.setText(field.getMessage());
        //}
    }
}
