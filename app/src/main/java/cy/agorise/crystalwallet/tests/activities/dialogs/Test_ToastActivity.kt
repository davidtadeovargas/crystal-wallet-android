package cy.agorise.crystalwallet.tests.activities.dialogs

import android.os.Bundle
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.activities.CustomActivity
import cy.agorise.crystalwallet.dialogs.material.ToastIt

class Test_ToastActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty_activity)

        //showShort()
        showLongShort()
    }


    /*
    * Show simple short toast
    * */
    fun showShort(){
        ToastIt.showShortToast(this,"showShortToast")
    }


    /*
    * Show simple short toast
    * */
    fun showLongShort(){
        ToastIt.showShortToast(this,"showLongShort")
    }
}