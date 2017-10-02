package cy.agorise.crystalwallet.cryptonetinforequests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry Varona on 1/10/2017.
 */

public class CryptoNetInfoRequests {
    private List<CryptoNetInfoRequest> requests;
    private List<CryptoNetInfoRequestsListener> listeners;
    private CryptoNetInfoRequests instance;

    private void CryptoNetInfoRequests(){
        //Private constructor for singleton pattern
    }

    public CryptoNetInfoRequests getInstance(){
        if (this.instance == null){
            this.instance = new CryptoNetInfoRequests();
            this.requests = new ArrayList<CryptoNetInfoRequest>();
            this.listeners = new ArrayList<CryptoNetInfoRequestsListener>();
        }

        return this.instance;
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
