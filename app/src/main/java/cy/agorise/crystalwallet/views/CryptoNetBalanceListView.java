package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoNetBalance;
import cy.agorise.crystalwallet.viewmodels.CryptoCoinBalanceListViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetBalanceListViewModel;

/**
 * Created by Henry Varona on 10/9/2017.
 *
 * A view that show the list of crypto net balances of the user.
 * Each crypto net has its own list of crypto coin balances.
 *
 * With this view the user can see all of his/her account balances of
 * every crypto coin.
 */

public class CryptoNetBalanceListView extends RelativeLayout {

    LayoutInflater mInflater;

    /*
     * The root container of this view. Its a relative layout
     */
    View rootView;
    /*
     * The list view that holds every crypto net balance item
     */
    RecyclerView listView;
    /*
     * The adapter for the previous list view
     */
    CryptoNetBalanceListAdapter listAdapter;

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public CryptoNetBalanceListView(Context context){
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public CryptoNetBalanceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /*
     * One of three constructors needed to be inflated from a layout
     */
    public CryptoNetBalanceListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    /**
     * Initialize the list view holding the crypto net balances
     */
    public void init(){
        //inflates the corresponding view
        rootView = mInflater.inflate(R.layout.balance_list, this, true);
        this.listView = rootView.findViewById(R.id.balanceListView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.listView.setLayoutManager(linearLayoutManager);
        //Prevents the UI from an infinite scrolling of balances
        this.listView.setNestedScrollingEnabled(false);
    }

    /*
     * Sets the data for the list of balances.
     *
     * @param data the list of crypto net balances that will be show to the user
     * @param fragment a LifecycleOwner fragment to allow the inners views to use the ViewModels
     */
    public void setData(List<CryptoNetBalance> data, Fragment fragment){
        //initializes the list adapter
        if (this.listAdapter == null) {
            this.listAdapter = new CryptoNetBalanceListAdapter(fragment);
            this.listView.setAdapter(this.listAdapter);
        }

        //sets the data of the list adapter
        if (data != null) {
            this.listAdapter.setList(data);
        }
    }


}
