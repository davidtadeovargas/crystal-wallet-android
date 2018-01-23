package cy.agorise.crystalwallet.activities;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.views.AccountSeedListView;

/**
 * Created by xd on 1/9/18.
 *
 */

public class AccountsActivity extends AppCompatActivity {

    @BindView(R.id.tvSettings)
    TextView tvSettings;

    @BindView(R.id.tvClose)
    TextView tvClose;

    @BindView(R.id.vAccountSeedList)
    AccountSeedListView vAccountSeedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);

        AccountSeedListViewModel accountSeedListViewModel = ViewModelProviders.of(this).get(AccountSeedListViewModel.class);
        LiveData<List<AccountSeed>> accountSeedData = accountSeedListViewModel.getAccountSeedList();
        vAccountSeedList.setData(null);

        accountSeedData.observe(this, new Observer<List<AccountSeed>>() {
            @Override
            public void onChanged(List<AccountSeed> accountSeeds) {
                vAccountSeedList.setData(accountSeeds);
            }
        });
    }

    @OnClick(R.id.tvSettings)
    public void onTvSettingsClick(){
        onBackPressed();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvClose)
    public void cancel(){
        onBackPressed();
    }
}