package cy.agorise.crystalwallet.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;

public class LicenseActivity extends AppCompatActivity {

    @BindView(R.id.wvEULA) WebView wvEULA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        // TODO check if license has been agreed

        String html = getString(R.string.licence_html);
        wvEULA.loadData(html, "text/html", "UTF-8");
    }

    @OnClick(R.id.btnAgree)
    public void onAgree() {
        // TODO send user to Intro activity if no active account or to Board activity otherwise
    }

    @OnClick(R.id.btnDisAgree)
    public void onDisagree() {
        finish();
    }
}
