package cy.agorise.crystalwallet.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;

/**
 * Created by Henry Varona on 01/20/2018.
 *
 * The adapter to show a list of crypto net account in a spinner.
 */

public class CryptoNetAccountAdapter extends ArrayAdapter<CryptoNetAccount> {
    private List<CryptoNetAccount> data;

    public CryptoNetAccountAdapter(Context context, int resource, List<CryptoNetAccount> objects) {
        super(context, resource, objects);
        this.data = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /*
     * Creates the view for every element of the spinner
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.crypto_net_account_adapter_item, parent, false);
        TextView tvCryptoNetAccountName = v.findViewById(R.id.tvCryptoNetAccountName);

        CryptoNetAccount cryptoNetAccount = getItem(position);
        tvCryptoNetAccountName.setText(cryptoNetAccount.getName());

        return v;
    }
}
