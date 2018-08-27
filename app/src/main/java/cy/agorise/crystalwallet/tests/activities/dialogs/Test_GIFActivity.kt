package cy.agorise.crystalwallet.tests.activities.dialogs

import android.os.Bundle
import android.view.View
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.activities.CustomActivity
import kotlinx.android.synthetic.main.empty_activity.*


/*
* Unit test for class GIFView
* */
class Test_GIFActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty_activity)

        /*
        * For testings purpouses
        * */
        gifView.visibility = View.VISIBLE

        load()
        //loadWithContainer()
    }


    /*
    * Load normaly the gif
    * */
    fun load(){
        gifView.load(R.raw.burbujas)
    }

    /*
    * Load with fit into container
    * */
    fun loadWithContainer(){
        gifView.centerCrop()
        gifView.load(R.raw.burbujas)
    }
}