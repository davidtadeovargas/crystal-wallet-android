package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.R;
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
                setMessageForValue(mixedValues, validator.getContext().getResources().getString(R.string.insufficient_amount));
                setValidForValue(mixedValues, false);
            } else if (newAmountValue == 0){
                setMessageForValue(mixedValues, validator.getContext().getResources().getString(R.string.amount_should_be_greater_than_zero));
                setValidForValue(mixedValues, false);
            } else {
                setValidForValue(mixedValues, true);
            }
        } catch (NumberFormatException e){
            setLastValue("");
            setMessageForValue("",validator.getContext().getResources().getString(R.string.please_enter_valid_amount));
            setValidForValue("", false);
        }
    }
}
