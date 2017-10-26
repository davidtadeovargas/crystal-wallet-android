package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class MemoValidationField extends ValidationField {

    private EditText memoField;

    public MemoValidationField(EditText memoField){
        super(memoField);
        this.memoField = memoField;
    }

    public void validate(){
        final String memoNewValue = memoField.getText().toString();
        this.setLastValue(memoNewValue);
        setValidForValue(memoNewValue, true);
        validator.validationSucceeded(this);
    }
}
