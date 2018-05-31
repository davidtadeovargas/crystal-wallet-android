package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;


/**
 * Created by xd on 12/28/17.
 */

public class GeneralCryptoNetAccountSettingsFragment extends Fragment {

    @BindView(R.id.tvMnemonic)
    TextView tvMnemonic;

    CryptoNetAccount cryptoNetAccount;
    AccountSeed accountSeed;

    public GeneralCryptoNetAccountSettingsFragment() {

        if (getArguments() != null) {
            long cryptoNetAcountId = getArguments().getLong("CRYPTO_NET_ACCOUNT_ID", -1);

            if (cryptoNetAcountId > -1) {
                this.cryptoNetAccount = CrystalDatabase.getAppDatabase(getContext()).cryptoNetAccountDao().getById(cryptoNetAcountId);
                this.accountSeed = CrystalDatabase.getAppDatabase(getContext()).accountSeedDao().findById(this.cryptoNetAccount.getSeedId());
            }
        }
        // Required empty public constructor
    }

    public static GeneralCryptoNetAccountSettingsFragment newInstance(long cryptoNetAccountId) {
        GeneralCryptoNetAccountSettingsFragment fragment = new GeneralCryptoNetAccountSettingsFragment();
        Bundle args = new Bundle();
        args.putLong("CRYPTO_NET_ACCOUNT_ID", cryptoNetAccountId);
        fragment.setArguments(args);


        if (cryptoNetAccountId > -1){
            fragment.cryptoNetAccount = CrystalDatabase.getAppDatabase(fragment.getContext()).cryptoNetAccountDao().getById(cryptoNetAccountId);
            fragment.accountSeed = CrystalDatabase.getAppDatabase(fragment.getContext()).accountSeedDao().findById(fragment.cryptoNetAccount.getSeedId());
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_general_crypto_net_account_settings, container, false);
        ButterKnife.bind(this, v);

        initAlreadyLtm();

        return v;
    }

    public void initAlreadyLtm(){
        if (this.cryptoNetAccount != null) {
            tvMnemonic.setText(this.accountSeed.getMasterSeed());
        }
    }
}
