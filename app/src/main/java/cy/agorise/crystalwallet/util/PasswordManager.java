package cy.agorise.crystalwallet.util;

/**
 * Created by Henry Varona on 29/1/2018.
 */

public class PasswordManager {

    //TODO implement password checking using the encryption implemented in encriptPassword
    public static boolean checkPassword(String encriptedPassword, String passwordToCheck){
        if (encriptedPassword.equals(passwordToCheck)){
            return true;
        } else {
            return false;
        }
    }

    //TODO implement password encryption
    public static String encriptPassword(String password){
        return password;
    }
}
