package cy.agorise.crystalwallet.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;

/**
 * Created by xd on 1/17/18.
 */

public class SecuritySettingsFragment extends Fragment {

    public SecuritySettingsFragment() {
        // Required empty public constructor
    }

    public static SecuritySettingsFragment newInstance() {
        SecuritySettingsFragment fragment = new SecuritySettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.pager)
    public ViewPager mPager;

    public SecurityPagerAdapter securityPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_security_settings, container, false);
        ButterKnife.bind(this, v);

        securityPagerAdapter = new SecurityPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(securityPagerAdapter);

        TabLayout tabLayout = v.findViewById(R.id.tabs);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        return v;
    }

    private class SecurityPagerAdapter extends FragmentPagerAdapter {
        SecurityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    //return new GeneralSettingsFragment();
                case 1:
                    //return new SecuritySettingsFragment();
                case 2:
                    return new BackupsSettingsFragment();
            }


            return null; //new OnConstructionFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
