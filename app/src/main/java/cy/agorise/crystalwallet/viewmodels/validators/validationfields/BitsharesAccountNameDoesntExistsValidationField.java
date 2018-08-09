package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.ValidateExistBitsharesAccountRequest;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class BitsharesAccountNameDoesntExistsValidationField extends ValidationField {

    private EditText accountNameField;

    public BitsharesAccountNameDoesntExistsValidationField(EditText accountNameField){
        super(accountNameField);
        this.accountNameField = accountNameField;
    }

    public void validate(){

        final String newValue = accountNameField.getText().toString();
        this.setLastValue(newValue);
        this.startValidating();

        /*
        * Validate empty field
        * */
        if (newValue.equals("")){
            setValidForValue("", false);
            setMessageForValue("","");
            validator.validationFailed(this);
        }
        /*
            Validate at least min length
        */
        else if(newValue.length()<10){
            setValidForValue("", false);
            setMessageForValue(validator.getContext().getResources().getString(R.string.create_account_window_err_min_account_name_len),"");
            validator.validationFailed(this);
        }
        /*
            Validate at least one number for the account string
        */
        else if(!newValue.matches(".*\\\\d+.*")){
            setValidForValue("", false);
            setMessageForValue(validator.getContext().getResources().getString(R.string.create_account_window_err_at_least_one_number),"");
            validator.validationFailed(this);
        }
        /*
            Validate at least one middle script
        */
        else if(!newValue.contains("-")){
            setValidForValue("", false);
            setMessageForValue(accountNameField.getContext().getResources().getString(R.string.create_account_window_err_at_least_one_number),"");
            validator.validationFailed(this);
        }
        /*
        *   Passed all primary validations
        * */
        else {

            final ValidationField field = this;

            final ValidateExistBitsharesAccountRequest request = new ValidateExistBitsharesAccountRequest(newValue);
            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {

                    if (request.getAccountExists()) {

                        /*
                        *   The account exists and is not valid
                        * */
                        setMessageForValue(newValue,validator.getContext().getResources().getString(R.string.account_name_already_exist,"'"+newValue+"'"));
                        setValidForValue(newValue, false);

                    } else {
                       setValidForValue(newValue, true);
                    }
                }
            });
            CryptoNetInfoRequests.getInstance().addRequest(request);
        }
    }
}
