package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.Language;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;
import cy.agorise.crystalwallet.views.TimeZoneAdapter;


/**
 * Created by xd on 12/28/17.
 */

public class GeneralSettingsFragment extends Fragment {

    private HashMap<String,String> countriesMap;
    private GeneralSettingListViewModel generalSettingListViewModel;
    private LiveData<List<GeneralSetting>> generalSettingListLiveData;

    private Boolean spPreferredLanguageInitialized;
    private Boolean spTimeZoneInitialized;

    @BindView (R.id.spTaxableCountry)
    Spinner spTaxableCountry;
    @BindView (R.id.spPreferredLanguage)
    Spinner spPreferredLanguage;
    @BindView (R.id.spDisplayDateTime)
    Spinner spDisplayDateTime;

    public GeneralSettingsFragment() {
        this.spPreferredLanguageInitialized = false;
        this.spTimeZoneInitialized = false;
        // Required empty public constructor
    }

    public static GeneralSettingsFragment newInstance() {
        GeneralSettingsFragment fragment = new GeneralSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.spPreferredLanguageInitialized = false;
        fragment.spTimeZoneInitialized = false;
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



        //Observes the general settings data
        generalSettingListLiveData.observe(this, new Observer<List<GeneralSetting>>() {
            @Override
            public void onChanged(@Nullable List<GeneralSetting> generalSettings) {
                loadSettings(generalSettings);
            }
        });



        return v;
    }

    public void initPreferredCountry(GeneralSetting preferredCountrySetting){
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

        if (preferredCountrySetting != null) {
            String preferedCountryCode = preferredCountrySetting.getValue();
            spTaxableCountry.setSelection(((ArrayAdapter<String>) spTaxableCountry.getAdapter()).getPosition(countriesMap.get(preferedCountryCode)));
        }
    }

    public void initPreferredLanguage(GeneralSetting preferredLanguageSetting){
        ArrayAdapter<Language> preferredLanguageAdapter = new ArrayAdapter<Language>(getContext(), android.R.layout.simple_spinner_item, Language.values());
        spPreferredLanguage.setAdapter(preferredLanguageAdapter);
        if (preferredLanguageSetting != null) {
            spPreferredLanguage.setSelection(preferredLanguageAdapter.getPosition(Language.getByCode(preferredLanguageSetting.getValue())));
        }
    }

    public void initDateTimeFormat(GeneralSetting dateTimeFormatSetting){
        TimeZoneAdapter timeZoneAdapter;
        if (spDisplayDateTime.getAdapter() == null) {
            timeZoneAdapter = new TimeZoneAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
            spDisplayDateTime.setAdapter(timeZoneAdapter);
        } else {
            timeZoneAdapter = (TimeZoneAdapter) spDisplayDateTime.getAdapter();
        }
        if (dateTimeFormatSetting != null) {
            spDisplayDateTime.setSelection(timeZoneAdapter.getPosition(dateTimeFormatSetting.getValue()));
        }
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
            GeneralSetting generalSettingCountryCode = this.getSetting(GeneralSetting.SETTING_NAME_PREFERRED_COUNTRY);
            GeneralSetting generalSettingCurrency = this.getSetting(GeneralSetting.SETTING_NAME_PREFERRED_CURRENCY);

            if (generalSettingCountryCode == null){
                generalSettingCountryCode = new GeneralSetting();
                generalSettingCountryCode.setName(GeneralSetting.SETTING_NAME_PREFERRED_COUNTRY);
            }
            if (generalSettingCurrency == null){
                generalSettingCurrency = new GeneralSetting();
                generalSettingCurrency.setName(GeneralSetting.SETTING_NAME_PREFERRED_CURRENCY);
            }

            String countryCode = countriesMap.get((String) spTaxableCountry.getSelectedItem());
            Locale locale = new Locale("", countryCode);
            Currency currency = Currency.getInstance(locale);

            generalSettingCountryCode.setValue(countryCode);
            generalSettingCurrency.setValue(currency.getCurrencyCode());
            this.generalSettingListViewModel.saveGeneralSettings(generalSettingCountryCode, generalSettingCurrency);
        }
    }

    @OnItemSelected(R.id.spDisplayDateTime)
    void onTimeZoneSelected(int position){
        //The first call will be when the spinner gets an adapter attached
        if (this.spTimeZoneInitialized) {
            String timeZoneIdSelected = (String) this.spDisplayDateTime.getSelectedItem();
            GeneralSetting generalSettingTimeZone = this.getSetting(GeneralSetting.SETTING_NAME_TIME_ZONE);

            if (generalSettingTimeZone == null) {
                generalSettingTimeZone = new GeneralSetting();
                generalSettingTimeZone.setName(GeneralSetting.SETTING_NAME_TIME_ZONE);
            }

            if ((generalSettingTimeZone.getValue() == null)||(!generalSettingTimeZone.getValue().equals(timeZoneIdSelected))) {
                generalSettingTimeZone.setValue(timeZoneIdSelected);
                this.generalSettingListViewModel.saveGeneralSettings(generalSettingTimeZone);
            }
        } else {
            this.spTimeZoneInitialized = true;
        }
    }

    @OnItemSelected(R.id.spPreferredLanguage)
    void onPreferredLanguageSelected(int position){
        //The first call will be when the spinner gets an adapter attached
        if (this.spPreferredLanguageInitialized) {
            Language languageSelected = (Language) this.spPreferredLanguage.getSelectedItem();
            GeneralSetting generalSettingPreferredLanguage = this.getSetting(GeneralSetting.SETTING_NAME_PREFERRED_LANGUAGE);

            if (generalSettingPreferredLanguage == null) {
                generalSettingPreferredLanguage = new GeneralSetting();
                generalSettingPreferredLanguage.setName(GeneralSetting.SETTING_NAME_PREFERRED_LANGUAGE);
            }

            if ((generalSettingPreferredLanguage.getValue() == null)||(!generalSettingPreferredLanguage.getValue().equals(languageSelected.getCode()))) {
                generalSettingPreferredLanguage.setValue(languageSelected.getCode());
                this.generalSettingListViewModel.saveGeneralSettings(generalSettingPreferredLanguage);

                Resources resources = getContext().getResources();
                Locale locale = new Locale(languageSelected.getCode());
                Locale.setDefault(locale);
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration configuration = resources.getConfiguration();
                configuration.locale = locale;
                resources.updateConfiguration(configuration, dm);
                Intent i = getContext().getPackageManager()
                        .getLaunchIntentForPackage(getContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        } else {
            this.spPreferredLanguageInitialized = true;
        }
    }

    public void loadSettings(List<GeneralSetting> generalSettings){

        initPreferredCountry(getSetting(GeneralSetting.SETTING_NAME_PREFERRED_COUNTRY));
        initPreferredLanguage(getSetting(GeneralSetting.SETTING_NAME_PREFERRED_LANGUAGE));
        initDateTimeFormat(getSetting(GeneralSetting.SETTING_NAME_TIME_ZONE));
    }
}
