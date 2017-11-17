package cy.agorise.crystalwallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.fragments.BalanceFragment;
import cy.agorise.crystalwallet.fragments.ContactsFragment;
import cy.agorise.crystalwallet.fragments.TransactionsFragment;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class BoardActivity  extends AppCompatActivity {

    @BindView(R.id.pager)
    public ViewPager mPager;

    @BindView(R.id.btnGeneralSettings)
    public ImageButton btnGeneralSettings;

    @BindView(R.id.fabSend)
    public FloatingActionButton fabSend;

    @BindView(R.id.fabReceive)
    public FloatingActionButton fabReceive;

    @BindView(R.id.fabAddContact)
    public FloatingActionButton fabAddContact;

    public BoardPagerAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boardAdapter = new BoardPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(boardAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        /*fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Hide Add Contact fab, it won't hide until first page changed...
        fabAddContact.hide();

        // Hide and show respective fabs when convenient
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.hide();
                        break;

                    case 1:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.hide();
                        break;

                    default:
                        fabReceive.hide();
                        fabSend.hide();
                        fabAddContact.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btnGeneralSettings)
    public void onBtnGeneralSettingsClick(){
        Intent intent = new Intent(this, GeneralSettingsActivity.class);
        startActivity(intent);
    }

    private class BoardPagerAdapter extends FragmentStatePagerAdapter {
        public BoardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new BalanceFragment();
                case 1:
                    return new TransactionsFragment();
                case 2:
                    return new ContactsFragment();
            }


            return null; //new OnConstructionFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
