package cy.agorise.crystalwallet.dialogs.material

import android.app.Activity
import android.widget.Toast


/*
* This class is an implementation of toast
* */
class ToastIt {

    /*
    * Satitic methods
    * */
    companion object {

        /*
        * Show long toast
        * */
        @JvmStatic fun showLongToast(activity: Activity, message:String) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }

        /*
        * Show short toast
        * */
        @JvmStatic fun showShortToast(activity: Activity, message:String) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
}