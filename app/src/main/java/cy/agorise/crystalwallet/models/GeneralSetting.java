package cy.agorise.crystalwallet.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import cy.agorise.crystalwallet.enums.SeedType;

/**
 * Created by Henry Varona on 6/11/2017.
 */
@Entity(tableName = "general_setting")
public class GeneralSetting {

    public final static String SETTING_NAME_PREFERED_COUNTRY = "PREFERED_COUNTRY";
    public final static String SETTING_NAME_PREFERED_CURRENCY = "PREFERED_CURRENCY";
    public final static String SETTING_PASSWORD = "PASSWORD";

    /**
     * The id on the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    /**
     * The name of this setting
     */
    @ColumnInfo(name = "name")
    private String mName;

    /**
     * The value of this setting
     */
    @ColumnInfo(name = "value")
    private String mValue;

    public long getId() {
        return mId;
    }

    public void setId(long id){
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }

    public static final DiffCallback<GeneralSetting> DIFF_CALLBACK = new DiffCallback<GeneralSetting>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull GeneralSetting oldSetting, @NonNull GeneralSetting newSetting) {
            return oldSetting.getId() == newSetting.getId();
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull GeneralSetting oldSetting, @NonNull GeneralSetting newSetting) {
            return oldSetting.equals(newSetting);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralSetting that = (GeneralSetting) o;

        if (mId != that.mId) return false;
        if (mName != that.mName) return false;
        return  mValue.equals(that.mValue);

    }
}
