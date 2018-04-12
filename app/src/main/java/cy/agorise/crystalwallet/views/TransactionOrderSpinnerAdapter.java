package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.CryptoCurrency;

/**
 * Created by Henry Varona on 09/04/2018.
 *
 */

public class TransactionOrderSpinnerAdapter extends ArrayAdapter<TransactionOrderSpinnerAdapter.TransactionOrderSpinnerItem> {
    public static class TransactionOrderSpinnerItem {
        private String field;
        private String label;
        private boolean ascending;
        private int order;

        public TransactionOrderSpinnerItem(String field, String label, int order, boolean ascending){
            this.field = field;
            this.label = label;
            this.ascending = ascending;
            this.order = order;
        }

        public void setAscending(boolean newValue){
            this.ascending = newValue;
        }

        public void setOrder(int newValue){
            this.order = newValue;
        }

        public String getField(){
            return this.field;
        }

        public String getLabel(){
            return this.label;
        }

        public int getOrder(){
            return this.order;
        }

        public boolean getAscending(){
            return this.ascending;
        }
    }


    private List<TransactionOrderSpinnerItem> data;

    public TransactionOrderSpinnerAdapter(Context context, int resource, List<TransactionOrderSpinnerItem> objects) {
        super(context, resource, objects);
        this.data = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /*
     * Creates the view for every element of the spinner
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.transactions_order_adapter_item, parent, false);
        TextView tvTransactionOrderLabel = v.findViewById(R.id.tvTransactionOrderLabel);

        TransactionOrderSpinnerItem transactionOrderSpinnerItem = getItem(position);
        tvTransactionOrderLabel.setText(transactionOrderSpinnerItem.getLabel());

        return v;
    }
}
