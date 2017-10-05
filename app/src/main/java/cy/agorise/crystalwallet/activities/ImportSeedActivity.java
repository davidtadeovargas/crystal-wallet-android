package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.ImportSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.ImportSeedValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.ValidationField;
import cy.agorise.crystalwallet.views.TransactionListView;

public class ImportSeedActivity extends AppCompatActivity implements ImportSeedValidatorListener {

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

        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        importSeedValidator = accountSeedViewModel.getValidator();

        importSeedValidator.setListener(this);
    }

    @OnTextChanged(value = R.id.etPin,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinChanged(Editable editable) {
        this.importSeedValidator.validatePin(editable.toString());
        this.importSeedValidator.validatePinConfirmation(etPinConfirmation.getText().toString(),editable.toString());
    }

    @OnTextChanged(value = R.id.etPinConfirmation,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPinConfirmationChanged(Editable editable) {
        this.importSeedValidator.validatePinConfirmation(editable.toString(), etPin.getText().toString());
    }

    @OnTextChanged(value = R.id.etAccountName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAccountNameChanged(Editable editable) {
        this.importSeedValidator.validateAccountName(editable.toString(), etSeedWords.getText().toString());
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
        switch (field.getName()){
            case "pin":
                tvPinError.setText("");
                break;
            case "pinconfirmation":
                tvPinConfirmationError.setText("");
                break;
            case "accountname":
                tvAccountNameError.setText("");
                break;
        }
    }

    @Override
    public void onValidationFailed(ValidationField field) {
        switch (field.getName()){
            case "pin":
                tvPinError.setText(field.getMessage());
                break;
            case "pinconfirmation":
                tvPinConfirmationError.setText(field.getMessage());
                break;
            case "accountname":
                tvAccountNameError.setText(field.getMessage());
                break;
        }
    }
}
