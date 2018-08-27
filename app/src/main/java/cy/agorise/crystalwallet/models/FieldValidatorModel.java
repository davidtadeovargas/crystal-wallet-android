package cy.agorise.crystalwallet.models;

import android.view.View;

public class FieldValidatorModel {

    /*
    * Determine if the field is valid
    * */
    private boolean valid;

    /*
    * Contains the message of the error
    * */
    private String message;



    /*
    * Setters and getters
    * */
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    /*
     * End of setters and getters
     * */


    /*
    * Set tha the field is invalid
    * */
    final public void setInvalid(){
        this.valid = false;
    }


    /*
     * Set tha the field is valid
     * */
    final public void setValid(){
        this.valid = true;
    }
}
