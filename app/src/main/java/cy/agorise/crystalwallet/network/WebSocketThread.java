package cy.agorise.crystalwallet.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;
import java.util.HashMap;

import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.dialogs.material.DialogMaterial;

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

    /*
     *
     * Interface to catch only errors in connection with sockets
     * */
    private GrapheneApiGenerator.OnErrorWebSocker onErrorWebSocker;

    /*
     *
     * Interface to catch errors and success responses in connection with sockets
     * */
    private GrapheneApiGenerator.OnResponsesWebSocket onResponsesWebSocket;


    /*
    * To catch websocket errors
    * */
    private Activity activity;

    /*
    * To show normal error message or not
    * */
    private boolean showNormalMessage = true;


    /*
    * Object needed for socket connection
    * */
    private WebSocketFactory factory;

    /**
     * Basic constructor,
     *
     * TODO make it throw exception is problem creating the socket
     *
     * @param webSocketListener The socket listener for the wbesocket to response
     * @param url The url to connect
     */
    public WebSocketThread(WebSocketListener webSocketListener, String url) {

        /*
        * The listener always can be setted
        * */
        this.mWebSocketListener = webSocketListener;

        /*
        *
        * If at this point the url is not defined, this will be set after
        * */
        if(url!=null){
            try {
                factory = new WebSocketFactory().setConnectionTimeout(5000);
                this.mUrl = url;
                this.mWebSocket = factory.createSocket(this.mUrl);
                this.mWebSocket.addListener(this.mWebSocketListener);
            } catch (IOException e) {
                Log.e(TAG, "IOException. Msg: "+e.getMessage());
            } catch(NullPointerException e){
                Log.e(TAG, "NullPointerException at WebsocketWorkerThreas. Msg: "+e.getMessage());
            }
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

            /*
            * If the initialization of the socket comes after
            * */
            if(factory==null){
                factory = new WebSocketFactory().setConnectionTimeout(5000);
                this.mWebSocket = factory.createSocket(this.mUrl);
                this.mWebSocket.addListener(this.mWebSocketListener);
            }

            WebSocketThread.currentThreads.put(this.getId(),this);
            mWebSocket.connect();

            /*
            *
            * Websocket success response
            * */
            if(onResponsesWebSocket!=null){
                onResponsesWebSocket.onSuccess();
            }

        } catch (final Exception e) {
            Log.e(TAG, "WebSocketException. Msg: "+e.getMessage());

            //Deliver error to user
            if(activity!=null){

                /*
                 * Show error to user if aplies
                 * */
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(showNormalMessage){
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                /*Deliver response in the listeners*/
                if(onErrorWebSocker!=null){
                    onErrorWebSocker.onError(e);
                }
                else if(onResponsesWebSocket!=null){
                    onResponsesWebSocket.onError(e);
                }
            }

        }
        WebSocketThread.currentThreads.remove(this.getId());
    }

    public boolean isConnected(){
        return mWebSocket.isOpen();
    }


    public void setOnErrorWebSocker(GrapheneApiGenerator.OnErrorWebSocker onErrorWebSocker) {
        this.onErrorWebSocker = onErrorWebSocker;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setShowNormalMessage(boolean showNormalMessage) {
        this.showNormalMessage = showNormalMessage;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setOnResponsesWebSocket(GrapheneApiGenerator.OnResponsesWebSocket onResponsesWebSocket) {
        this.onResponsesWebSocket = onResponsesWebSocket;
    }
}
