package cy.agorise.crystalwallet.requestmanagers;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be used to communicate events related to crypto net accounts data
 *
 * Created by Henry Varona on 4/28/2018.
 */

public class CryptoNetEvents {
    private List<CryptoNetEventsListener> listeners;
    private static CryptoNetEvents instance;

    /**
     * Private constructor for singleton pattern
     */
    private void CryptoNetEvents(){
    }

    /**
     * Gets an instance of this manager
     * @return the instance to manage the cryptonetinforequest
     */
    public static CryptoNetEvents getInstance(){
        if (CryptoNetEvents.instance == null){
            CryptoNetEvents.instance = new CryptoNetEvents();
            CryptoNetEvents.instance.listeners = new ArrayList<>();
        }

        return CryptoNetEvents.instance;
    }

    public void fireEvent(CryptoNetEvent event){
        for (int i=0;i<this.listeners.size();i++){
            this.listeners.get(i).onCryptoNetEvent(event);
        }
    }

    public void removeListener(CryptoNetEventsListener listener){
        this.listeners.remove(listener);
    }

    public void addListener(CryptoNetEventsListener listener){
        this.listeners.add(listener);
    }
}
