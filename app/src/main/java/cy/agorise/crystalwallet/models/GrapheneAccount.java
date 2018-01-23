package cy.agorise.crystalwallet.models;

import android.content.Context;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.graphenej.BrainKey;

/**
 *
 * Created by henry on 24/9/2017.
 */

public class GrapheneAccount extends CryptoNetAccount {

    public static int subclass = 1;
    protected String name;
    protected String accountId;

    public GrapheneAccount() {
    }

    public GrapheneAccount(CryptoNetAccount account) {
        super(account.getId(),account.getSeedId(),account.getAccountIndex(),account.getCryptoNet());
    }

    public void loadInfo(GrapheneAccountInfo info){
        this.name = info.getName();
        this.accountId = info.getAccountId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Return the owner key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getOwnerKey(Context context){
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId());
        if(seed == null){
            System.out.println("Error: Seed null " + this.getSeedId());
            return null;
        }
        if(seed.getType().equals(SeedType.BRAINKEY)){
            System.out.println("Seed type barinkey");
            return seed.getPrivateKey();
        }else{
            System.out.println("Seed type bip39");
            DeterministicKey masterKey = (DeterministicKey) seed.getPrivateKey();
            DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey,
                    new ChildNumber(48, true));
            DeterministicKey networkKey = HDKeyDerivation.deriveChildKey(purposeKey,
                    new ChildNumber(1, true));
            DeterministicKey accountIndexKey = HDKeyDerivation.deriveChildKey(networkKey,
                    new ChildNumber(0, true));
            DeterministicKey permission = HDKeyDerivation.deriveChildKey(accountIndexKey,
                    new ChildNumber(0, true));
            DeterministicKey address = HDKeyDerivation.deriveChildKey(permission,
                    new ChildNumber(0, false));
            return ECKey.fromPrivate(address.getPrivKeyBytes());
        }
    }

    /**
     * Return the active key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getActiveKey(Context context){
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId());
        if(seed == null)
            return null;
        if(seed.getType().equals(SeedType.BRAINKEY)){
            return new BrainKey(seed.getMasterSeed(),0).getPrivateKey();
        }else{
            System.out.println("calculating activekey from bip39");
            DeterministicKey masterKey = (DeterministicKey) seed.getPrivateKey();
            DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey,
                    new ChildNumber(48, true));
            DeterministicKey networkKey = HDKeyDerivation.deriveChildKey(purposeKey,
                    new ChildNumber(1, true));
            DeterministicKey accountIndexKey = HDKeyDerivation.deriveChildKey(networkKey,
                    new ChildNumber(0, true));
            DeterministicKey permission = HDKeyDerivation.deriveChildKey(accountIndexKey,
                    new ChildNumber(1, true));
            DeterministicKey address = HDKeyDerivation.deriveChildKey(permission,
                    new ChildNumber(0, false));  //TODO implement multiple Address and accounts
            return ECKey.fromPrivate(address.getPrivKeyBytes());
        }
    }

    /**
     * Return the memo key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getMemoKey(Context context){
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId());
        if(seed == null)
            return null;
        if(seed.getType().equals(SeedType.BRAINKEY)){
            return new BrainKey(seed.getMasterSeed(),0).getPrivateKey();
        }else{
            DeterministicKey masterKey = (DeterministicKey) seed.getPrivateKey();
            DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey,
                    new ChildNumber(48, true));
            DeterministicKey networkKey = HDKeyDerivation.deriveChildKey(purposeKey,
                    new ChildNumber(1, true));
            DeterministicKey accountIndexKey = HDKeyDerivation.deriveChildKey(networkKey,
                    new ChildNumber(0, true));
            DeterministicKey permission = HDKeyDerivation.deriveChildKey(accountIndexKey,
                    new ChildNumber(3, true));
            DeterministicKey address = HDKeyDerivation.deriveChildKey(permission,
                    new ChildNumber(0, false));  //TODO implement multiple Address and accounts
            return ECKey.fromPrivate(address.getPrivKeyBytes());
        }
    }
}
