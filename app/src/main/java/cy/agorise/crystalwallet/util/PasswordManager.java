package cy.agorise.crystalwallet.util;

import android.util.Log;

import java.math.BigInteger;

import cy.agorise.crystalwallet.util.yubikey.TOTP;

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

    public static String totpd(String sharedSecret, long unixtime, byte[] salt){
        char[] ch = sharedSecret.toCharArray();

        StringBuilder builder = new StringBuilder();
        for (char c : ch) {
            String hexCode=String.format("%H", c);
            builder.append(hexCode);
        }
        String secretHex = String.format("%040x", new BigInteger(1, sharedSecret.getBytes()));

        long time = unixtime/30L;

        String steps = Long.toHexString(time).toUpperCase();



        /*for (int i=0;i<64;i++){
            steps = Long.toHexString(time+i).toUpperCase();
            Log.i("TOTPTEST", TOTP.generateTOTP(
                    secretHex,
                    steps,
                    "6", "HmacSHA1", salt));
            secretHex = "00"+secretHex;
        }*/

        return TOTP.generateTOTP(
                secretHex,
                steps,
                "6", "HmacSHA1", salt);
    }
}
