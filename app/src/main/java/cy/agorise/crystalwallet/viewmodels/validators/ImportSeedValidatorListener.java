package cy.agorise.crystalwallet.viewmodels.validators;

/**
 * Created by Henry Varona on 2/10/2017.
 */

public interface ImportSeedValidatorListener {

    public void onValidationSucceeded();
    public void onValidationFailed(String error);
}
