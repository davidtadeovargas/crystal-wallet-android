package cy.agorise.crystalwallet.tests.activities.dialogs

import android.net.Uri
import android.os.Bundle
import android.view.View
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.activities.CustomActivity
import kotlinx.android.synthetic.main.empty_activity.*


/*
* Class for test VideoExView
* */
class Test_VideoActivity : CustomActivity() {

    /*
    * cy.agorise.crystalwallet.views.natives.VideoViewEx
    * should be used as view to get the new implementations
    * */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty_activity)

        /*
        * For testing purpouses
        * */
        video.visibility = View.VISIBLE

        //play()
        playIndeterminate()
    }


    /*
    * Just one time play
    * */
    fun play(){
        video.setVideoRaw(R.raw.appbar_background)
        video.start()
    }


    /*
    * Just one time play
    * */
    fun playIndeterminate(){
        video.setVideoRaw(R.raw.appbar_background)
        video.playContinius()
    }
}