package cy.agorise.crystalwallet.tests.activities.dialogs

import android.os.Bundle
import android.widget.Toast
import cy.agorise.crystalwallet.activities.CustomActivity
import cy.agorise.crystalwallet.activities.LoadingActivity


/*
* Class to test LoadingActivity
* */
class Test_LoadingActivity : CustomActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        show()
        //withTimer()
        //onLoadingClose()
        //onLoadingReady()
        //sizeInLoadingIcon()
    }


    /*
    * Show the simple loading graphic
    * */
    fun show(){
        LoadingActivity.show(globalActivity)
        //LoadingActivity.dismiss() //For testing porpouse
    }


    /*
    * Loading with timer
    * */
    fun withTimer(){
        LoadingActivity.closeOnTime(3)
        LoadingActivity.show(globalActivity)
    }


    /*
    * Listener when the loading window is closed
    * */
    fun onLoadingClose(){
        LoadingActivity.closeOnTime(3)
        LoadingActivity.onLoadingClosed(object : LoadingActivity.LoadingClosed{
            override fun onLoadingClosed() {
                Toast.makeText(globalActivity, "onLoadingClosed event fired", Toast.LENGTH_LONG).show()
            }

        })
        LoadingActivity.show(globalActivity)
    }


    /*
    * Listener when the loading window is resume
    * */
    fun onLoadingReady(){
        LoadingActivity.onLoadingReady(object : LoadingActivity.LoadingReady{
            override fun onLoadingReady() {
                Toast.makeText(globalActivity, "onLoadingReady event fired", Toast.LENGTH_LONG).show()
            }

        })
        LoadingActivity.show(globalActivity)
    }

    /*
    * Set specified size to the loading icon
    * */
    fun sizeInLoadingIcon(){
        LoadingActivity.loadingIconSize(30,30)
        LoadingActivity.show(globalActivity)
    }
}