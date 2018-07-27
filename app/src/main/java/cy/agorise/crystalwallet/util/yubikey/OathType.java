package cy.agorise.crystalwallet.util.yubikey;

public enum OathType {
    HOTP((byte)0x10),
    TOTP((byte)0x20);

    protected Byte byteVal;

    OathType(Byte byteVal){
        this.byteVal = byteVal;
    }

    public Byte getByteVal(){
        return this.byteVal;
    }
}
