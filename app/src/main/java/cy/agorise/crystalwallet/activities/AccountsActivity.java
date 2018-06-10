package cy.agorise.crystalwallet.activities;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
//import com.nicdahlquist.pngquant.LibPngQuant;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.viewmodels.AccountSeedListViewModel;
import cy.agorise.crystalwallet.viewmodels.CryptoNetAccountListViewModel;
import cy.agorise.crystalwallet.views.AccountSeedListView;
import cy.agorise.crystalwallet.views.CryptoNetAccountListView;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by xd on 1/9/18.
 *
 */

public class AccountsActivity extends AppCompatActivity {

    @BindView(R.id.tvSettings)
    TextView tvSettings;

    @BindView(R.id.tvClose)
    TextView tvClose;

    @BindView(R.id.vAccountList)
    CryptoNetAccountListView vAccountList;

    @BindView(R.id.user_img)
    CircleImageView userImg;

    static final int NEW_PICTURE_REQUEST_CODE = 1;

    FileObserver fileObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);

        CryptoNetAccountListViewModel crytpoNetAccountListViewModel = ViewModelProviders.of(this).get(CryptoNetAccountListViewModel.class);
        LiveData<List<CryptoNetAccount>> accountData = crytpoNetAccountListViewModel.getCryptoNetAccounts();
        vAccountList.setData(null);

        accountData.observe(this, new Observer<List<CryptoNetAccount>>() {
            @Override
            public void onChanged(List<CryptoNetAccount> cryptoNetAccounts) {
                vAccountList.setData(cryptoNetAccounts);
            }
        });

        loadUserImage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUserImage();
    }

    public void loadUserImage(){
        //Search for a existing photo
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File photoFile = new File(directory + File.separator + "photo.png");

        if (photoFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
            userImg.setImageBitmap(bitmap);
        }
    }

    @OnClick(R.id.tvSettings)
    public void onTvSettingsClick(){
        onBackPressed();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvClose)
    public void cancel(){
        onBackPressed();
    }

    @OnClick(R.id.user_img)
    public void loadUserImageSetting(){
        ImagePicker.create(this)
                .single()
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            if (image != null){
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("profile", Context.MODE_PRIVATE);
                if (!directory.exists()) {
                    directory.mkdir();
                }

                try {
                    File imageCompressed = new Compressor(this)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .setMaxWidth(320)
                            .setMaxHeight(480)
                            .compressToFile(new File(image.getPath()));
                    imageCompressed.renameTo(new File(directory + File.separator + "photo.png"));


                    if (imageCompressed.exists()){
                        imageCompressed.delete();
                    }
                    imageCompressed.createNewFile();
                    //File inputPngFile = new File(image.getPath());
                    //File outputPngFile = new File("photo.png");
                    //new LibPngQuant().pngQuantFile(inputPngFile, outputPngFile);

                    //Bitmap bitmapDecoded= BitmapFactory.decodeFile(image.getPath());
                    //Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmapDecoded, 320, 480, false);
                    //resizedbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    //fos.close();
                } catch (Exception e) {
                    //Log.e("SAVE_IMAGE", e.getMessage(), e);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}