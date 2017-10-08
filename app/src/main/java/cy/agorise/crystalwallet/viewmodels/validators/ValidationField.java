package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;
import android.view.View;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public abstract class ValidationField {

    protected String lastValue;
    protected String message;
    protected boolean validating;
    protected Boolean valid;
    protected UIValidator validator;
    protected View view;

    public ValidationField(View view){
        this.lastValue = "";
        this.message = "";
        this.validating = false;
        this.valid = null;
        this.view = view;
    }

    public void setValidator(UIValidator validator){
        this.validator = validator;
    }

    public void startValidating(){
        this.valid = null;
        this.validating = true;
    }

    abstract public void validate();

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
        return (this.valid != null?this.valid:false);
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        if (!this.lastValue.equals(lastValue)) {
            this.valid = null;
            this.validating = false;
            this.lastValue = lastValue;
        }
    }

    public void setView(View view){
        this.view = view;
    }

    public View getView(){
        return this.view;
    }
}
