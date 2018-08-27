package cy.agorise.crystalwallet.dialogs.material

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import cy.agorise.crystalwallet.R

/*
*
* Controls the custom implementarion for all kind of material dialogs
* Reference in: https://github.com/afollestad/material-dialogs
*
* */
open abstract class DialogMaterial{

    protected var builder: MaterialDialog.Builder //Contains the builder
    protected lateinit var materialDialog: MaterialDialog //Contains the controller for the dialog

    /*
    * Contains the activity
    * */
    protected var activity:Activity;


    /*
    * Contains the response for positive button click
    * */
    var positiveResponse:PositiveResponse? = null

    /*
    * Contains the response for negative button click
    * */
    var negativeResponse:NegativeResponse? = null




    constructor(activity: Activity) {

        /*
        * Save the activity
        * */
        this.activity = activity

        /*
        *   Init the builder
        * */
        builder = MaterialDialog.Builder(activity)
    }


    /*
    * Show the dialog
    * */
    fun show() {

        /*
        * If user wants positive and negative
        * */
        if(positiveResponse != null && negativeResponse != null){

            /*
            * Add positve
            * */
            builder.positiveText(activity.resources.getString(R.string.ok))
            builder.onPositive { dialog, which ->

                /*
                * If response is not null deliver response
                * */
                if(positiveResponse != null){
                    positiveResponse!!.onPositive()
                }
            }

            /*
            * Add negative
            * */
            builder.negativeText(activity.resources.getString(R.string.cancel))
            builder.onNegative { dialog, which ->

                /*
                * If response is not null deliver response
                * */
                if(negativeResponse != null){
                    negativeResponse!!.onNegative()
                }
            }
        }

        /*
        * If user wants positive button
        * */
        if(positiveResponse != null){
            builder.positiveText(activity.resources.getString(R.string.ok))
            builder.onPositive { dialog, which ->

                /*
                * If response is not null deliver response
                * */
                if(positiveResponse != null){
                    positiveResponse!!.onPositive()
                }
            }
        }

        /*
        * Build internal material dialog, this lets to show it
        * */
        this.build()

        /*
        * Show the dialog
        * */
        materialDialog.show()
    }

    /*
    * Close the dialog
    * */
    fun dismiss() {
        this.materialDialog.dismiss()
    }

    /*
    * After the class is completed as needed, we need to call this method to join all together after show the
    * childs implementations
    * */
    open fun build() {
        this.materialDialog = this.builder.build()
    }


    /*
    * Set indeterminate progress
    *
    * */
    open fun progress(){
        this.builder.progress(true, 0)
    }

    /*
    * Setters and getters
    * */
    fun setText(message: String) {
        this.builder.content(message)
    }

    fun setTitle(title: String) {
        this.builder.title(title)
    }
    /*
     * End of setters and getters
     * */
}