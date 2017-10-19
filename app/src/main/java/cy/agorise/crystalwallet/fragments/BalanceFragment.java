package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoNetBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetBalanceListViewModel;
import cy.agorise.crystalwallet.views.CryptoNetBalanceListView;

public class BalanceFragment extends Fragment {
    CryptoNetBalanceListViewModel cryptoNetBalanceListViewModel;

    @BindView(R.id.vCryptoNetBalanceListView)
    CryptoNetBalanceListView vCryptoNetBalanceListView;

    public BalanceFragment() {
        // Required empty public constructor
    }

    public static BalanceFragment newInstance() {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        ButterKnife.bind(this, view);

        cryptoNetBalanceListViewModel = ViewModelProviders.of(this).get(CryptoNetBalanceListViewModel.class);
        LiveData<List<CryptoNetBalance>> cryptoNetBalanceData = cryptoNetBalanceListViewModel.getCryptoNetBalanceList();
        vCryptoNetBalanceListView.setData(null, this);

        final Fragment fragment = this;
        
        cryptoNetBalanceData.observe(this, new Observer<List<CryptoNetBalance>>() {
            @Override
            public void onChanged(List<CryptoNetBalance> cryptoNetBalances) {
                vCryptoNetBalanceListView.setData(cryptoNetBalances, fragment);
            }
        });

        return view;
    }
}
