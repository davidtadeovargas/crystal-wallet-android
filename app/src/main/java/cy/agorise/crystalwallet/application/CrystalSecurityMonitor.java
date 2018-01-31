package cy.agorise.crystalwallet.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cy.agorise.crystalwallet.activities.CreateSeedActivity;
import cy.agorise.crystalwallet.activities.PinRequestActivity;

/**
 * Created by Henry Varona on 27/1/2018.
 */

class CrystalSecurityMonitor implements Application.ActivityLifecycleCallbacks {
    private int numStarted = 0;

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            callPasswordRequest(activity);
        }
        numStarted++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            callPasswordRequest(activity);
        }
    }

    public void callPasswordRequest(Activity activity){
        if ((!activity.getIntent().hasExtra("ACTIVITY_TYPE")) || (!activity.getIntent().getStringExtra("ACTIVITY_TYPE").equals("PASSWORD_REQUEST"))) {
            //Intent intent = new Intent(activity, PinRequestActivity.class);
            //intent.putExtra("ACTIVITY_TYPE", "PASSWORD_REQUEST");
            //activity.startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        //
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //
    }



}
