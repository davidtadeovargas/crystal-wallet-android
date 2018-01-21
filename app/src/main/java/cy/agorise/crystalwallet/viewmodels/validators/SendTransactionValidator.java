package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;

import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.AmountValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.AssetValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.FromValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.MemoValidationField;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ToValidationField;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class SendTransactionValidator extends UIValidator {

    private CryptoNetAccount account;


    public SendTransactionValidator(Context context, CryptoNetAccount account, Spinner fromEdit, EditText toEdit, Spinner assetSpinner, EditText amountEdit, EditText memoEdit){
        super(context);
        this.account = account;
        this.addField(new FromValidationField(fromEdit));
        this.addField(new ToValidationField(fromEdit, toEdit));
        this.addField(new AssetValidationField(assetSpinner));
        this.addField(new AmountValidationField(amountEdit, assetSpinner, this.account));
        this.addField(new MemoValidationField(memoEdit));
    }

    public CryptoNetAccount getAccount() {
        return account;
    }
}
