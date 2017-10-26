package cy.agorise.crystalwallet.viewmodels.validators;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public abstract class UIValidator {
    protected Context context;
    protected UIValidatorListener listener;
    protected List<ValidationField> validationFields;

    public UIValidator(Context context){
        this.context = context;
        this.validationFields = new ArrayList<ValidationField>();
    }

    public void addField(ValidationField newField){
        this.validationFields.add(newField);
        newField.setValidator(this);
    }

    public Context getContext(){
        return this.context;
    }

    public void setListener(UIValidatorListener listener){
        this.listener = listener;
    }

    public boolean isValid(){
        for(int i=0;i<this.validationFields.size();i++){
            ValidationField nextField = this.validationFields.get(i);

            if (!nextField.getValid()){
                return false;
            }
        }

        return true;
    }

    public void validate(){
        for (int i=0;i<this.validationFields.size();i++){
            this.validationFields.get(i).validate();
        }
    }

    public int validationFieldsCount(){
        return this.validationFields.size();
    }

    public ValidationField getValidationField(int index){
        return this.validationFields.get(index);
    }

    public void validationFailed(ValidationField field){
        this._fireOnValidationFailedEvent(field);
    }

    public void validationSucceeded(ValidationField field){
        this._fireOnValidationSucceededEvent(field);
    }

    public void _fireOnValidationFailedEvent(ValidationField field){
        this.listener.onValidationFailed(field);
    }

    public void _fireOnValidationSucceededEvent(ValidationField field){
        this.listener.onValidationSucceeded(field);
    }
}
