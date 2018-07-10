package cy.agorise.crystalwallet.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.views.ContactListAdapter;

public class ContactsFragment extends Fragment {

    @BindView(R.id.recyclerViewContacts)
    RecyclerView recyclerViewContacts;

    ContactListAdapter adapter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);

        // Configure RecyclerView and its adapter
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactListAdapter();
        recyclerViewContacts.setAdapter(adapter);

        // Gets contacts LiveData instance from ContactsViewModel
        ContactListViewModel contactListViewModel =
                ViewModelProviders.of(this).get(ContactListViewModel.class);
        LiveData<PagedList<Contact>> contactsLiveData = contactListViewModel.getContactList();

        contactsLiveData.observe(this, new Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(@Nullable PagedList<Contact> contacts) {
                adapter.submitList(contacts);
            }
        });

        return view;
    }
}
