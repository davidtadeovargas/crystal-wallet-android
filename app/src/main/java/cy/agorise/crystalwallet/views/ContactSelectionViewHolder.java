package cy.agorise.crystalwallet.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.activities.CreateContactActivity;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.Contact;

/**
 * Created by Henry Varona on 2/16/2018.
 *
 * Represents an element view from the Contact Selection List
 */

public class ContactSelectionViewHolder extends RecyclerView.ViewHolder {
    private TextView tvName;
    private ImageView ivThumbnail;
    private TextView tvLastPaid;
    private Context context;
    private ContactSelectionViewHolderListener listener;

    public ContactSelectionViewHolder(View itemView) {
        super(itemView);
        //TODO: use ButterKnife to load this
        tvName = (TextView) itemView.findViewById(R.id.tvContactName);
        ivThumbnail = (ImageView) itemView.findViewById(R.id.ivContactThumbnail);
        tvLastPaid = (TextView) itemView.findViewById(R.id.tvLastPaid);
        this.context = itemView.getContext();

    }

    public void setListener(ContactSelectionViewHolderListener listener){
        this.listener = listener;
    }

    /*
     * Clears the information in this element view
     */
    public void clear(){
        tvName.setText("");
        ivThumbnail.setImageResource(android.R.color.transparent);
        tvLastPaid.setText("");
    }

    /*
     * Binds this view with the data of an element of the list
     */
    public void bindTo(final Contact contact) {
        if (contact == null){
            this.clear();
        } else {
            final ContactSelectionViewHolder thisViewHolder = this;

            tvName.setText(contact.getName());
            tvLastPaid.setText("Paid: 1 Jan, 2001 01:01");

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onContactSelected(thisViewHolder, contact);
                    }
                }
            });
        }
    }

    public interface ContactSelectionViewHolderListener {
        public void onContactSelected(ContactSelectionViewHolder contactSelectionViewHolder, Contact contact);
    }
}
