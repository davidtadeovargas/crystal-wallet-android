package cy.agorise.crystalwallet.apigenerator;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cy.agorise.crystalwallet.network.WebSocketThread;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetAccounts;
import cy.agorise.graphenej.api.GetKeyReferences;
import cy.agorise.graphenej.api.GetRelativeAccountHistory;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.WitnessResponse;


/**
 * This class manage all the api request directly to the Graphene Servers
 * Created by henry on 26/9/2017.
 */

public class GrapheneApiGenerator {

    //TODO network connections
    //TODO make to work with all Graphene stype, not only bitshares
    private static int connectionTimeout = 5000;
    private static String url = "http://128.0.69.157:8090";

    /**
     * Retrieves the data of an account searching by it's id
     *
     * @param accountId The accountId to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountById(String accountId, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccounts(accountId, new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                if (response.result.getClass() == ArrayList.class) {
                    List list = (List) response.result;
                    if (list.size() > 0) {
                        if (list.get(0).getClass() == AccountProperties.class) {
                            request.getListener().success(list.get(0),request.getId());
                            return;
                            //TODO answer a crystal model
                        }
                    }
                }
                request.getListener().fail(request.getId());
            }

            @Override
            public void onError(BaseResponse.Error error) {
                request.getListener().fail(request.getId());
            }
        }),url);
        thread.start();
    }

    /**
     * Gets the account ID from an owner or active key
     *
     * @param address The address to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountByOwnerOrActiveAddress(Address address, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetKeyReferences(address, true, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    final List<List<UserAccount>> resp = (List<List<UserAccount>>) response.result;
                    if(resp.size() > 0){
                        List<UserAccount> accounts = resp.get(0);
                        if(accounts.size() > 0){
                            for(UserAccount account : accounts) {
                                request.getListener().success(account,request.getId());}}}
                    request.getListener().fail(request.getId());
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);

            thread.start();
    }

    /**
     * Gets the Transaction for an Account
     *
     * @param accountId The account id to search
     * @param start The start index of the transaction list
     * @param stop The stop index of the transaction list
     * @param limit the maximun transactions to retrieve
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountTransaction(String accountId, int start, int stop, int limit, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetRelativeAccountHistory(new UserAccount(accountId), stop, limit, start, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    WitnessResponse<List<HistoricalTransfer>> resp = response;
                    request.getListener().success(resp,request.getId());
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);
            thread.start();
    }

    /**
     * Retrieves the account id by the name of the account
     *
     * @param accountName The account Name to find
     * @param request The Api request object, to answer this petition
     */
    public static void getAccountIdByName(String accountName, final ApiRequest request){
        WebSocketThread thread = new WebSocketThread(new GetAccountByName(accountName, new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    AccountProperties accountProperties = ((WitnessResponse<AccountProperties>) response).result;
                    if(accountProperties == null){
                        request.getListener().success(null,request.getId());
                    }else{
                        request.getListener().success(accountProperties.id,request.getId());
                    }
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    request.getListener().fail(request.getId());
                }
            }),url);
        thread.start();
    }
}
