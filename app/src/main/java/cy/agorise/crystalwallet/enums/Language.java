package cy.agorise.crystalwallet.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Henry Varona on 16/3/2018.
 */

public enum Language {
    NOTSET("",""),ENGLISH("en","English"),SPANISH("es","Spanish");

    protected String code;

    protected String label;

    private static Map<String, Language> codeMap = new HashMap<String, Language>();
    static {
        for (Language languageEnum : Language.values()) {
            codeMap.put(languageEnum.code, languageEnum);
        }
    }

    Language(String code, String label){
        this.code = code;
        this.label = label;
    }

    public String getCode(){
        return this.code;
    }

    public String getLabel(){
        return this.label;
    }

    public String toString(){
        return this.getLabel();
    }

    public static Language getByCode(String code){
        if (codeMap.containsKey(code)) {
            return codeMap.get(code);
        } else {
            return Language.NOTSET;
        }
    }
}
