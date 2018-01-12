package cy.agorise.crystalwallet.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.fragments.BalanceFragment;
import cy.agorise.crystalwallet.fragments.ContactsFragment;
import cy.agorise.crystalwallet.fragments.ReceiveTransactionFragment;
import cy.agorise.crystalwallet.fragments.SendTransactionFragment;
import cy.agorise.crystalwallet.fragments.TransactionsFragment;
import cy.agorise.crystalwallet.util.CircularImageView;

/**
 * Created by Henry Varona on 7/10/2017.
 *
 */

public class BoardActivity  extends AppCompatActivity {

    @BindView(R.id.pager)
    public ViewPager mPager;

    //@BindView(R.id.btnGeneralSettings)
    //public ImageButton btnGeneralSettings;

    @BindView(R.id.fabSend)
    public FloatingActionButton fabSend;

    @BindView(R.id.fabReceive)
    public FloatingActionButton fabReceive;

    @BindView(R.id.fabAddContact)
    public FloatingActionButton fabAddContact;

    public BoardPagerAdapter boardAdapter;

    /*
     * the id of the account of this crypto net balance. It will be loaded
     * when the element is bounded.
     */
    long cryptoNetAccountId;

    @BindView(R.id.surface_view)
    public SurfaceView mSurfaceView;

    @BindView(R.id.toolbar_user_img)
    public CircularImageView userImage;

    @BindView(R.id.lightning)
    public ImageView lightning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        ButterKnife.bind(this);

        //-1 represents a crypto net account not loaded yet
        this.cryptoNetAccountId = -1;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Appbar animation
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Log.d(TAG,"surfaceCreated");
                MediaPlayer mediaPlayer = MediaPlayer.create(BoardActivity.this, R.raw.appbar_background);
                mediaPlayer.setDisplay(mSurfaceView.getHolder());
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                //Log.d(TAG,"surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //Log.d(TAG,"surfaceDestroyed");
            }
        });

        boardAdapter = new BoardPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(boardAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        fabReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveToThisAccount();
            }
        });
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFromThisAccount();
            }
        });

        // Hide Add Contact fab, it won't hide until first page changed...
        // Convert 72dp to pixels (fab is 56dp in diameter + 16dp margin)
        final int fabDistanceToHide = (int) (72 * Resources.getSystem().getDisplayMetrics().density);
        fabAddContact.animate().translationY(fabDistanceToHide)
                .setInterpolator(new LinearInterpolator()).start();

        // Hide and show respective fabs when convenient
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.animate().translationY(fabDistanceToHide)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;

                    case 1:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.animate().translationY(fabDistanceToHide)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;

                    default:
                        fabReceive.hide();
                        fabSend.hide();
                        fabAddContact.animate().translationY(0)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*
     * dispatch the user to the accounts fragment
     */
    @OnClick(R.id.toolbar_user_img)
    public void accounts() {
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("AccountsDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        AccountsFragment newFragment = AccountsFragment.newInstance(this.cryptoNetAccountId);
        newFragment.show(ft, "AccountsDialog");*/
        Intent intent = new Intent(this, AccountsActivity.class);

        Pair p1 = Pair.create(userImage, "gravatarTransition");
        Pair p2 = Pair.create(lightning, "lightningTransition");

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, userImage, "gravatarTransition");
        //ActivityCompat.startActivity(this, intent, options.toBundle());
        startActivity(intent);
    }

    /*
     * dispatch the user to the receive fragment using this account
     */
    public void receiveToThisAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("ReceiveDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ReceiveTransactionFragment newFragment = ReceiveTransactionFragment.newInstance(this.cryptoNetAccountId);
        newFragment.show(ft, "ReceiveDialog");
    }

    /*
     * dispatch the user to the send fragment using this account
     */
    public void sendFromThisAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("SendDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SendTransactionFragment newFragment = SendTransactionFragment.newInstance(this.cryptoNetAccountId);
        newFragment.show(ft, "SendDialog");
    }

    private class BoardPagerAdapter extends FragmentStatePagerAdapter {
        BoardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new BalanceFragment();
                case 1:
                    return new TransactionsFragment();
                case 2:
                    return new ContactsFragment();
            }


            return null; //new OnConstructionFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
