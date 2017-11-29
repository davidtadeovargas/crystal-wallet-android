package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 26/11/2017.
 */

public class ReceiveAmountValidationField extends ValidationField {

    private EditText amountField;

    public ReceiveAmountValidationField(EditText amountField) {
        super(amountField);
        this.amountField = amountField;
    }

    public void validate(){
        String newAmountText = amountField.getText().toString();
        try {
            final float newAmountValue = Float.parseFloat(newAmountText);
        } catch (NumberFormatException e){
            this.setLastValue(newAmountText);
            setValidForValue(newAmountText, false);
            validator.validationFailed(this);
            return;
        }
        this.setLastValue(newAmountText);
        this.startValidating();

        setValidForValue(newAmountText, true);
        validator.validationSucceeded(this);
    }
}
