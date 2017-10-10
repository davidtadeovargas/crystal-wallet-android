package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class BitsharesAccountNameValidationField extends ValidationField {

    private EditText accountNameField;

    public BitsharesAccountNameValidationField(EditText accountNameField){
        super(accountNameField);
        this.accountNameField = accountNameField;
    }

    public void validate(){
        final String newValue = accountNameField.getText().toString();
        this.setLastValue(newValue);
        this.startValidating();
        final ValidationField field = this;

        final ValidateExistBitsharesAccountRequest request = new ValidateExistBitsharesAccountRequest(newValue);
        request.setListener(new CryptoNetInfoRequestListener() {
            @Override
            public void onCarryOut() {
                if (!request.getAccountExists()){
                    setValidForValue(newValue, false);
                    setMessage(validator.getContext().getResources().getString(R.string.account_name_not_exist));
                    validator.validationFailed(field);
                } else {
                    setValidForValue(newValue, true);
                    validator.validationSucceeded(field);
                }
            }
        });
        CryptoNetInfoRequests.getInstance().addRequest(request);
    }
}
