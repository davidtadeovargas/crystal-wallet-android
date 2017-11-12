package cy.agorise.crystalwallet.cryptonetinforequests;

/**
 * Listener extends for the manager classes
 * Created by Henry Varona on 1/10/2017.
 */

public interface CryptoNetInfoRequestsListener {
    /**
     * A new request for the manager
     * @param request The request, we can query of the class of this object to know if the request is from a particular manager
     */
    public void onNewRequest(CryptoNetInfoRequest request);
}
