package cy.agorise.crystalwallet.apigenerator.insightapi;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cy.agorise.crystalwallet.apigenerator.insightapi.models.AddressTxi;
import cy.agorise.crystalwallet.apigenerator.insightapi.models.Txi;
import cy.agorise.crystalwallet.apigenerator.insightapi.models.Vin;
import cy.agorise.crystalwallet.apigenerator.insightapi.models.Vout;
import cy.agorise.crystalwallet.models.GTxIO;
import cy.agorise.crystalwallet.models.GeneralCoinAccount;
import cy.agorise.crystalwallet.models.GeneralCoinAddress;
import cy.agorise.crystalwallet.models.GeneralTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Get all the transaction data of the addresses of an account
 *
 */

public class GetTransactionByAddress extends Thread implements Callback<AddressTxi> {
    /**
     * The account to be query
     */
    private GeneralCoinAccount mAccount;
    /**
     * The list of address to query
     */
    private List<GeneralCoinAddress> mAddresses = new ArrayList<>();
    /**
     * The serviceGenerator to call
     */
    private InsightApiServiceGenerator mServiceGenerator;
    /**
     * This app context, used to save on the DB
     */
    private Context mContext;


    /**
     * Basic consturcotr
     * @param account The account to be query
     * @param context This app context
     */
    public GetTransactionByAddress(GeneralCoinAccount account, Context context) {
        String serverUrl = InsightApiConstants.sProtocol + "://" + InsightApiConstants.getAddress(account.getCryptoCoin()) +"/";
        this.mAccount = account;
        this.mServiceGenerator = new InsightApiServiceGenerator(serverUrl);
        this.mContext = context;
    }

    /**
     * add an address to be query
     * @param address the address to be query
     */
    public void addAddress(GeneralCoinAddress address) {
        this.mAddresses.add(address);
    }


    /**
     * Handle the response
     * @param call The call with the addresTxi object
     * @param response the response status object
     */
    @Override
    public void onResponse(Call<AddressTxi> call, Response<AddressTxi> response) {
        if (response.isSuccessful()) {
            boolean changed = false;
            AddressTxi addressTxi = response.body();

            for (Txi txi : addressTxi.items) {
                GeneralCoinAccount tempAccount = null;
                GeneralTransaction transaction = new GeneralTransaction();
                transaction.setAccount(this.mAccount);
                transaction.setTxid(txi.txid);
                transaction.setBlock(txi.blockheight);
                transaction.setDate(new Date(txi.time * 1000));
                transaction.setFee((long) (txi.fee * Math.pow(10,this.mAccount.getCryptoCoin().getPrecision())));
                transaction.setConfirm(txi.confirmations);
                transaction.setType(this.mAccount.getCryptoCoin());
                transaction.setBlockHeight(txi.blockheight);

                for (Vin vin : txi.vin) {
                    GTxIO input = new GTxIO();
                    input.setAmount((long) (vin.value * Math.pow(10,this.mAccount.getCryptoCoin().getPrecision())));
                    input.setTransaction(transaction);
                    input.setOut(true);
                    input.setType(this.mAccount.getCryptoCoin());
                    String addr = vin.addr;
                    input.setAddressString(addr);
                    input.setIndex(vin.n);
                    input.setScriptHex(vin.scriptSig.hex);
                    input.setOriginalTxid(vin.txid);
                    for (GeneralCoinAddress address : this.mAddresses) {
                        if (address.getAddressString(this.mAccount.getNetworkParam()).equals(addr)) {
                            input.setAddress(address);
                            tempAccount = address.getAccount();

                            if (!address.hasTransactionOutput(input, this.mAccount.getNetworkParam())) {
                                address.getTransactionOutput().add(input);
                            }
                            changed = true;
                        }
                    }
                    transaction.getTxInputs().add(input);
                }

                for (Vout vout : txi.vout) {
                    if(vout.scriptPubKey.addresses == null || vout.scriptPubKey.addresses.length <= 0){
                        // The address is null, this must be a memo
                        String hex = vout.scriptPubKey.hex;
                        int opReturnIndex = hex.indexOf("6a");
                        if(opReturnIndex >= 0) {
                            byte[] memoBytes = new byte[Integer.parseInt(hex.substring(opReturnIndex+2,opReturnIndex+4),16)];
                            for(int i = 0; i < memoBytes.length;i++){
                                memoBytes[i] = Byte.parseByte(hex.substring(opReturnIndex+4+(i*2),opReturnIndex+6+(i*2)),16);
                            }
                            transaction.setMemo(new String(memoBytes));
                        }
                    }else {
                        GTxIO output = new GTxIO();
                        output.setAmount((long) (vout.value * Math.pow(10, this.mAccount.getCryptoCoin().getPrecision())));
                        output.setTransaction(transaction);
                        output.setOut(false);
                        output.setType(this.mAccount.getCryptoCoin());
                        String addr = vout.scriptPubKey.addresses[0];
                        output.setAddressString(addr);
                        output.setIndex(vout.n);
                        output.setScriptHex(vout.scriptPubKey.hex);
                        for (GeneralCoinAddress address : this.mAddresses) {
                            if (address.getAddressString(this.mAccount.getNetworkParam()).equals(addr)) {
                                output.setAddress(address);
                                tempAccount = address.getAccount();

                                if (!address.hasTransactionInput(output, this.mAccount.getNetworkParam())) {
                                    address.getTransactionInput().add(output);
                                }
                                changed = true;
                            }
                        }

                        transaction.getTxOutputs().add(output);
                    }
                }
                if(txi.txlock && txi.confirmations< this.mAccount.getCryptoNet().getConfirmationsNeeded()){
                    transaction.setConfirm(this.mAccount.getCryptoNet().getConfirmationsNeeded());
                }
                //TODO database
                /*SCWallDatabase db = new SCWallDatabase(this.mContext);
                long idTransaction = db.getGeneralTransactionId(transaction);
                if (idTransaction == -1) {
                    db.putGeneralTransaction(transaction);
                } else {
                    transaction.setId(idTransaction);
                    db.updateGeneralTransaction(transaction);
                }*/

                if (tempAccount != null && transaction.getConfirm() < this.mAccount.getCryptoNet().getConfirmationsNeeded()) {
                    new GetTransactionData(transaction.getTxid(), tempAccount, this.mContext, true).start();
                }
                for (GeneralCoinAddress address : this.mAddresses) {
                    if (address.updateTransaction(transaction)) {
                        break;
                    }
                }
            }

            if(changed) {
                this.mAccount.balanceChange();
            }
        }
    }

    /**
     * Failure of the call
     * @param call The call object
     * @param t The reason for the failure
     */
    @Override
    public void onFailure(Call<AddressTxi> call, Throwable t) {
        Log.e("GetTransactionByAddress", "Error in json format");
    }

    /**
     * Function to start the insight api call
     */
    @Override
    public void run() {
        if (this.mAddresses.size() > 0) {
            StringBuilder addressToQuery = new StringBuilder();
            for (GeneralCoinAddress address : this.mAddresses) {
                addressToQuery.append(address.getAddressString(this.mAccount.getNetworkParam())).append(",");
            }
            addressToQuery.deleteCharAt(addressToQuery.length() - 1);
            InsightApiService service = this.mServiceGenerator.getService(InsightApiService.class);
            Call<AddressTxi> addressTxiCall = service.getTransactionByAddress(InsightApiConstants.getPath(this.mAccount.getCryptoCoin()),addressToQuery.toString());
            addressTxiCall.enqueue(this);
        }
    }
}
