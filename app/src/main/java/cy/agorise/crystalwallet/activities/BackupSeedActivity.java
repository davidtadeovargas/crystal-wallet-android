package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.viewmodels.AccountSeedViewModel;
//tvBrainKey
public class BackupSeedActivity extends AppCompatActivity {

    AccountSeedViewModel accountSeedViewModel;

    @BindView(R.id.tvBrainKey)
    TextView textfieldBrainkey;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCopy)
    Button btnCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_seed);

        //Note: Test porpouses
        /*final TextView textView = findViewById(R.id.tvBrainKey);
            textView.setText("sakk902909321o p3k21kldsa0'dsa90'e930eidakdñsakdñlsakdi90i03 2i90idopsasakk902909321op3k21 kldsa0'dsa90'e930eid akdñsakdñlsakdi90i032i90idopsa");
        */

        ButterKnife.bind(this);

        long seedId = getIntent().getLongExtra("SEED_ID",-1);

        if (seedId > -1) {
            accountSeedViewModel = ViewModelProviders.of(this).get(AccountSeedViewModel.class);
            accountSeedViewModel.loadSeed(seedId);
            LiveData<AccountSeed> liveDataAccountSeed = accountSeedViewModel.getAccountSeed();
            liveDataAccountSeed.observe(this, new Observer<AccountSeed>() {
                @Override
                public void onChanged(@Nullable AccountSeed accountSeed) {
                    textfieldBrainkey.setText(accountSeed.getMasterSeed());
                }
            });
            accountSeedViewModel.loadSeed(seedId);

        } else {
            finish();
        }
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick(){
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    /*
     *   Clic on button copy to clipboard
     * */
    @OnClick(R.id.btnCopy)
    public void btnCopyClick(){

        /*
         *  Save to clipboard the brainkey chain
         * */
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(textfieldBrainkey.getText(), textfieldBrainkey.getText().toString());
        clipboard.setPrimaryClip(clip);

        /*
         * Success message
         * */
        Toast.makeText(this.getBaseContext(),getResources().getString(R.string.window_seed_toast_clipboard), Toast.LENGTH_SHORT).show();
    }
}
