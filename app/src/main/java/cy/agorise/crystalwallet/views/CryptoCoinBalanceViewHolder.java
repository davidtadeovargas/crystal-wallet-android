package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoCurrencyEquivalence;
import cy.agorise.crystalwallet.models.GeneralSetting;

/**
 * Created by Henry Varona on 17/9/2017.
 *
 * Represents an element view from a Crypto Coin Balance List
 */

public class CryptoCoinBalanceViewHolder extends RecyclerView.ViewHolder {
    /*
     * the view holding the crypto coin name
     */
    private TextView cryptoCoinName;
    /*
     * the view holding the crypto coin balance amount
     */
    private TextView cryptoCoinBalance;
    /*
     * the view holding the crypto coin balance equivalent value
     */
    private TextView cryptoCoinBalanceEquivalence;

    private Context context;

    public CryptoCoinBalanceViewHolder(View itemView) {
        super(itemView);
        //TODO: use ButterKnife to load this
        cryptoCoinName = (TextView) itemView.findViewById(R.id.tvCryptoCoinName);
        cryptoCoinBalance = (TextView) itemView.findViewById(R.id.tvCryptoCoinBalanceAmount);
        cryptoCoinBalanceEquivalence = (TextView) itemView.findViewById(R.id.tvCryptoCoinBalanceEquivalence);
        this.context = itemView.getContext();

    }

    /*
     * Clears the information in this element view
     */
    public void clear(){
        cryptoCoinName.setText("loading...");
        cryptoCoinBalance.setText("");
        cryptoCoinBalanceEquivalence.setText("");
    }

    /*
     * Binds this view with the data of an element of the list
     */
    public void bindTo(final CryptoCoinBalance balance/*, final OnTransactionClickListener listener*/) {
        if (balance == null){
            this.clear();
        } else {
            //Retrieves the preferred currency selected by the user
            LiveData<GeneralSetting> preferedCurrencySetting = CrystalDatabase.getAppDatabase(this.context).generalSettingDao().getByName(GeneralSetting.SETTING_NAME_PREFERED_CURRENCY);
            //Retrieves the currency of this balance
            final CryptoCurrency currencyFrom = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getById(balance.getCryptoCurrencyId());
            //Sets the name and amount of the balance in the view
            String balanceString = String.format("%.2f",balance.getBalance()/Math.pow(10,currencyFrom.getPrecision()));
            cryptoCoinName.setText(currencyFrom.getName());
            cryptoCoinBalance.setText(balanceString);

            //Observes the preferred currency of the user. If the user changes it, this will change the equivalent value
            preferedCurrencySetting.observe((LifecycleOwner) this.context, new Observer<GeneralSetting>() {
                @Override
                public void onChanged(@Nullable GeneralSetting generalSetting) {
                    if (generalSetting != null) {
                        //Gets the currency object of the preferred currency
                        CryptoCurrency currencyTo = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getByName(generalSetting.getValue());

                        //Retrieves the equivalent value of this balance using the "From" currency and the "To" currency
                        LiveData<CryptoCurrencyEquivalence> currencyEquivalenceLiveData = CrystalDatabase.getAppDatabase(context)
                                .cryptoCurrencyEquivalenceDao().getByFromTo(
                                        currencyTo.getId(),
                                        currencyFrom.getId()

                                );

                        //Observes the equivalent value. If the equivalent value changes, this will keep the value showed correct
                        currencyEquivalenceLiveData.observe((LifecycleOwner) context, new Observer<CryptoCurrencyEquivalence>() {
                            @Override
                            public void onChanged(@Nullable CryptoCurrencyEquivalence cryptoCurrencyEquivalence) {
                                if (cryptoCurrencyEquivalence != null) {
                                    CryptoCurrency toCurrency = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getById(cryptoCurrencyEquivalence.getFromCurrencyId());
                                    String equivalenceString = String.format(
                                            "%.2f",
                                            (balance.getBalance()/Math.pow(10,currencyFrom.getPrecision()))/
                                            (cryptoCurrencyEquivalence.getValue()/Math.pow(10,toCurrency.getPrecision()))
                                    );

                                    cryptoCoinBalanceEquivalence.setText(
                                            equivalenceString + " " + toCurrency.getName());
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
