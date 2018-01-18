package cy.agorise.crystalwallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_security_settings, container, false);
        ButterKnife.bind(this, v);

        return v;
    }
}
