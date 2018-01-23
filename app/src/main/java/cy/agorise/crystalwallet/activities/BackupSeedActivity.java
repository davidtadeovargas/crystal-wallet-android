package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.CreateSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

public class BackupSeedActivity extends AppCompatActivity {

    AccountSeedViewModel accountSeedViewModel;

    @BindView(R.id.tvMnemonic)
    TextView tvMnemonic;
    @BindView(R.id.btnOk)
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_seed);
        ButterKnife.bind(this);

        long seedId = getIntent().getLongExtra("SEED_ID",-1);

        if (seedId > -1) {
            accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
            accountSeedViewModel.loadSeed(seedId);
            LiveData<AccountSeed> liveDataAccountSeed = accountSeedViewModel.getAccountSeed();
            liveDataAccountSeed.observe(this, new Observer<AccountSeed>() {
                @Override
                public void onChanged(@Nullable AccountSeed accountSeed) {
                    tvMnemonic.setText(accountSeed.getMasterSeed());
                }
            });
            accountSeedViewModel.loadSeed(seedId);

        } else {
            finish();
        }
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick(){
        this.finish();
    }
}
