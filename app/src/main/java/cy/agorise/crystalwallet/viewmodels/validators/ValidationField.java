package cy.agorise.crystalwallet.viewmodels.validators;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public class ValidationField {

    public String name;

    public String lastValue;
    public String message;
    public boolean validating;
    public boolean valid;

    public ValidationField(String name){
        this.name = name;
        this.lastValue = "";
        this.message = "";
        this.validating = false;
        this.valid = false;
    }

    public String getName(){
        return this.name;
    }

    public void startValidating(){
        this.validating = true;
    }

    public void stopValidating(){
        this.validating = false;
    }

    public void setValidForValue(String value, boolean newValue){
        if (this.lastValue.equals(value)) {
            this.validating = false;
            this.valid = newValue;
        }
    }

    public void setMessage(String newValue){
        this.message = newValue;
    }

    public String getMessage(){
        return this.message;
    }

    public boolean getValidating(){
        return this.validating;
    }

    public boolean getValid(){
        return this.valid;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        if (!this.lastValue.equals(lastValue)) {
            this.valid = false;
            this.validating = false;
            this.lastValue = lastValue;
        }
    }
}
