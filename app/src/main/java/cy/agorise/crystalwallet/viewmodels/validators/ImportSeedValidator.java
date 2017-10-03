package cy.agorise.crystalwallet.viewmodels.validators;

import android.accounts.Account;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateImportBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.AccountSeed;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class ImportSeedValidator {

    private ImportSeedValidatorListener listener;

    private List<ValidationField> validationFields;
    private AccountSeed accountSeed;

    private boolean isValid = false;

    public ImportSeedValidator(AccountSeed seed){
        this.accountSeed = seed;
        this.validationFields = new ArrayList<ValidationField>();
        //this.validationFields.add(new ValidationField("pin"));
        //this.validationFields.add(new ValidationField("pinConfirmation"));
        this.validationFields.add(new ValidationField("accountname"));
    }

    public void setListener(ImportSeedValidatorListener listener){
        this.listener = listener;
    }

    public void validate(){
        //validatePin();
        //validatePinConfirmation();
        //validateAccountName();
    }

    public ValidationField getValidationField(String name){
        for (int i=0;i<this.validationFields.size();i++){
            if (this.validationFields.get(i).getName().equals(name)){
                return this.validationFields.get(i);
            }
        }

        return null;
    }

    //public validatePin(){

    //}

    public void validateAccountName(String accountName){
        final ValidationField validationField = getValidationField("accountname");

        if (this.accountSeed != null){
            final ValidateImportBitsharesAccountRequest request = new ValidateImportBitsharesAccountRequest(this.accountSeed.getName(),this.accountSeed.getMasterSeed());
            request.setListener(new CryptoNetInfoRequestListener() {
                @Override
                public void onCarryOut() {
                    if (!request.getAccountExists()){
                        validationField.setValid(false);
                        //validationField.setMessage(R.string.account_name_not_exist);
                    } else {
                        validationField.setValid(true);
                    }
                }
            });
            CryptoNetInfoRequests.getInstance().addRequest(request);
        }
    }
}
