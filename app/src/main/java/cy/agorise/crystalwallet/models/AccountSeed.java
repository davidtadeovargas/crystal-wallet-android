package cy.agorise.crystalwallet.models;

import android.arch.persistence.room.*;

/**
 * Created by Henry Varona on 6/9/2017.
 */
@Entity
public class AccountSeed {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "master_seed")
    private String mMasterSeed;

    public int getId() {
        return mId;
    }

    public void setId(int id){
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getMasterSeed() {
        return mMasterSeed;
    }

    public void setMasterSeed(String mMasterSeed) {
        this.mMasterSeed = mMasterSeed;
    }


}
