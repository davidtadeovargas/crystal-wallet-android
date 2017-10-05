package cy.agorise.crystalwallet.viewmodels.validators;

import android.accounts.Account;
import android.content.res.Resources;

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
    private Resources res;

    private boolean isValid = false;

    public ImportSeedValidator(Resources res){
        this.res = res;
        this.validationFields = new ArrayList<ValidationField>();
        //this.validationFields.add(new ValidationField("pin"));
        //this.validationFields.add(new ValidationField("pinConfirmation"));
        this.validationFields.add(new ValidationField("accountname"));
    }

    public void setListener(ImportSeedValidatorListener listener){
        this.listener = listener;
    }

    public boolean isValid(){
        for(int i=0;i<this.validationFields.size();i++){
            ValidationField nextField = this.validationFields.get(i);

            if (!nextField.getValid()){
                return false;
            }
        }

        return true;
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

    public void validateAccountName(final String accountName, final String mnemonic){
        final ValidationField validationField = getValidationField("accountname");
        validationField.setLastValue(accountName);

        final ValidateImportBitsharesAccountRequest request = new ValidateImportBitsharesAccountRequest(accountName,mnemonic);
        request.setListener(new CryptoNetInfoRequestListener() {
            @Override
            public void onCarryOut() {
                if (!request.getAccountExists()){
                    validationField.setValidForValue(accountName, false);
                    validationField.setMessage(res.getString(R.string.account_name_not_exist));
                } else {
                    validationField.setValidForValue(accountName, true);
                }
            }
        });
        CryptoNetInfoRequests.getInstance().addRequest(request);
    }
}
