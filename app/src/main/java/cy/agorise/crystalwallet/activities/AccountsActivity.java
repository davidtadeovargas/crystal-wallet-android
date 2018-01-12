package cy.agorise.crystalwallet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;

/**
 * Created by xd on 1/9/18.
 *
 */

public class AccountsActivity extends Activity {

    @BindView(R.id.tvSettings)
    TextView tvSettings;

    @BindView(R.id.tvClose)
    TextView tvClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);
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