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
 */

public class CryptoCoinBalanceViewHolder extends RecyclerView.ViewHolder {
    private TextView cryptoCoinName;
    private TextView cryptoCoinBalance;
    private TextView cryptoCoinBalanceEquivalence;
    private Context context;

    public CryptoCoinBalanceViewHolder(View itemView) {
        super(itemView);
        cryptoCoinName = (TextView) itemView.findViewById(R.id.tvCryptoCoinName);
        cryptoCoinBalance = (TextView) itemView.findViewById(R.id.tvCryptoCoinBalanceAmount);
        cryptoCoinBalanceEquivalence = (TextView) itemView.findViewById(R.id.tvCryptoCoinBalanceEquivalence);
        this.context = itemView.getContext();

    }

    public void clear(){
        cryptoCoinName.setText("loading...");
        cryptoCoinBalance.setText("");
        cryptoCoinBalanceEquivalence.setText("");
    }

    public void bindTo(final CryptoCoinBalance balance/*, final OnTransactionClickListener listener*/) {
        if (balance == null){
            cryptoCoinName.setText("loading...");
            cryptoCoinBalance.setText("");
            cryptoCoinBalanceEquivalence.setText("");
        } else {
            LiveData<GeneralSetting> preferedCurrencySetting = CrystalDatabase.getAppDatabase(this.context).generalSettingDao().getByName(GeneralSetting.SETTING_NAME_PREFERED_CURRENCY);
            final CryptoCurrency currencyFrom = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getById(balance.getCryptoCurrencyId());
            cryptoCoinName.setText(currencyFrom.getName());
            cryptoCoinBalance.setText("" + balance.getBalance());

            preferedCurrencySetting.observe((LifecycleOwner) this.context, new Observer<GeneralSetting>() {
                @Override
                public void onChanged(@Nullable GeneralSetting generalSetting) {
                    if (generalSetting != null) {
                        CryptoCurrency currencyTo = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getByName(generalSetting.getValue());

                        LiveData<CryptoCurrencyEquivalence> currencyEquivalenceLiveData = CrystalDatabase.getAppDatabase(context)
                                .cryptoCurrencyEquivalenceDao().getByFromTo(
                                        currencyFrom.getId(),
                                        currencyTo.getId()
                                );

                        currencyEquivalenceLiveData.observe((LifecycleOwner) context, new Observer<CryptoCurrencyEquivalence>() {
                            @Override
                            public void onChanged(@Nullable CryptoCurrencyEquivalence cryptoCurrencyEquivalence) {
                                if (cryptoCurrencyEquivalence != null) {
                                    CryptoCurrency toCurrency = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getById(cryptoCurrencyEquivalence.getToCurrencyId());
                                    cryptoCoinBalanceEquivalence.setText(cryptoCurrencyEquivalence.getValue() + " " + toCurrency.getName());
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
