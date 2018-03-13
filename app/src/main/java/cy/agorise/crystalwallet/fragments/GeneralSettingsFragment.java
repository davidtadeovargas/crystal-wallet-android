package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by xd on 12/28/17.
 */

public class GeneralSettingsFragment extends Fragment {

    private HashMap<String,String> countriesMap;
    private GeneralSettingListViewModel generalSettingListViewModel;
    private LiveData<List<GeneralSetting>> generalSettingListLiveData;

    @BindView (R.id.spTaxableCountry)
    Spinner spTaxableCountry;

    public GeneralSettingsFragment() {
        // Required empty public constructor
    }

    public static GeneralSettingsFragment newInstance() {
        GeneralSettingsFragment fragment = new GeneralSettingsFragment();
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
        View v = inflater.inflate(R.layout.fragment_general_settings, container, false);
        ButterKnife.bind(this, v);

        generalSettingListViewModel = ViewModelProviders.of(this).get(GeneralSettingListViewModel.class);
        generalSettingListLiveData = generalSettingListViewModel.getGeneralSettingList();

        // Initializes the countries spinner
        countriesMap = new HashMap<String, String>();
        String[] countryCodeList = Locale.getISOCountries();
        ArrayList<String> countryAndCurrencyList = new ArrayList<String>();
        String countryAndCurrencyLabel = "";
        for (String countryCode : countryCodeList) {
            Locale locale = new Locale("", countryCode);
            try {
                Currency currency = Currency.getInstance(locale);
                countryAndCurrencyLabel = locale.getDisplayCountry() + " (" + currency.getCurrencyCode() + ")";
                countryAndCurrencyList.add(countryAndCurrencyLabel);
                countriesMap.put(countryCode, countryAndCurrencyLabel);
                countriesMap.put(countryAndCurrencyLabel, countryCode);
            } catch (Exception e) {

            }
        }
        Collections.sort(countryAndCurrencyList);
        countryAndCurrencyList.add(0,"SELECT COUNTRY");
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, countryAndCurrencyList);
        spTaxableCountry.setAdapter(countryAdapter);

        //Observes the general settings data
        generalSettingListLiveData.observe(this, new Observer<List<GeneralSetting>>() {
            @Override
            public void onChanged(@Nullable List<GeneralSetting> generalSettings) {
                loadSettings(generalSettings);
            }
        });

        return v;
    }

    public GeneralSetting getSetting(String name){
        for (GeneralSetting generalSetting:this.generalSettingListLiveData.getValue()) {
            if (generalSetting.getName().equals(name)) {
                return generalSetting;
            }
        }

        return null;
    }

    @OnItemSelected(R.id.spTaxableCountry)
    void onItemSelected(int position) {
        if (position != 0) {
            GeneralSetting generalSettingCountryCode = this.getSetting(GeneralSetting.SETTING_NAME_PREFERED_COUNTRY);
            GeneralSetting generalSettingCurrency = this.getSetting(GeneralSetting.SETTING_NAME_PREFERED_CURRENCY);

            if (generalSettingCountryCode == null){
                generalSettingCountryCode = new GeneralSetting();
                generalSettingCountryCode.setName(GeneralSetting.SETTING_NAME_PREFERED_COUNTRY);
            }
            if (generalSettingCurrency == null){
                generalSettingCurrency = new GeneralSetting();
                generalSettingCurrency.setName(GeneralSetting.SETTING_NAME_PREFERED_CURRENCY);
            }

            String countryCode = countriesMap.get((String) spTaxableCountry.getSelectedItem());
            Locale locale = new Locale("", countryCode);
            Currency currency = Currency.getInstance(locale);

            generalSettingCountryCode.setValue(countryCode);
            generalSettingCurrency.setValue(currency.getCurrencyCode());
            this.generalSettingListViewModel.saveGeneralSettings(generalSettingCountryCode, generalSettingCurrency);
        }
    }

    public void loadSettings(List<GeneralSetting> generalSettings){
        for (GeneralSetting generalSetting:generalSettings) {
            if (generalSetting.getName().equals(GeneralSetting.SETTING_NAME_PREFERED_COUNTRY)){
                String preferedCountryCode = generalSetting.getValue();
                spTaxableCountry.setSelection(((ArrayAdapter<String>)spTaxableCountry.getAdapter()).getPosition(countriesMap.get(preferedCountryCode)));
            }
        }
    }
}
