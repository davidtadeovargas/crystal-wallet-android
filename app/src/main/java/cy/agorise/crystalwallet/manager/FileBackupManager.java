package cy.agorise.crystalwallet.manager;

import android.os.Environment;

import org.bitcoinj.core.ECKey;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.network.CryptoNetManager;
import cy.agorise.crystalwallet.requestmanagers.CreateBackupRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequestsListener;
import cy.agorise.crystalwallet.requestmanagers.ImportBackupRequest;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.FileBin;
import cy.agorise.graphenej.api.GetKeyReferences;
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
        } else if (request instanceof ImportBackupRequest){
            readBinFile((ImportBackupRequest) request);
        }
    }

    private void createBackupBinFile(CreateBackupRequest request)
    {
        CrystalDatabase db = CrystalDatabase.getAppDatabase(request.getContext());
        List<BitsharesSeedName> seedNames = new ArrayList<>();
        List<AccountSeed> seeds = db.accountSeedDao().getAllNoLiveData();
        for(AccountSeed seed : seeds) {
            List<CryptoNetAccount> accounts = db.cryptoNetAccountDao().getAllCryptoNetAccountBySeed(seed.getId());
            for(CryptoNetAccount account : accounts){
                if(account.getCryptoNet().equals(CryptoNet.BITSHARES)){
                    seedNames.add(new BitsharesSeedName(account.getName(),seed.getMasterSeed()));
                }
            }
        }

        getBinBytesFromBrainkey(seedNames,request); //TODO make funcion for non-bitshares accounts
    }

    private void getBinBytesFromBrainkey(List<BitsharesSeedName> bitsharesSeedNames, CreateBackupRequest request) {

        try {
            ArrayList<Wallet> wallets = new ArrayList<>();
            ArrayList<LinkedAccount> accounts = new ArrayList<>();
            ArrayList<PrivateKeyBackup> keys = new ArrayList<>();
            String fileName = null; //TODO choice a good name,  now we use the first bitshares account as the bin backup
            for(BitsharesSeedName bitsharesSeedName : bitsharesSeedNames) {
                if(fileName == null){
                    fileName = bitsharesSeedName.accountName;
                }
                BrainKey brainKey = new BrainKey(bitsharesSeedName.accountSeed, 0); //TODO chain to use BIP39
                //TODO adapt CHAIN ID
                Wallet wallet = new Wallet(bitsharesSeedName.accountName, brainKey.getBrainKey(), brainKey.getSequenceNumber(), CryptoNetManager.getChaindId(CryptoNet.BITSHARES), request.getPassword());
                wallets.add(wallet);
                PrivateKeyBackup keyBackup = new PrivateKeyBackup(brainKey.getPrivateKey().getPrivKeyBytes(),
                        brainKey.getSequenceNumber(), brainKey.getSequenceNumber(), wallet.getEncryptionKey(request.getPassword()));
                keys.add(keyBackup);
                LinkedAccount linkedAccount = new LinkedAccount(bitsharesSeedName.accountName, CryptoNetManager.getChaindId(CryptoNet.BITSHARES));
                accounts.add(linkedAccount);
            }

            WalletBackup backup = new WalletBackup(wallets, keys, accounts);
            byte[] results = FileBin.serializeWalletBackup(backup, request.getPassword());
            List<Integer> resultFile = new ArrayList<>();
            for(byte result: results){
                resultFile.add(result & 0xff);
            }
            saveBinContentToFile(resultFile, fileName, request);
        }
        catch (Exception e) {
            request.setStatus(CreateBackupRequest.StatusCode.FAILED);
            //TODO error exception

        }
    }

    private void saveBinContentToFile(List<Integer> content, String fileName, CreateBackupRequest request )
    {

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dateHourString = df.format(new Date());

        String folder = Environment.getExternalStorageDirectory() + File.separator + "Crystal"; //TODO make constant
        String path =  folder + File.separator + fileName + dateHourString +".bin";

        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }

        boolean success = saveBinFile(path,content,request);
        if(success) {
            request.setFilePath(path);
            request.setStatus(CreateBackupRequest.StatusCode.SUCCEEDED);
        }else{
            request.setStatus(CreateBackupRequest.StatusCode.FAILED);
        }

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

    private void readBinFile(ImportBackupRequest request){
        try {
            File file = new File(request.getFilePath());
            DataInputStream dis = new DataInputStream(new FileInputStream(file));

            ArrayList<Integer> readBytes = new ArrayList<>();


            for ( int i = 0 ; i < file.length() ; i++ )
            {
                int val = unsignedToBytes(dis.readByte());
                readBytes.add(val);
            }

            dis.close();

            byte[] byteArray = new byte[readBytes.size()];
            for(int i = 0 ; i < readBytes.size();i++){
                byteArray[i] = readBytes.get(i).byteValue();
            }

            WalletBackup walletBackup = FileBin.deserializeWalletBackup(byteArray,request.getPassword());
            if(walletBackup == null){
                //TODO handle error
                return;
            }

            List<BitsharesSeedName> seedNames = new ArrayList<>();

            for(int i = 0; i < walletBackup.getKeyCount(); i++){
                String brainKey = walletBackup.getWallet(i).decryptBrainKey(request.getPassword());
                int sequence = walletBackup.getWallet(i).getBrainkeySequence();
                String accountName = walletBackup.getWallet(i).getPrivateName();
                seedNames.add(new BitsharesSeedName(accountName,brainKey));
            }
            //TODO handle the accounts
            request.setStatus(ImportBackupRequest.StatusCode.SUCCEEDED);


        } catch (Exception e) {
            request.setStatus(ImportBackupRequest.StatusCode.FAILED);
            //TODO handle exception
        }
    }

    private int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    public class BitsharesSeedName{
        String accountName;
        String accountSeed;

        public BitsharesSeedName(String accountName, String accountSeed) {
            this.accountName = accountName;
            this.accountSeed = accountSeed;
        }
    }



}
