package cy.agorise.crystalwallet.notifiers;

import android.content.Context;
import android.media.MediaPlayer;

import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEvent;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetEventsListener;
import cy.agorise.crystalwallet.requestmanagers.ReceivedFundsCryptoNetEvent;

/**
 * Created by Henry Varona on 29/4/2018.
 */

public class CrystalWalletNotifier implements CryptoNetEventsListener{

    private Context context;

    public CrystalWalletNotifier(Context context){
        this.context = context;
    }

    public void onCryptoNetEvent(CryptoNetEvent event) {
        if (event instanceof ReceivedFundsCryptoNetEvent){
            playReceivedFundsSound();
        }
    }

    private void playReceivedFundsSound() {
        MediaPlayer defaultMediaPlayer = MediaPlayer.create(this.context, R.raw.woohoo);
        defaultMediaPlayer.start();
    }
}
