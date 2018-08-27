package cy.agorise.crystalwallet.util

import cy.agorise.crystalwallet.viewmodels.validators.customImpl.interfaces.UIValidator
import java.util.ArrayList

class FieldsValidator {

    /*
    * Contains the fields to validate
    * */
    private var fields: MutableList<UIValidator> = ArrayList()

    /*
    * Setters and getters
    * */
    fun setFields(fields: List<UIValidator>) {
        this.fields = fields as MutableList<UIValidator>
    }
    /*
     * Endo of setters and getters
     * */

    /*
    * Validate all the fields
    * */
    fun validate() {
        for (uiValidator in fields) {
            uiValidator.validate()
        }
    }

    /*
    * Add component to the list
    * */
    fun add(uiValidator: UIValidator) {
        this.fields.add(uiValidator)
    }
}