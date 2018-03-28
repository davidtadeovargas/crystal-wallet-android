package cy.agorise.crystalwallet.manager;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cy.agorise.crystalwallet.application.CrystalApplication;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.requestmanagers.CreateBackupRequest;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequestsListener;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.Chains;
import cy.agorise.graphenej.FileBin;
import cy.agorise.graphenej.models.backup.LinkedAccount;
import cy.agorise.graphenej.models.backup.PrivateKeyBackup;
import cy.agorise.graphenej.models.backup.Wallet;
import cy.agorise.graphenej.models.backup.WalletBackup;

/**
 * Created by henry on 21/3/2018.
 */

public class FileBackupManager implements FileServiceRequestsListener {

    @Override
    public void onNewRequest(FileServiceRequest request) {
        if(request instanceof CreateBackupRequest){
            createBackupBinFile((CreateBackupRequest) request);
        }
    }

    public void createBackupBinFile(CreateBackupRequest request)
    {
        CrystalDatabase db = CrystalDatabase.getAppDatabase(request.getContext());
        db.cryptoNetAccountDao().getAllCryptoNetAccountBySeed(request.getSeed().getId());

        /*TinyDB tinyDB = new TinyDB(myActivity);
        ArrayList<AccountDetails> accountDetails = tinyDB.getListObject(myActivity.getResources().getString(R.string.pref_wallet_accounts), AccountDetails.class);
        String _brnKey = getBrainKey(accountDetails);
        String _accountName = getAccountName(accountDetails);
        String _pinCode = getPin(accountDetails);

        getBinBytesFromBrainkey(_pinCode, _brnKey, _accountName);*/
    }

    public void getBinBytesFromBrainkey(String pin, String brnKey, String accountName, CreateBackupRequest request) {
        BrainKey brainKey = new BrainKey(brnKey, 0);
        try {
            ArrayList<Wallet> wallets = new ArrayList<>();
            ArrayList<LinkedAccount> accounts = new ArrayList<>();
            ArrayList<PrivateKeyBackup> keys = new ArrayList<>();

            Wallet wallet = new Wallet(accountName, brainKey.getBrainKey(), brainKey.getSequenceNumber(), Chains.BITSHARES.CHAIN_ID, pin);
            wallets.add(wallet);

            PrivateKeyBackup keyBackup = new PrivateKeyBackup(brainKey.getPrivateKey().getPrivKeyBytes(),
                    brainKey.getSequenceNumber(), brainKey.getSequenceNumber(), wallet.getEncryptionKey(pin));
            keys.add(keyBackup);

            LinkedAccount linkedAccount = new LinkedAccount(accountName, Chains.BITSHARES.CHAIN_ID);
            accounts.add(linkedAccount);

            WalletBackup backup = new WalletBackup(wallets, keys, accounts);
            byte[] results = FileBin.serializeWalletBackup(backup, pin);
            List<Integer> resultFile = new ArrayList<>();
            for(byte result: results){
                resultFile.add(result & 0xff);
            }
            saveBinContentToFile(resultFile, accountName, request);
        }
        catch (Exception e) {

            //TODO error exception

        }
    }
    static void saveBinContentToFile(List<Integer> content, String _accountName, CreateBackupRequest request )
    {

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dateHourString = df.format(new Date());

        String folder = Environment.getExternalStorageDirectory() + File.separator + "Crystal"; //TODO make constant
        String path =  folder + File.separator + _accountName + dateHourString +".bin";

        boolean success = saveBinFile(path,content,request);
        //TODO handle sucess
    }

    private static boolean saveBinFile (String filePath , List<Integer> content, CreateBackupRequest request)
    {
        boolean success = false;
        try
        {
            //TODO permissions
            // PermissionManager Manager = new PermissionManager();
            // Manager.verifyStoragePermissions(_activity);

            File file = new File(filePath);
            byte[] fileData = new byte[content.size()];

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            for ( int i = 0 ; i < content.size() ; i++ )
            {
                fileData[i] = content.get(i).byteValue();
            }

            bos.write(fileData);
            bos.flush();
            bos.close();

            success = true;
        }
        catch (Exception e)
        {
            //TODO handle error
        }

        return success;
    }
}
