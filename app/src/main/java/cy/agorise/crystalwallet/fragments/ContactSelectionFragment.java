package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountListViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.ContactSelectionListAdapter;
import cy.agorise.crystalwallet.views.CryptoCurrencyAdapter;
import cy.agorise.crystalwallet.views.CryptoNetAccountAdapter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ContactSelectionFragment extends DialogFragment implements ContactSelectionListAdapter.ContactSelectionListAdapterListener{

    private CryptoNet cryptoNet;
    private CrystalDatabase db;
    private AlertDialog.Builder builder;

    @BindView(R.id.contactListView)
    RecyclerView contactSelectionListView;

    public static ContactSelectionFragment newInstance(CryptoNet cryptoNet) {
        ContactSelectionFragment f = new ContactSelectionFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("CRYPTO_NET", cryptoNet.name());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog.Builder
        builder = new AlertDialog.Builder(getActivity(), R.style.SendTransactionTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_list, null);
        ButterKnife.bind(this, view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.contactSelectionListView.setLayoutManager(linearLayoutManager);
        //Prevents the list to start again when scrolling to the end
        this.contactSelectionListView.setNestedScrollingEnabled(false);
        final ContactSelectionListAdapter contactSelectionListAdapter = new ContactSelectionListAdapter();
        contactSelectionListAdapter.setListener(this);
        contactSelectionListView.setAdapter(contactSelectionListAdapter);

        this.cryptoNet  = CryptoNet.valueOf(getArguments().getString("CRYPTO_NET"));
        if (this.cryptoNet != null) {
            ContactListViewModel contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
            contactListViewModel.init(this.cryptoNet);
            LiveData<PagedList<Contact>> contactsLiveData = contactListViewModel.getContactList();

            contactsLiveData.observe(this, new Observer<PagedList<Contact>>() {
                @Override
                public void onChanged(@Nullable PagedList<Contact> contacts) {
                    contactSelectionListAdapter.setList(contacts);
                }
            });
        }

        return builder.setView(view).create();
    }

    @Override
    public void onContactSelected(Contact contact) {
        Intent result = new Intent();
        result.putExtra("CONTACT_ID", contact.getId());
        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, result);
        this.dismiss();
    }
}
