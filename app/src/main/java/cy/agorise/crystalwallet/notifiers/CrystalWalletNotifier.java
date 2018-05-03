package cy.agorise.crystalwallet.notifiers;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEvent;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEventsListener;
import cy.agorise.crystalwallet.requestmanagers.ReceivedFundsCryptoNetEvent;
import cy.agorise.crystalwallet.viewmodels.GeneralSettingListViewModel;

/**
 * Created by Henry Varona on 29/4/2018.
 */

public class CrystalWalletNotifier implements CryptoNetEventsListener{

    private Context context;
    private MediaPlayer receivedFundsMediaPlayer;
    private Application application;

    public CrystalWalletNotifier(Application application){
        this.context = application.getApplicationContext();
        this.application = application;
        loadReceivedFundsSound();
    }

    public void loadReceivedFundsSound(){
        GeneralSettingListViewModel generalSettingListViewModel = new GeneralSettingListViewModel(this.application);
        GeneralSetting receivedFundsSoundGeneralSetting = generalSettingListViewModel.getGeneralSettingByName(GeneralSetting.SETTING_NAME_RECEIVED_FUNDS_SOUND_PATH);

        File receivedFundsSoundFile = null;

        if ((receivedFundsSoundGeneralSetting != null)){
            if (!receivedFundsSoundGeneralSetting.getValue().equals("")) {
                receivedFundsSoundFile = new File(receivedFundsSoundGeneralSetting.getValue());

                if (!receivedFundsSoundFile.exists()){
                    receivedFundsSoundFile = null;
                }
            }
        }

        if (receivedFundsSoundFile != null){
            receivedFundsMediaPlayer = new MediaPlayer();
            try {
                receivedFundsMediaPlayer.setDataSource(receivedFundsSoundGeneralSetting.getValue());
                receivedFundsMediaPlayer.prepare();
            } catch (IOException e) {
                receivedFundsMediaPlayer = MediaPlayer.create(this.context, R.raw.woohoo);
            }
        } else {
            receivedFundsMediaPlayer = MediaPlayer.create(this.context, R.raw.woohoo);
        }
    }

    public void onCryptoNetEvent(CryptoNetEvent event) {
        if (event instanceof ReceivedFundsCryptoNetEvent){
            playReceivedFundsSound();
        }
    }

    private void playReceivedFundsSound() {
        receivedFundsMediaPlayer.start();
    }
}
