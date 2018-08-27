package cy.agorise.crystalwallet.tests.activities.dialogs

import android.os.Bundle
import android.widget.Toast
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.activities.CustomActivity
import cy.agorise.crystalwallet.dialogs.material.CrystalDialog
import cy.agorise.crystalwallet.dialogs.material.NegativeResponse
import cy.agorise.crystalwallet.dialogs.material.PositiveResponse

/*
* Class to test CrystalDialog
* */
class Test_CrystalDialogActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty_activity)

        //show()
        //showOKDialog()
        //showOKCancelDialog()
        //showProgressIndeterminate()
    }

    /*
    *
    * Show the simplest dialog
    *
    * */
    fun show(){

        var crytalDialog:CrystalDialog = CrystalDialog(this)
        crytalDialog.setTitle("Title")
        crytalDialog.setText("Text")
        crytalDialog.show()
        //crytalDialog.dismiss()
    }

    /*
    *
    * Show the accept dialog
    *
    * */
    fun showOKDialog(){

        var crytalDialog:CrystalDialog = CrystalDialog(this)
        crytalDialog.setTitle("Title")
        crytalDialog.setText("Text")
        crytalDialog.positiveResponse = object:PositiveResponse{
            override fun onPositive() {
                Toast.makeText(globalActivity, "CrystalDialog Positive clicked", Toast.LENGTH_LONG).show()
            }
        }
        crytalDialog.show()
    }

    /*
    *
    * Show the accept and cancel dialog
    *
    * */
    fun showOKCancelDialog(){

        var crytalDialog:CrystalDialog = CrystalDialog(this)
        crytalDialog.setTitle("Title")
        crytalDialog.setText("Text")
        crytalDialog.positiveResponse = object:PositiveResponse{
            override fun onPositive() {
                Toast.makeText(globalActivity, "CrystalDialog Positive clicked", Toast.LENGTH_LONG).show()
            }
        }
        crytalDialog.negativeResponse = object:NegativeResponse{
            override fun onNegative() {
                Toast.makeText(globalActivity, "CrystalDialog Negative clicked", Toast.LENGTH_LONG).show()
            }
        }
        crytalDialog.show()
    }

    /*
    *
    * Show the indeterminate dialog with text
    *
    * */
    fun showProgressIndeterminate(){

        var crytalDialog:CrystalDialog = CrystalDialog(this)
        crytalDialog.setText("Loading...")
        crytalDialog.progress()
        crytalDialog.show()
    }
}