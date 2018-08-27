package cy.agorise.crystalwallet.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.loading_activity.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import butterknife.BindView
import cy.agorise.crystalwallet.R


/*
* This activity is to show a loading window when it is needed
* */
class LoadingActivity : CustomActivity() {

    @BindView(R.id.imageviewLoading)
    lateinit var imageviewLoading:ImageView




    override fun onCreate(savedInstanceState: Bundle?) {

        /*
        * Construct the parent
        * */
        super.onCreate(savedInstanceState)

        /*
        * If the window was closed before be created so finish it
        * */
        if(destroyWindow!!){
            finish()
        }

        /*
        * Assign the view to this controller
        * */
        setContentView(cy.agorise.crystalwallet.R.layout.loading_activity)

        /*
        * Save the current activity
        * */
        currentActivity = this

        /*
        * If has to manage timer
        * */
        if(LoadingActivity.seconds != -1){

            /*
            * */
            Handler().postDelayed({

                /*
                * Reset flag
                * */
                LoadingActivity.seconds = -1

                finish() //Finish the current window
            }, (LoadingActivity.seconds * 1000).toLong())
        }

        /*
        * Initialice butterknife MVC
        * */
        ButterKnife.bind(this)

        /*
        * If has to change the loading sizes so
        * */
        if(loadinIconChangeSize){
            imageviewLoading.layoutParams.width = loadingIconWidth
            imageviewLoading.layoutParams.height = loadingIconHeigt
        }

        /*
        * Rotate the image
        * */
        val rotation = AnimationUtils.loadAnimation(this, cy.agorise.crystalwallet.R.anim.rotate360)
        imageviewLoading.startAnimation(rotation)

        /*
        * If listener is set deliver response
        * */
        if(onLoadingReady != null){
            onLoadingReady!!.onLoadingReady()
        }
    }

    /*
    * This events hires when the window is destroyed
    * */
    override fun onDestroy() {
        super.onDestroy()

        /*
        * If listener is set deliver response
        * */
        if(onLoadingClosed != null){
            onLoadingClosed?.onLoadingClosed()
        }
    }

    override fun onResume() {
        super.onResume()

        /*
        * If the window was closed before be created so finish it
        * */
        if(destroyWindow!!){
            finish()
        }
    }

    /*
    * Static methods
    * */
    companion object {

        /*
        * Contains the activity shown
        * */
        private var currentActivity: Activity? = null

        /*
        * Flag to validate if the window has to finish or not
        * */
        private var destroyWindow:Boolean? = false

        /*
        * Listener when the loading window is closed
        * */
        private var onLoadingClosed:LoadingClosed? = null

        /*
        * Listener when the loading window is resume
        * */
        private var onLoadingReady:LoadingReady? = null

        /*
        * Contains the seconds to finish the window in case of timer
        * */
        private var seconds:Int = -1

        /*
        * Contains the icon loading size
        * */
        private var loadingIconWidth:Int = -1
        private var loadingIconHeigt:Int = -1
        private var loadinIconChangeSize:Boolean = false




        /*
        * Show the loading activity
        * */
        @JvmStatic
        open fun show(activity: Activity) {

            if(activity!=null){

                /*
                * If it is not visible
                * */
                if(currentActivity==null){

                    /*
                    * Reset flags
                    * */
                    destroyWindow = false

                    /*
                    * Show the loading activity
                    * */
                    val intent = Intent(activity, LoadingActivity::class.java)
                    activity.startActivity(intent)
                }
            }
        }

        /*
        * Dismiss the loading activity
        * */
        @JvmStatic
        open fun dismiss() {

            if(currentActivity!=null){

                /*
                * Close the activity
                * */
                currentActivity?.finish()

                /*
                * Reset flags
                * */
                loadinIconChangeSize = false
                currentActivity = null
                destroyWindow = true
            }
        }

        /*
        * Change the loading icon size
        * */
        @JvmStatic
        open fun loadingIconSize(width:Int,heigth:Int) {

            /*
            * The loading icon size wil change
            * */
            loadinIconChangeSize = true

            /*
            * Save the sizes
            * */
            loadingIconWidth = width
            loadingIconHeigt = heigth
        }

        /*
        * When the loading window is closed
        * */
        @JvmStatic
        open fun onLoadingClosed(onLoadingClose: LoadingClosed) {
            this.onLoadingClosed = onLoadingClose
        }

        /*
        * When the loading window is up and visible
        * */
        @JvmStatic
        open fun onLoadingReady(onLoadingResume:LoadingReady) {
            this.onLoadingReady = onLoadingResume
        }


        /*
        * Timer to close the window
        * */
        @JvmStatic
        open fun closeOnTime(seconds:Int) {
            LoadingActivity.seconds = seconds
        }
    }


    /*
    * Interface for all the events
    * */
    interface LoadingClosed{
        fun onLoadingClosed()
    }
    interface LoadingReady{
        fun onLoadingReady()
    }
}