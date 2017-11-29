package cy.agorise.crystalwallet.fragments;

import android.app.Dialog;
import android.app.LauncherActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import cy.agorise.graphenej.Invoice;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateBitsharesSendRequest;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinBalance;
import cy.agorise.crystalwallet.models.CryptoCurrency;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.validators.ReceiveTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.SendTransactionValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;
import cy.agorise.crystalwallet.views.CryptoCurrencyAdapter;
import cy.agorise.graphenej.LineItem;

public class ReceiveTransactionFragment extends DialogFragment implements UIValidatorListener {

    ReceiveTransactionValidator receiveTransactionValidator;

    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAmountError)
    TextView tvAmountError;
    @BindView(R.id.spAsset)
    Spinner spAsset;
    @BindView(R.id.tvAssetError)
    TextView tvAssetError;
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;

    private Button btnShareQrCode;
    private Button btnClose;

    private long cryptoNetAccountId;
    private CryptoNetAccount cryptoNetAccount;
    private CryptoCurrency cryptoCurrency;
    private GrapheneAccount grapheneAccount;
    private CrystalDatabase db;

    private Invoice invoice;
    private ArrayList<LineItem> invoiceItems;

    public static ReceiveTransactionFragment newInstance(long cryptoNetAccountId) {
        ReceiveTransactionFragment f = new ReceiveTransactionFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("CRYPTO_NET_ACCOUNT_ID", cryptoNetAccountId);
        f.setArguments(args);

        f.invoiceItems = new ArrayList<LineItem>();
        f.invoice = new Invoice("","","","",null,"","");

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Receive Assets");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.receive_transaction, null);
        ButterKnife.bind(this, view);

        this.cryptoNetAccountId  = getArguments().getLong("CRYPTO_NET_ACCOUNT_ID",-1);

        if (this.cryptoNetAccountId != -1) {
            db = CrystalDatabase.getAppDatabase(this.getContext());
            this.cryptoNetAccount = db.cryptoNetAccountDao().getById(this.cryptoNetAccountId);

            /*
            * this is only for graphene accounts.
            *
            **/
            this.grapheneAccount = new GrapheneAccount(this.cryptoNetAccount);
            this.grapheneAccount.loadInfo(db.grapheneAccountInfoDao().getByAccountId(this.cryptoNetAccountId));

            final LiveData<List<CryptoCoinBalance>> balancesList = db.cryptoCoinBalanceDao().getBalancesFromAccount(cryptoNetAccountId);
            balancesList.observe(this, new Observer<List<CryptoCoinBalance>>() {
                @Override
                public void onChanged(@Nullable List<CryptoCoinBalance> cryptoCoinBalances) {
                    ArrayList<Long> assetIds = new ArrayList<Long>();
                    for (CryptoCoinBalance nextBalance : balancesList.getValue()) {
                        assetIds.add(nextBalance.getCryptoCurrencyId());
                    }
                    List<CryptoCurrency> cryptoCurrencyList = db.cryptoCurrencyDao().getByIds(assetIds);

                    CryptoCurrencyAdapter assetAdapter = new CryptoCurrencyAdapter(getContext(), android.R.layout.simple_spinner_item, cryptoCurrencyList);
                    spAsset.setAdapter(assetAdapter);
                }
            });

            receiveTransactionValidator = new ReceiveTransactionValidator(this.getContext(), this.cryptoNetAccount, spAsset, etAmount);
            receiveTransactionValidator.setListener(this);
        }

        builder.setView(view);

        builder.setPositiveButton("Share this QR",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareQrCode();
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                btnShareQrCode = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                btnClose = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                btnShareQrCode.setEnabled(false);
            }
        });

        return dialog;
    }

    @OnTextChanged(value = R.id.etAmount,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAmountChanged(Editable editable) {
        this.receiveTransactionValidator.validate();
    }

    @OnItemSelected(R.id.spAsset)
    public void afterAssetSelected(Spinner spinner, int position) {
        this.cryptoCurrency = (CryptoCurrency)spinner.getSelectedItem();
        this.receiveTransactionValidator.validate();
    }

    public void shareQrCode(){
        if (this.receiveTransactionValidator.isValid()) {
            //Share Qr Code
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final ReceiveTransactionFragment fragment = this;


        if (field.getView() == etAmount){
            tvAmountError.setText("");
        } else if (field.getView() == spAsset){
            tvAssetError.setText("");
        }

        if (btnShareQrCode != null) {
            if (receiveTransactionValidator.isValid()) {
                createQrCode();
                btnShareQrCode.setEnabled(true);
            } else {
                btnShareQrCode.setEnabled(false);
            }
        }
    }

    @Override
    public void onValidationFailed(ValidationField field) {
        if (field.getView() == spAsset){
            tvAssetError.setText(field.getMessage());
        } else if (field.getView() == etAmount){
            tvAmountError.setText(field.getMessage());
        }

        ivQrCode.setImageResource(android.R.color.transparent);
    }

    public void createQrCode(){
        Double amount = 0.0;
        try{
            amount = Double.valueOf(this.etAmount.getText().toString());
        } catch(NumberFormatException e){
            Log.e("ReceiveFragment","Amount casting error.");
        }


        this.invoiceItems.clear();
        this.invoiceItems.add(
            new LineItem("transfer", 1, amount)
        );

        LineItem items[] = new LineItem[this.invoiceItems.size()];
        items = this.invoiceItems.toArray(items);
        this.invoice.setLineItems(items);
        this.invoice.setTo(this.grapheneAccount.getName());
        this.invoice.setCurrency(this.cryptoCurrency.getName());

        try {
            Bitmap bitmap = textToImageEncode(Invoice.toQrCode(invoice));
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("ReceiveFragment", "Error creating QrCode");
        }
    }

    Bitmap textToImageEncode(String Value) throws WriterException {
        //TODO: do this in another thread

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    ivQrCode.getWidth(), ivQrCode.getHeight(), null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, ivQrCode.getWidth(), 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
