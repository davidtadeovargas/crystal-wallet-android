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
            CryptoCurrency currency = CrystalDatabase.getAppDatabase(this.context).cryptoCurrencyDao().getById(balance.getCryptoCurrencyId());
            CryptoCurrency currencyBitEur = CrystalDatabase.getAppDatabase(this.context).cryptoCurrencyDao().getByName("EUR");

            LiveData<CryptoCurrencyEquivalence> currencyEquivalenceLiveData = CrystalDatabase.getAppDatabase(this.context)
                    .cryptoCurrencyEquivalenceDao().getByFromTo(
                            currency.getId(),
                            currencyBitEur.getId()
                    );

            cryptoCoinName.setText(currency.getName());
            cryptoCoinBalance.setText(""+balance.getBalance());

            currencyEquivalenceLiveData.observe((LifecycleOwner) this.context, new Observer<CryptoCurrencyEquivalence>() {
                @Override
                public void onChanged(@Nullable CryptoCurrencyEquivalence cryptoCurrencyEquivalence) {
                    if (cryptoCurrencyEquivalence != null){
                        CryptoCurrency toCurrency = CrystalDatabase.getAppDatabase(context).cryptoCurrencyDao().getById(cryptoCurrencyEquivalence.getToCurrencyId());
                        cryptoCoinBalanceEquivalence.setText(cryptoCurrencyEquivalence.getValue()+" "+toCurrency.getName());
                    }
                }
            });
        }
    }
}
