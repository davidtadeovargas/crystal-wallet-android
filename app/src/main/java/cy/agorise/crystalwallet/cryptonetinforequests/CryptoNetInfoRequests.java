package cy.agorise.crystalwallet.cryptonetinforequests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry Varona on 1/10/2017.
 */

public class CryptoNetInfoRequests {
    private List<CryptoNetInfoRequest> requests;
    private List<CryptoNetInfoRequestsListener> listeners;
    private static CryptoNetInfoRequests instance;

    private void CryptoNetInfoRequests(){
        //Private constructor for singleton pattern
    }

    public static CryptoNetInfoRequests getInstance(){
        if (CryptoNetInfoRequests.instance == null){
            CryptoNetInfoRequests.instance = new CryptoNetInfoRequests();
            CryptoNetInfoRequests.instance.requests = new ArrayList<CryptoNetInfoRequest>();
            CryptoNetInfoRequests.instance.listeners = new ArrayList<CryptoNetInfoRequestsListener>();
        }

        return CryptoNetInfoRequests.instance;
    }

    public void addRequest(CryptoNetInfoRequest request){
        this.requests.add(request);

        this._fireNewRequestEvent(request);
    }

    public void removeRequest(CryptoNetInfoRequest request){
        this.requests.remove(request);
    }

    public void addListener(CryptoNetInfoRequestsListener listener){
        this.listeners.add(listener);
    }

    private void _fireNewRequestEvent(CryptoNetInfoRequest request){
        for (int i=0;i<this.listeners.size();i++){
            this.listeners.get(i).onNewRequest(request);
        }
    }
}
