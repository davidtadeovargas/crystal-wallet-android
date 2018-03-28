package cy.agorise.crystalwallet.requestmanagers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 26/3/2018.
 */

public class FileServiceRequests {

    private List<FileServiceRequest> requests;
    private List<FileServiceRequestsListener> listeners;
    private static FileServiceRequests instance;


    /**
     * Private constructor for singleton pattern
     */
    private FileServiceRequests() {
    }

    /**
     * Gets an instance of this manager
     * @return the instance to manage the cryptonetinforequest
     */
    public static FileServiceRequests getInstance(){
        if (FileServiceRequests.instance == null){
            FileServiceRequests.instance = new FileServiceRequests();
            FileServiceRequests.instance.requests = new ArrayList<>();
            FileServiceRequests.instance.listeners = new ArrayList<>();
        }

        return FileServiceRequests.instance;
    }

    public void addRequest(FileServiceRequest request){
        this.requests.add(request);

        this._fireNewRequestEvent(request);
    }

    public void removeRequest(FileServiceRequest request){
        this.requests.remove(request);
    }

    public void addListener(FileServiceRequestsListener listener){
        this.listeners.add(listener);
    }

    private void _fireNewRequestEvent(FileServiceRequest request){
        for (int i=0;i<this.listeners.size();i++){
            this.listeners.get(i).onNewRequest(request);
        }
    }
}
