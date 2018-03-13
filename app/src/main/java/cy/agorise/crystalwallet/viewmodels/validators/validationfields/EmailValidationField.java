package cy.agorise.crystalwallet.viewmodels.validators.validationfields;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cy.agorise.crystalwallet.R;

/**
 * Created by Henry Varona on 2/21/2017.
 */

public class EmailValidationField extends ValidationField {

    private EditText emailField;

    public EmailValidationField(EditText emailField){
        super(emailField);
        this.emailField = emailField;
    }

    public void validate(){
        String newValue = emailField.getText().toString();
        if (!newValue.equals("")) {

            if (!newValue.equals(this.getLastValue())) {
                this.setLastValue(newValue);
                this.startValidating();

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(newValue);

                if (!matcher.matches()) {
                    this.setMessageForValue(newValue, "The email is not valid");
                    this.setValidForValue(newValue, false);
                } else {
                    this.setValidForValue(newValue, true);
                }
            }
        } else {
            this.setLastValue("");
            this.startValidating();
            this.setMessageForValue("", "");
            this.setValidForValue("", false);
        }
    }
}
