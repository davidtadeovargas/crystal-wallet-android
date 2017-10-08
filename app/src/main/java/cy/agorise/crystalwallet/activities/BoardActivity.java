package cy.agorise.crystalwallet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    public BoardPagerAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        ButterKnife.bind(this);

        boardAdapter = new BoardPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(boardAdapter);
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
