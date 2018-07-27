package cy.agorise.crystalwallet.activities;

import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.util.PasswordManager;
import cy.agorise.crystalwallet.util.yubikey.Algorithm;
import cy.agorise.crystalwallet.util.yubikey.OathType;
import cy.agorise.crystalwallet.util.yubikey.YkOathApi;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

public class PocketRequestActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter ndef;
    IntentFilter[] intentFiltersArray;
    String[][] techList;

    @Override
    public void onBackPressed() {
        //Do nothing to prevent the user to use the back button
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_request);
        ButterKnife.bind(this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        this.configureForegroundDispatch();
    }

    public void configureForegroundDispatch(){
        if (mNfcAdapter != null) {
            pendingIntent = PendingIntent.getActivity(
                    this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("fail", e);
            }
            intentFiltersArray = new IntentFilter[]{ndef,};
            techList = new String[][]{ new String[] {IsoDep.class.getName(), NfcA.class.getName(), MifareClassic.class.getName(), NdefFormatable.class.getName()} };

        } else {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
    }

    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
        }
    }

    public void onNewIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        IsoDep tagIsoDep = IsoDep.get(tagFromIntent);
        Log.i("Tag from nfc","New Intent");
        try {

            String encodedSecret = "hola";
            Base32 decoder = new Base32();

            if ((encodedSecret != null) && (!encodedSecret.equals("")) && decoder.isInAlphabet(encodedSecret)) {
                byte[] secret = decoder.decode(encodedSecret);
                YkOathApi ykOathApi = new YkOathApi();
                tagIsoDep.connect();
                tagIsoDep.setTimeout(15000);

                //byte[] keyBytes = {0x68,0x6f,0x6c,0x61};
                ykOathApi.putCode(tagIsoDep,"prueba",secret, OathType.TOTP, Algorithm.SHA256,(byte)6,0,false);
                tagIsoDep.close();

                Toast.makeText(this, "Credential saved!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Invalid password for credential", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Tag from nfc: "+tagFromIntent, Toast.LENGTH_LONG).show();
    }

}


