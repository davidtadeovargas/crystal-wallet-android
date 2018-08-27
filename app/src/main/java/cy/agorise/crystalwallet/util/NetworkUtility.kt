package cy.agorise.crystalwallet.util

import android.app.Activity
import android.widget.Toast
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator
import cy.agorise.crystalwallet.application.CrystalApplication
import cy.agorise.crystalwallet.enums.CryptoNet
import cy.agorise.crystalwallet.network.CryptoNetManager

/*
*
* Static methods for network utility
* */
class NetworkUtility {

    /*
    * Satitic methods
    * */
    companion object {

        /*
        * Test connection with server
        * */
        @JvmStatic fun testServerConnnection(activity:Activity){

            val onErrorWebSocker = GrapheneApiGenerator.OnErrorWebSocket {
                /*
                    * Show message to client
                    * */
                activity.runOnUiThread(Runnable { Toast.makeText(activity, activity.getResources().getString(R.string.network_err_no_server_connection), Toast.LENGTH_LONG).show() })
            }
            CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES, CrystalApplication.BITSHARES_TESTNET_URL, activity, onErrorWebSocker, false)

        }

        /*
        * Test connection with server and custom implementation callback
        * */
        @JvmStatic fun testServerConnnection(activity:Activity, onResponseWebSocket: GrapheneApiGenerator.OnResponsesWebSocket){
            CryptoNetManager.addCryptoNetURL(CryptoNet.BITSHARES, CrystalApplication.BITSHARES_TESTNET_URL, activity, onResponseWebSocket, false)
        }

        /*
        * Test connection with server and custom implementation callback and with normal error
        * */
        @JvmStatic fun testServerConnnectionNormalError(activity:Activity, onResponseWebSocket: GrapheneApiGenerator.OnResponsesWebSocket){

            /*
            *
            * Listener to catch the error and show the normal user error message
            *
            * */
            val onErrorWebSocker = GrapheneApiGenerator.OnErrorWebSocket {
                /*
                    * Show message to client
                    * */
                activity.runOnUiThread(Runnable { Toast.makeText(activity, activity.getResources().getString(R.string.network_err_no_server_connection), Toast.LENGTH_LONG).show() })
            }

            /*
            * Request
            * */
            CryptoNetManager.addCryptoNetURL(   CryptoNet.BITSHARES,
                                                CrystalApplication.BITSHARES_TESTNET_URL,
                                                activity,
                                                onErrorWebSocker,
                                                onResponseWebSocket,
                                                false)
        }
    }
}