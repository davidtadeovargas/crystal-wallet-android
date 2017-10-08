package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.ImportSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.ValidationField;

public class ImportSeedActivity extends AppCompatActivity implements UIValidatorListener {

    AccountSeedViewModel accountSeedViewModel;
    ImportSeedValidator importSeedValidator;

    @BindView(R.id.etPin)
    EditText etPin;
    @BindView(R.id.tvPinError)
    TextView tvPinError;

    @BindView(R.id.etPinConfirmation)
    EditText etPinConfirmation;
    @BindView(R.id.tvPinConfirmationError)
    TextView tvPinConfirmationError;

    @BindView(R.id.etSeedWords)
    EditText etSeedWords;

    @BindView (R.id.etAccountName)
    EditText etAccountName;
    @BindView(R.id.tvAccountNameError)
    TextView tvAccountNameError;

    @BindView(R.id.btnImport)
    Button btnImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_seed);

        ButterKnife.bind(this);

        btnImport.setEnabled(false);
        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        importSeedValidator = new ImportSeedValidator(this.getApplicationContext(),etPin,etPinConfirmation,etAccountName,etSeedWords);
        importSeedValidator.setListener(this);
    }

    @OnTextChanged(value = R.id.etPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinChanged(Editable editable) {
        this.importSeedValidator.validate();
    }

    @OnTextChanged(value = R.id.etPinConfirmation,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinConfirmationChanged(Editable editable) {
        this.importSeedValidator.validate();
    }

    @OnTextChanged(value = R.id.etAccountName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAccountNameChanged(Editable editable) {
        this.importSeedValidator.validate();
    }

    @OnClick(R.id.btnImport)
    public void importSeed(){
        if (this.importSeedValidator.isValid()) {
            AccountSeed seed = new AccountSeed();

            //TODO verify if PIN and PIN confirmation are not null and are the same
            //TODO verify if words are already in the db
            //TODO check if name has been asigned to other seed
            seed.setMasterSeed(etSeedWords.getText().toString());
            seed.setName(etAccountName.getText().toString());

            accountSeedViewModel.addSeed(seed);
            //TODO get back to the previous activity
        }
    }

    @Override
    public void onValidationSucceeded(ValidationField field) {
        if (field.getView() == etPin) {
            tvPinError.setText("");
        } else if (field.getView() == etPinConfirmation){
            tvPinConfirmationError.setText("");
        } else if (field.getView() == etAccountName){
            tvAccountNameError.setText("");
        }

        if (this.importSeedValidator.isValid()){
            btnImport.setEnabled(true);
        }
    }

    @Override
    public void onValidationFailed(ValidationField field) {
        if (field.getView() == etPin) {
            tvPinError.setText(field.getMessage());
        } else if (field.getView() == etPinConfirmation){
            tvPinConfirmationError.setText(field.getMessage());
        } else if (field.getView() == etAccountName){
            tvAccountNameError.setText(field.getMessage());
        }
    }
}
