package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;

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
                    setMessageForValue(newValue,validator.getContext().getResources().getString(R.string.account_name_not_exist));
                    setValidForValue(newValue, false);
                } else {
                    setValidForValue(newValue, true);
                }
            }
        });
        CryptoNetInfoRequests.getInstance().addRequest(request);
    }
}
