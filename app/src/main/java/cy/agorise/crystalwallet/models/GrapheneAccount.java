package cy.agorise.crystalwallet.models;

import android.content.Context;

import org.bitcoinj.core.ECKey;

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
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId()).getValue();
        if(seed == null)
        return null;
        if(seed.getType().equals(SeedType.BRAINKEY)){
            return new BrainKey(seed.getMasterSeed(),0).getPrivateKey();
        }else{
            //TODO implement slip48
            return null;
        }
    }

    /**
     * Return the active key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getActiveKey(Context context){
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId()).getValue();
        if(seed == null)
            return null;
        if(seed.getType().equals(SeedType.BRAINKEY)){
            return new BrainKey(seed.getMasterSeed(),0).getPrivateKey();
        }else{
            //TODO implement slip48
            return null;
        }
    }

    /**
     * Return the memo key, generates from the seed if it has not been generated. null if it can't be generated
     */
    public ECKey getMemoKey(Context context){
        AccountSeed seed = CrystalDatabase.getAppDatabase(context).accountSeedDao().findById(this.getSeedId()).getValue();
        if(seed == null)
            return null;
        if(seed.getType().equals(SeedType.BRAINKEY)){
            return new BrainKey(seed.getMasterSeed(),0).getPrivateKey();
        }else{
            //TODO implement slip48
            return null;
        }
    }
}
