package cy.agorise.crystalwallet.views;

import android.arch.paging.PagedList;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.viewmodels.TransactionListViewModel;

/**
 * Created by Henry Varona on 1/15/2018.
 *
 * A list view showing the user contacts
 */

public class ContactListView extends RelativeLayout {

    LayoutInflater mInflater;

    /*
     * The root view of this view
     */
    View rootView;
    /*
     * The list view that holds every user contact item
     */
    RecyclerView listView;
    /*
     * The adapter for the previous list view
     */
    ContactListAdapter listAdapter;

    ContactListViewModel contactListViewModel;

    /*
     * how much contacts will remain to show before the list loads more
     */
    private int visibleThreshold = 5;
    /*
     * if true, the contact list will be loading new data
     */
    private boolean loading = true;

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public ContactListView(Context context){
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public ContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public ContactListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /*
     * Initializes this view
     */
    public void init(){
        rootView = mInflater.inflate(R.layout.contact_list, this, true);
        this.listView = rootView.findViewById(R.id.contactListView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.listView.setLayoutManager(linearLayoutManager);
        //Prevents the list to start again when scrolling to the end
        this.listView.setNestedScrollingEnabled(false);

    }

    /*
     * Sets the elements data of this view
     *
     * @param data the contacts that will be showed to the user
     */
    public void setData(PagedList<Contact> data){
        //Initializes the adapter of the contact list
        if (this.listAdapter == null) {
            this.listAdapter = new ContactListAdapter();
            this.listView.setAdapter(this.listAdapter);
        }

        //Sets the data of the transaction list
        if (data != null) {
            this.listAdapter.setList(data);
        }
    }


}
