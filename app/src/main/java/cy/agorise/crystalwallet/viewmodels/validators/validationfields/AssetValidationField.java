package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class AssetValidationField extends ValidationField {

    private Spinner assetField;

    public AssetValidationField(Spinner assetField){
        super(assetField);
        this.assetField = assetField;
    }

    public void validate(){
        final CryptoCurrency cryptoCurrencySelected = (CryptoCurrency) this.assetField.getSelectedItem();
        final String newValue = ""+cryptoCurrencySelected.getId();
        this.setLastValue(newValue);
        validator.validationSucceeded(this);
    }
}
