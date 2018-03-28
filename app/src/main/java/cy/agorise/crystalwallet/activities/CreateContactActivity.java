package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.ContactAddress;
import cy.agorise.crystalwallet.viewmodels.ContactViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.CreateContactValidator;
import cy.agorise.crystalwallet.viewmodels.validators.ModifyContactValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.ContactAddressListAdapter;

public class CreateContactActivity extends AppCompatActivity implements UIValidatorListener {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvNameError)
    TextView tvNameError;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tvEmailError)
    TextView tvEmailError;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnCreate)
    Button btnCreate;
    @BindView(R.id.btnModify)
    Button btnModify;
    @BindView(R.id.rvContactAddresses)
    RecyclerView rvContactAddresses;
    @BindView(R.id.btnAddAddress)
    Button btnAddAddress;
    List<ContactAddress> contactAddressList;
    ContactAddressListAdapter listAdapter;

    CreateContactValidator createContactValidator;
    ModifyContactValidator modifyContactValidator;

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        ButterKnife.bind(this);

        btnCreate.setEnabled(false);

        listAdapter = new ContactAddressListAdapter();
        rvContactAddresses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvContactAddresses.setAdapter(listAdapter);

        long contactId = this.getIntent().getLongExtra("CONTACT_ID",-1);

        btnCreate.setVisibility(View.GONE);
        btnModify.setVisibility(View.GONE);

        if (contactId >= 0){
            final ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

            contactViewModel.init(contactId);
            LiveData<Contact> contactLiveData = contactViewModel.getContact();
            final CreateContactActivity thisActivity = this;

            contactLiveData.observe(this, new Observer<Contact>() {
                @Override
                public void onChanged(@Nullable Contact contactChanged) {
                    if (contactChanged != null){
                        contact = contactChanged;
                        etName.setText(contact.getName());
                        etEmail.setText(contact.getEmail());

                        LiveData<List<ContactAddress>> contactAddresses = contactViewModel.getContactAddresses();

                        contactAddresses.observe(thisActivity, new Observer<List<ContactAddress>>() {
                            @Override
                            public void onChanged(@Nullable List<ContactAddress> contactAddresses) {
                                contactAddressList = contactAddresses;
                                listAdapter.setList(contactAddressList);
                                listAdapter.notifyDataSetChanged();
                            }
                        });

                        modifyContactValidator = new ModifyContactValidator(thisActivity.getApplicationContext(),contact,etName,etEmail);
                        modifyContactValidator.setListener(thisActivity);
                        btnModify.setVisibility(View.VISIBLE);
                    } else {
                        //No contact was found, this will exit the activity
                        finish();
                    }
                }
            });
        } else {
            contactAddressList = new ArrayList<ContactAddress>();
            listAdapter.setList(contactAddressList);
            createContactValidator = new CreateContactValidator(this.getApplicationContext(),etName,etEmail);
            createContactValidator.setListener(this);

            btnCreate.setVisibility(View.VISIBLE);
        }
    }

    public void validate(){
        if (this.createContactValidator != null){
            this.createContactValidator.validate();
        } else if (this.modifyContactValidator != null){
            this.modifyContactValidator.validate();
        }
    }

    public boolean isValid(){
        if (this.createContactValidator != null){
            return this.createContactValidator.isValid();
        } else if (this.modifyContactValidator != null){
            return this.modifyContactValidator.isValid();
        }

        return false;
    }

    @OnTextChanged(value = R.id.etName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterContactNameChanged(Editable editable) {
        this.validate();
    }

    @OnTextChanged(value = R.id.etEmail,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterEmailChanged(Editable editable) {
        this.validate();
    }


    @OnClick(R.id.btnAddAddress)
    public void addAddress(){
        ContactAddress newContactAddress = new ContactAddress();
        this.contactAddressList.add(newContactAddress);
        this.listAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.finish();
    }

    @OnClick(R.id.btnModify)
    public void modifyContact(){
        if (this.modifyContactValidator.isValid()) {
            this.contact.setName(etName.getText().toString());
            this.contact.setEmail(etEmail.getText().toString());
            this.contact.clearAddresses();

            for (ContactAddress contactAddress : contactAddressList){
                this.contact.addAddress(contactAddress);
            }

            ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
            if (contactViewModel.modifyContact(this.contact)){
                this.finish();
            } else {
                this.modifyContactValidator.validate();
            }
        }
    }

    @OnClick(R.id.btnCreate)
    public void createContact(){
        if (this.createContactValidator.isValid()) {
            Contact newContact = new Contact();
            newContact.setName(etName.getText().toString());
            newContact.setEmail(etEmail.getText().toString());

            for (ContactAddress contactAddress : contactAddressList){
                newContact.addAddress(contactAddress);
            }

            ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
            if (contactViewModel.addContact(newContact)){
                this.finish();
            } else {
                createContactValidator.validate();
            }
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final CreateContactActivity activity = this;

        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == etName) {
                    tvNameError.setText("");
                } else if (field.getView() == etEmail) {
                    tvEmailError.setText("");
                }

                if (activity.isValid()){
                    btnCreate.setEnabled(true);
                } else {
                    btnCreate.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onValidationFailed(final ValidationField field) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (field.getView() == etName) {
                    tvNameError.setText(field.getMessage());
                } else if (field.getView() == etEmail) {
                    tvEmailError.setText(field.getMessage());
                }
            }
        });
    }
}
