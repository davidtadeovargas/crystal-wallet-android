package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.AmountValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.AssetValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.FromValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.MemoValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ReceiveAmountValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ToValidationField;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class ReceiveTransactionValidator extends UIValidator {

    private CryptoNetAccount account;


    public ReceiveTransactionValidator(Context context, CryptoNetAccount account, Spinner assetSpinner, EditText amountEdit){
        super(context);
        this.account = account;
        this.addField(new AssetValidationField(assetSpinner));
        this.addField(new ReceiveAmountValidationField(amountEdit));
    }

    public CryptoNetAccount getAccount() {
        return account;
    }
}
