package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateExistBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by Henry Varona on 2/03/2017.
 */

public class ContactNameValidationField extends ValidationField {

    private EditText nameField;
    private Contact contact;

    public ContactNameValidationField(EditText nameField){
        super(nameField);
        this.nameField = nameField;
        this.contact = null;
    }

    public ContactNameValidationField(EditText nameField, Contact contact){
        super(nameField);
        this.nameField = nameField;
        this.contact = contact;
    }

    public void validate(){
        final String newValue = this.nameField.getText().toString();

        if (this.contact != null){
            if (this.contact.getName().equals(newValue)){
                this.setLastValue(newValue);
                this.startValidating();
                this.setValidForValue(newValue, true);
                return;
            }
        }

        if (!newValue.equals("")) {
            if (!newValue.equals(this.getLastValue())) {
                this.setLastValue(newValue);
                this.startValidating();

                ContactListViewModel contactListViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ContactListViewModel.class);
                if (contactListViewModel.contactExists(newValue)) {
                    this.setMessageForValue(newValue, "This name is already used by another contact.");
                    this.setValidForValue(newValue, false);
                } else {
                    this.setValidForValue(newValue, true);
                }
            }
        } else {
            this.setLastValue("");
            this.startValidating();
            this.setMessageForValue("", "");
            this.setValidForValue("", false);
        }
    }
}
