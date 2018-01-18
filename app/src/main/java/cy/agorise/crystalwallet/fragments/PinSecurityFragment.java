package cy.agorise.crystalwallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;

/**
 * Created by xd on 1/18/18.
 */

public class PinSecurityFragment extends Fragment {

    public PinSecurityFragment() {
        // Required empty public constructor
    }

    public static PinSecurityFragment newInstance() {
        PinSecurityFragment fragment = new PinSecurityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pin_security, container, false);
        ButterKnife.bind(this, v);

        return v;
    }
}
