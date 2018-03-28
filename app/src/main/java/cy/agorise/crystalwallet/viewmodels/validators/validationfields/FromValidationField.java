package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.Spinner;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.ValidateExistBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class FromValidationField extends ValidationField {

    //private EditText fromField;
    private Spinner fromField;

    public FromValidationField(Spinner fromField){
        super(fromField);
        this.fromField = fromField;
    }

    public void validate(){
        final String newValue;

        if (fromField.getSelectedItem() != null) {
            newValue = fromField.getSelectedItem().toString();
        } else {
            newValue = "";
        }

        this.setLastValue(newValue);
        this.startValidating();
        final ValidationField field = this;

        final ValidateExistBitsharesAccountRequest request = new ValidateExistBitsharesAccountRequest(newValue);
        request.setListener(new CryptoNetInfoRequestListener() {
            @Override
            public void onCarryOut() {
                if (!request.getAccountExists()){
                    setMessageForValue(newValue,validator.getContext().getResources().getString(R.string.account_name_not_exist, "'"+newValue+"'"));
                    setValidForValue(newValue, false);
                } else {
                    setValidForValue(newValue, true);
                }
            }
        });
        CryptoNetInfoRequests.getInstance().addRequest(request);
    }
}
