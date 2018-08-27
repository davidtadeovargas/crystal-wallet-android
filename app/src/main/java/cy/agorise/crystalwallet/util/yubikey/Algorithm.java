package cy.agorise.crystalwallet.util.yubikey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import kotlin.jvm.internal.Intrinsics;

public enum Algorithm {
    SHA1((byte)1, "SHA1", 64),
    SHA256((byte)2, "SHA256", 64),
    SHA512((byte)3, "SHA512", 128);

    protected Byte byteVal;
    protected String name;
    protected int blockSize;
    protected MessageDigest messageDigest;

    Algorithm(Byte byteVal, String name, int blockSize){
        this.byteVal = byteVal;
        this.name = name;
        this.blockSize = blockSize;

        try {
            this.messageDigest = MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e){
            this.messageDigest = null;
        }
    }

    public Byte getByteVal(){
        return this.byteVal;
    }

    public String getName(){
        return this.name;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public byte[] prepareKey(byte[] key){
        int keyLength = key.length;
        byte[] keyPrepared;
        if ((0 <= keyLength) && (keyLength <= 13)) {
            keyPrepared = Arrays.copyOf(key, 14);
            return keyPrepared;
        }
        if ((14 <= keyLength) && (keyLength <= this.blockSize)){
            keyPrepared = key;
            return keyPrepared;
        }

        keyPrepared = this.messageDigest.digest(key);
        return keyPrepared;
    }
}
