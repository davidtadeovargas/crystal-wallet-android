package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * Created by Henry Varona on 25/10/2017.
 */

public class CryptoCurrencyAdapter extends ArrayAdapter<CryptoCurrency> {
    private List<CryptoCurrency> data;

    public CryptoCurrencyAdapter(Context context, int resource, List<CryptoCurrency> objects) {
        super(context, resource, objects);
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.crypto_currency_adapter_item, parent, false);
        TextView tvCryptoCurrencyName = v.findViewById(R.id.tvCryptoCurrencyName);

        CryptoCurrency cryptoCurrency = getItem(position);
        tvCryptoCurrencyName.setText(cryptoCurrency.getName());

        return v;
    }
}
