package cy.agorise.crystalwallet.network;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by henry on 8/10/2017.
 */

public class WebSocketThread extends Thread {

    // The tag of this class for the log
    private final String TAG = this.getClass().getName();

    //This is to manage the differents threads of this app
    private static HashMap<Long,WebSocketThread> currentThreads = new HashMap<>();
    // The connection tiemout
    private static int connectionTimeout = 5000;

    // The websocket to be used
    private WebSocket mWebSocket;
    // The socketListener for the websocket to reponse
    private WebSocketListener mWebSocketListener;
    // The url to connect
    private String mUrl;
    // If the parameters of this class can be change
    private boolean canChange = true;


    /**
     * Basic constructor,
     *
     * TODO make it throw exception is problem creating the socket
     *
     * @param webSocketListener The socket listener for the wbesocket to response
     * @param url The url to connect
     */
    public WebSocketThread(WebSocketListener webSocketListener, String url) {
        try {
            WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
            this.mUrl = url;
            this.mWebSocketListener = webSocketListener;
            this.mWebSocket = factory.createSocket(this.mUrl);
            this.mWebSocket.addListener(this.mWebSocketListener);
        } catch (IOException e) {
            Log.e(TAG, "IOException. Msg: "+e.getMessage());
        } catch(NullPointerException e){
            Log.e(TAG, "NullPointerException at WebsocketWorkerThreas. Msg: "+e.getMessage());
        }
    }

    /**
     * Gets the current url where the websocket will connect
     * @return the full url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Sets the url of the websocket to connects, it would not change if this thread was already started
     * @param url The full url with the protocol and the ports
     */
    public void setUrl(String url) {
        if(canChange) {
            try {
                WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
                this.mUrl = url;
                mWebSocket = factory.createSocket(mUrl);
                mWebSocket.addListener(this.mWebSocketListener);
            } catch (IOException e) {
                Log.e(TAG, "IOException. Msg: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointerException at WebsocketWorkerThreas. Msg: " + e.getMessage());
            }
        }
    }

    /**
     * Return the class listening for the websocket response
     */
    public WebSocketListener getWebSocketListener() {
        return mWebSocketListener;
    }

    /**
     * Sets the class listenening the websocket response, it will not change if this thread was already started
     */
    public void setWebSocketListener(WebSocketListener webSocketListener) {
        if(canChange) {
            try {
                WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
                this.mWebSocketListener = webSocketListener;
                mWebSocket = factory.createSocket(mUrl);
                mWebSocket.addListener(this.mWebSocketListener);
            } catch (IOException e) {
                Log.e(TAG, "IOException. Msg: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointerException at WebsocketWorkerThreas. Msg: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        canChange = false;
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            WebSocketThread.currentThreads.put(this.getId(),this);
            mWebSocket.connect();
        } catch (WebSocketException e) {
            Log.e(TAG, "WebSocketException. Msg: "+e.getMessage());
        } catch(NullPointerException e){
            Log.e(TAG, "NullPointerException. Msg: "+e.getMessage());
        }
        WebSocketThread.currentThreads.remove(this.getId());
    }

    public boolean isConnected(){
        return mWebSocket.isOpen();
    }
}
