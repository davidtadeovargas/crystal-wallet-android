package cy.agorise.crystalwallet.models.seed;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.graphenej.crypto.SecureRandomGenerator;

/**
 * Created by henry on 6/1/2018.
 */

public class BIP39  extends AccountSeed{
    /**
     * Teh amount of words for this seed
     */
    private final int wmWordNumber = 12;

    /**
     * Constructor from the dataabse
     * @param id The id on the database of this seed
     */
    public BIP39(long id, String words) {
        this.setId(id);
        this.setType(SeedType.BIP39);
        this.setMasterSeed(words);
    }

    /**
     * Constructor that generates the list of words
     * @param wordList Dictionary to be used
     */
    public BIP39(String[] wordList) {
        try {
            this.setType(SeedType.BIP39);
            int entropySize = ((this.wmWordNumber * 11) / 8) * 8;
            // We get a true random number
            SecureRandom secureRandom = SecureRandomGenerator.getSecureRandom();
            byte[] entropy = new byte[entropySize / 8];
            secureRandom.nextBytes(entropy);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] shaResult = md.digest(entropy);
            int mask = 0x80;
            int cheksum = 0;
            for (int i = 0; i < entropySize / 32; i++) {
                cheksum = cheksum ^ (shaResult[0] & mask);
                mask = mask / 2;
            }
            int[] wordsIndex = new int[(entropySize / 11) + 1];
            for (int i = 0; i < wordsIndex.length; i++) {
                wordsIndex[i] = 0;
            }

            int lastIndex = 0;
            int lastBit = 0;
            for (int i = 0; i < entropy.length; i++) {
                for (int j = 7; j >= 0; j--) {
                    if (lastBit == 11) {
                        lastBit = 0;
                        ++lastIndex;
                    }
                    wordsIndex[lastIndex] = wordsIndex[lastIndex] ^ ((int) (Math.pow(2, 11 - (lastBit + 1))) * (entropy[i] & ((int) Math.pow(2, j))) >> j);
                    ++lastBit;
                }
            }
            for (int j = 7; j >= 0; j--) {
                if (lastBit == 11) {
                    break;
                }
                wordsIndex[lastIndex] = wordsIndex[lastIndex] ^ ((int) (Math.pow(2, 11 - (lastBit + 1))) * (cheksum & ((int) Math.pow(2, j))) >> j);
                ++lastBit;
            }
            StringBuilder words = new StringBuilder();
            for (int windex : wordsIndex) {
                words.append(wordList[windex]).append(" ");
            }
            words.deleteCharAt(words.length() - 1);
            this.setMasterSeed(words.toString());
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    public byte[] getSeed() {
        return MnemonicCode.toSeed(Arrays.asList(this.getMasterSeed().split(" ")), "");
    }

    public ECKey getBitsharesActiveKey(int number){
        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(this.getSeed());
        DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey,
                new ChildNumber(48, true));
        DeterministicKey networkKey = HDKeyDerivation.deriveChildKey(purposeKey,
                new ChildNumber(1, true));
        DeterministicKey accountIndexKey = HDKeyDerivation.deriveChildKey(networkKey,
                new ChildNumber(0, true));
        DeterministicKey permission = HDKeyDerivation.deriveChildKey(accountIndexKey,
                new ChildNumber(1, true));
        DeterministicKey address = HDKeyDerivation.deriveChildKey(permission,
                new ChildNumber(number, false));  //TODO implement multiple Address and accounts
        return org.bitcoinj.core.ECKey.fromPrivate(address.getPrivKeyBytes());
    }
}
