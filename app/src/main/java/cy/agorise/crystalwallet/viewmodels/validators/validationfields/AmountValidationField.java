package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class AmountValidationField extends ValidationField {

    private EditText amountField;
    private Spinner assetSpinner;
    private CryptoNetAccount account;

    public AmountValidationField(EditText amountField, Spinner assetSpinner, CryptoNetAccount account) {
        super(amountField);
        this.amountField = amountField;
        this.assetSpinner = assetSpinner;
        this.account = account;
    }

    public void validate(){
        try {
            final float newAmountValue = Float.parseFloat(amountField.getText().toString());
            final CryptoCurrency cryptoCurrency = (CryptoCurrency)assetSpinner.getSelectedItem();
            final String mixedValues = newAmountValue+"_"+cryptoCurrency.getId();
            this.setLastValue(mixedValues);
            this.startValidating();
            final ValidationField field = this;

            CryptoCoinBalance balance = CrystalDatabase.getAppDatabase(amountField.getContext()).cryptoCoinBalanceDao().getBalanceFromAccount(this.account.getId(),cryptoCurrency.getId());

            if (newAmountValue > balance.getBalance()){
                setValidForValue(mixedValues, false);
                setMessage(validator.getContext().getResources().getString(R.string.insufficient_amount));
                validator.validationFailed(field);
            } else if (newAmountValue == 0){
                setValidForValue(mixedValues, false);
                setMessage(validator.getContext().getResources().getString(R.string.amount_should_be_greater_than_zero));
                validator.validationFailed(field);
            } else {
                setValidForValue(mixedValues, true);
                validator.validationSucceeded(field);
            }
        } catch (NumberFormatException e){
            setLastValue("");
            setValidForValue("", false);
            setMessage(validator.getContext().getResources().getString(R.string.please_enter_valid_amount));
            validator.validationFailed(this);
        }
    }
}
