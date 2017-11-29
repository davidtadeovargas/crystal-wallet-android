package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class ToValidationField extends ValidationField {

    private EditText fromField;
    private EditText toField;

    public ToValidationField(EditText fromField, EditText toField){
        super(toField);
        this.fromField = fromField;
        this.toField = toField;
    }

    public void validate(){
        final String fromNewValue = fromField.getText().toString();
        final String toNewValue = toField.getText().toString();
        final String mixedValue = fromNewValue+"_"+toNewValue;
        this.setLastValue(mixedValue);
        this.startValidating();
        final ValidationField field = this;

        if (fromNewValue.equals(toNewValue)){
            setValidForValue(mixedValue, false);
            setMessage(validator.getContext().getResources().getString(R.string.warning_msg_same_account));
            validator.validationFailed(field);
        } else {

            final ValidateExistBitsharesAccountRequest request = new ValidateExistBitsharesAccountRequest(toNewValue);
            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {
                    if (!request.getAccountExists()) {
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
}
