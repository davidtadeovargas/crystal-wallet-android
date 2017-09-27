package cy.agorise.crystalwallet.dao.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

import cy.agorise.crystalwallet.enums.CryptoCoin;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.CryptoNetAccount;

import static cy.agorise.crystalwallet.R.string.account;

/**
 * Created by Henry Varona on 13/9/2017.
 */

public class Converters {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public long cryptoNetAccountToId(CryptoNetAccount account) {
        if (account == null) {
            return -1;
        } else {
            return account.getId();
        }
    }

    @TypeConverter
    public CryptoNetAccount fromCryptoNetAccountId(long value) {
        if (value == -1){
            return null;
        } else {
            CryptoNetAccount account = new CryptoNetAccount();
            account.setId(value);
            return account;
        }
    }

    @TypeConverter
    public String cryptoCoinToName(CryptoCoin coin) {
        if (coin == null) {
            return "";
        } else {
            return coin.name();
        }
    }

    @TypeConverter
    public CryptoCoin nameToCryptoCoin(String value) {
        if (value.equals("")){
            return null;
        } else {
            return CryptoCoin.valueOf(value);
        }
    }

    @TypeConverter
    public String cryptoNetToName(CryptoNet net){
        if (net == null) {
            return "";
        } else {
            return net.getLabel();
        }
    }

    @TypeConverter
    public CryptoNet nameToCryptoNet(String value) {
        if (value.equals("")){
            return null;
        } else {
            return CryptoNet.valueOf(value);
        }
    }
}
