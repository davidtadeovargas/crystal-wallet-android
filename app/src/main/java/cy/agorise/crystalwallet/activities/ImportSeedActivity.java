package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;
import cy.agorise.crystalwallet.views.TransactionListView;

public class ImportSeedActivity extends AppCompatActivity {

    AccountSeedViewModel accountSeedViewModel;

    @BindView(R.id.tvPin)
    TextView tvPin;

    @BindView(R.id.tvPinConfirmation)
    TextView tvPinConfirmation;

    @BindView(R.id.etSeedWords)
    EditText etSeedWords;

    @BindView (R.id.etAccountName)
    EditText etAccountName;

    @BindView(R.id.btnImport)
    Button btnImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_seed);

        ButterKnife.bind(this);

        accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
        //this.seed = new AccountSeed();
    }

    @OnClick(R.id.btnImport)
    public void importSeed(){
        AccountSeed seed = new AccountSeed();

        //TODO verify if PIN and PIN confirmation are not null and are the same
        //TODO verify if words are already in the db
        seed.setMasterSeed(etSeedWords.getText().toString());
        seed.setName(etAccountName.getText().toString());

        accountSeedViewModel.addSeed(seed);
    }
}
