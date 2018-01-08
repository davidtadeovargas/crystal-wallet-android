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

    @BindView(R.id.tvSeedWords)
    TextView tvSeedWords;

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
        createSeedValidator = new CreateSeedValidator(this.getApplicationContext(),etPin,etPinConfirmation,etAccountName,tvSeedWords);
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

    @OnTextChanged(value = R.id.etSeedWords,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSeedWordsChanged(Editable editable) {
        this.createSeedValidator.validate();
    }


    @OnTextChanged(value = R.id.etAccountName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAccountNameChanged(Editable editable) {
        this.createSeedValidator.validate();
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.finish();
    }

    @OnClick(R.id.btnImport)
    public void createSeed(){
        if (this.createSeedValidator.isValid()) {
            AccountSeed seed = new AccountSeed();

            //TODO verify if words are already in the db
            //TODO check if name has been asigned to other seed
            seed.setMasterSeed(tvSeedWords.getText().toString());
            seed.setName(etAccountName.getText().toString());
            seed.setType(SeedType.BIP39);

            accountSeedViewModel.addSeed(seed);

            CryptoNetAccountViewModel cryptoNetAccountViewModel = ViewModelProviders.of(this).get(CryptoNetAccountViewModel.class);
            GrapheneAccountInfoViewModel grapheneAccountInfoViewModel = ViewModelProviders.of(this).get(GrapheneAccountInfoViewModel.class);
            CryptoNetAccount cryptoNetAccount = new CryptoNetAccount();
            cryptoNetAccount.setSeedId(seed.getId());
            cryptoNetAccount.setAccountIndex(0);
            cryptoNetAccount.setCryptoNet(cy.agorise.crystalwallet.enums.CryptoNet.BITSHARES);
            cryptoNetAccountViewModel.addCryptoNetAccount(cryptoNetAccount);
            GrapheneAccountInfo grapheneAccountInfo = new GrapheneAccountInfo(cryptoNetAccount.getId());
            grapheneAccountInfo.setName(etAccountName.getText().toString());
            grapheneAccountInfoViewModel.addGrapheneAccountInfo(grapheneAccountInfo);

            this.finish();
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
                } else if (field.getView() == etSeedWords){
                    tvSeedWordsError.setText("");
                }

                if (activity.importSeedValidator.isValid()){
                    btnImport.setEnabled(true);
                } else {
                    btnImport.setEnabled(false);
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
        } else if (field.getView() == etSeedWords){
            tvSeedWordsError.setText(field.getMessage());
        }
    }
}
