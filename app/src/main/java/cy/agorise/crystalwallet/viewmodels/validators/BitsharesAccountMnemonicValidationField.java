package cy.agorise.crystalwallet.viewmodels.validators;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class BitsharesAccountMnemonicValidationField extends ValidationField {

    private EditText mnemonicField;
    private EditText accountNameField;

    public BitsharesAccountMnemonicValidationField(EditText mnemonicField, EditText accountNameField){
        super(mnemonicField);
        this.mnemonicField = mnemonicField;
        this.accountNameField = accountNameField;
    }

    public void validate(){
        final String newMnemonicValue = mnemonicField.getText().toString();
        final String newAccountNameValue = accountNameField.getText().toString();
        final String mixedValue = newMnemonicValue+"_"+newAccountNameValue;

        this.setLastValue(mixedValue);
        this.startValidating();
        final ValidationField field = this;

        final ValidateImportBitsharesAccountRequest request = new ValidateImportBitsharesAccountRequest(newAccountNameValue,newMnemonicValue);
        request.setListener(new CryptoNetInfoRequestListener() {
            @Override
            public void onCarryOut() {
                if (!request.getMnemonicIsCorrect()){
                    setValidForValue(mixedValue, false);
                    setMessage(validator.getContext().getResources().getString(R.string.account_name_not_exist));
                    validator.validationFailed(field);
                } else {
                    setValidForValue(mixedValue, true);
                    validator.validationSucceeded(field);
                }
            }
        });
        CryptoNetInfoRequests.getInstance().addRequest(request);
    }
}
