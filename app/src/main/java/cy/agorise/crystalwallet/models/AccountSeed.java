package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Represents a type of crypto seed for HD wallets
 *
 * Created by Henry Varona on 6/9/2017.
 */
@Entity(tableName = "account_seed")
public class AccountSeed {

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    /**
     * The name or tag of this seed
     */
    @ColumnInfo(name = "name")
    private String mName;

    /**
     * The bytes of the master seed
     */
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
