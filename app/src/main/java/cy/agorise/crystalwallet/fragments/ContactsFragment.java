package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.views.ContactListView;

public class ContactsFragment extends Fragment {

    @BindView(R.id.vContactListView)
    ContactListView contactListView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, v);

        ContactListViewModel contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        contactListViewModel.init();
        LiveData<PagedList<Contact>> contactsLiveData = contactListViewModel.getContactList();

        contactsLiveData.observe(this, new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(@Nullable PagedList<Contact> contacts) {
                contactListView.setData(contacts);
            }
        });

        return v;
    }
}
