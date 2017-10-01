package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.views.AccountSeedListView;

public class AccountSeedsManagementActivity extends AppCompatActivity {

    AccountSeedsManagementActivity accountSeedsManagementActivity;
    AccountSeedListViewModel accountSeedListViewModel;

    @BindView(R.id.vAccountSeedList)
    AccountSeedListView vAccountSeedList;

    @BindView(R.id.btnImportAccountSeed)
    Button btnImportAccountSeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_seeds_management);
        ButterKnife.bind(this);

        accountSeedListViewModel = ViewModelProviders.of(this).get(AccountSeedListViewModel.class);
        LiveData<List<AccountSeed>>  accountSeedData = accountSeedListViewModel.getAccountSeedList();
        //vAccountSeedList = this.findViewById(R.id.vAccountSeedList);

        accountSeedData.observe(this, new Observer<List<AccountSeed>>() {
            @Override
            public void onChanged(List<AccountSeed> accountSeeds) {
                vAccountSeedList.setData(accountSeeds);
            }
        });

        //accountSeedListView.setData(null);
    }

    @OnClick (R.id.btnImportAccountSeed)
    public void importAccountSeed(){
        Intent intent = new Intent(this, ImportSeedActivity.class);
        startActivity(intent);
    }
}
