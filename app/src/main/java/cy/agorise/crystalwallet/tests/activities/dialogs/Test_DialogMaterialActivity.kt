package cy.agorise.crystalwallet.tests.activities.dialogs

import android.os.Bundle
import cy.agorise.crystalwallet.R
import cy.agorise.crystalwallet.activities.CustomActivity
import cy.agorise.crystalwallet.dialogs.material.DialogMaterial


/*
* Class to test DialogMaterial.kt
* */
class Test_DialogMaterialActivity : CustomActivity() {

    /*
    * Object to be tested
    * */
    lateinit var dialogMaterial: DialogMaterial;




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty_activity)
    }


    /*
    * This class can not be instantiate,
    * this methos is commented to prevent compilation error, if uncommented it should
    * throw error compilations and based on this this assertion is complete
    *
    * */
    fun instiantiation(){
        //dialogMaterial = DialogMaterial(this)
    }
}