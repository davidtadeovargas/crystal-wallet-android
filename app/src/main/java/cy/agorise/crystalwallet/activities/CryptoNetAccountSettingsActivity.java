package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.fragments.BackupsSettingsFragment;
import cy.agorise.crystalwallet.fragments.BitsharesSettingsFragment;
import cy.agorise.crystalwallet.fragments.GeneralCryptoNetAccountSettingsFragment;
import cy.agorise.crystalwallet.fragments.GeneralSettingsFragment;
import cy.agorise.crystalwallet.fragments.SecuritySettingsFragment;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountViewModel;

/**
 * Created by henry varona on 05/28/18.
 *
 */

public class CryptoNetAccountSettingsActivity extends AppCompatActivity{

    @BindView(R.id.ivGoBack)
    public ImageView ivGoBack;

    @BindView(R.id.pager)
    public ViewPager mPager;

    public SettingsPagerAdapter settingsPagerAdapter;

    @BindView(R.id.surface_view)
    public SurfaceView mSurfaceView;

    @BindView(R.id.tvBuildVersion)
    public TextView tvBuildVersion;

    @BindView(R.id.tabs)
    public TabLayout tabs;

    private CryptoNetAccount cryptoNetAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_net_account_activity_settings);
        ButterKnife.bind(this);
        final CryptoNetAccountSettingsActivity thisActivity = this;

        long accountId = getIntent().getLongExtra("CRYPTO_NET_ACCOUNT_ID",-1);

        if (accountId > -1) {
            CryptoNetAccountViewModel cryptoNetAccountViewModel = ViewModelProviders.of(this).get(CryptoNetAccountViewModel.class);
            cryptoNetAccountViewModel.loadCryptoNetAccount(accountId);
            LiveData<CryptoNetAccount> cryptoNetAccountLiveData = cryptoNetAccountViewModel.getCryptoNetAccount();

            cryptoNetAccountLiveData.observe(this, new Observer<CryptoNetAccount>() {
                @Override
                public void onChanged(@Nullable CryptoNetAccount cryptoNetAccount) {
                    thisActivity.cryptoNetAccount = cryptoNetAccount;

                    settingsPagerAdapter = new SettingsPagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(settingsPagerAdapter);

                    TabLayout tabLayout = findViewById(R.id.tabs);

                    switch(cryptoNetAccount.getCryptoNet()){
                        case BITSHARES:
                            tabLayout.addTab(tabLayout.newTab().setText("Bitshares"));
                            break;
                    }
                    mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));
                }
            });

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Appbar animation
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    //Log.d(TAG,"surfaceCreated");
                    MediaPlayer mediaPlayer = MediaPlayer.create(CryptoNetAccountSettingsActivity.this, R.raw.appbar_background);
                    mediaPlayer.setDisplay(mSurfaceView.getHolder());
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    //Log.d(TAG,"surfaceChanged");
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    //Log.d(TAG,"surfaceDestroyed");
                }
            });


        } else {
            this.finish();
        }
    }

    private class SettingsPagerAdapter extends FragmentStatePagerAdapter {
        SettingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return GeneralCryptoNetAccountSettingsFragment.newInstance(cryptoNetAccount.getId());
            }

            if (cryptoNetAccount != null){
                switch (cryptoNetAccount.getCryptoNet()){
                    case BITSHARES:
                        switch(position){
                            case 1:
                                return BitsharesSettingsFragment.newInstance(cryptoNetAccount.getId());
                        }

                        break;
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            int tabCount = 1;

            if (cryptoNetAccount != null){
                switch (cryptoNetAccount.getCryptoNet()){
                    case BITSHARES:
                        tabCount = tabCount+1;
                        break;
                }
            }

            return tabCount;
        }
    }

    @OnClick(R.id.ivGoBack)
    public void goBack(){
        onBackPressed();
    }
}
