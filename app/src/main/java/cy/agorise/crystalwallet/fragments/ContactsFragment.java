package cy.agorise.crystalwallet.fragments;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.util.BounceTouchListener;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.views.ContactListAdapter;

public class ContactsFragment extends Fragment {

    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;

    ContactListAdapter adapter;

    // Fields used to achieve bounce effect while over-scrolling the contacts list
    private BounceTouchListener bounceTouchListener;
    float pivotY1, pivotY2;

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
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactListAdapter();
        rvContacts.setAdapter(adapter);

        configureListBounceEffect();

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

    private void configureListBounceEffect() {
        rvContacts.setPivotX(getScreenWidth(getActivity()) * 0.5f);

        pivotY1 = 0;
        pivotY2 = (getScreenHeight(getActivity())) * .5f;

        bounceTouchListener = BounceTouchListener.create(rvContacts, new BounceTouchListener.OnTranslateListener() {
            @Override
            public void onTranslate(float translation) {
                if(translation > 0) {
                    bounceTouchListener.setMaxAbsTranslation(-99);
                    rvContacts.setPivotY(pivotY1);
                    float scale = ((2 * translation) / rvContacts.getMeasuredHeight()) + 1;
                    rvContacts.setScaleY((float) Math.pow(scale, .6f));
                } else {
                    bounceTouchListener.setMaxAbsTranslation((int) (pivotY2 * .33f));
                    rvContacts.setPivotY(pivotY2);
                    float scale = ((2 * translation) / rvContacts.getMeasuredHeight()) + 1;
                    rvContacts.setScaleY((float) Math.pow(scale, .5f));
                }
            }
        });

        // Sets custom touch listener to handle bounce/stretch effect
        rvContacts.setOnTouchListener(bounceTouchListener);
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
