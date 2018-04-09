package cy.agorise.crystalwallet.manager;

import android.os.Environment;

import com.google.common.primitives.Bytes;
import com.google.gson.GsonBuilder;

import org.bitcoinj.core.ECKey;
import org.tukaani.xz.CorruptedInputException;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.XZInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cy.agorise.crystalwallet.dao.AccountSeedDao;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.enums.CryptoNet;
import cy.agorise.crystalwallet.enums.SeedType;
import cy.agorise.crystalwallet.models.AccountSeed;
import cy.agorise.crystalwallet.models.CryptoNetAccount;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.network.CryptoNetManager;
import cy.agorise.crystalwallet.requestmanagers.CreateBackupRequest;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.requestmanagers.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequest;
import cy.agorise.crystalwallet.requestmanagers.FileServiceRequestsListener;
import cy.agorise.crystalwallet.requestmanagers.ImportBackupRequest;
import cy.agorise.crystalwallet.requestmanagers.ValidateImportBitsharesAccountRequest;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.FileBin;
import cy.agorise.graphenej.Util;
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
                        seedNames.add(new BitsharesSeedName(account.getName(), seed.getMasterSeed()));
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

                int sequence = 0;
                //TODO adapt CHAIN ID
                Wallet wallet = new Wallet(bitsharesSeedName.accountName, bitsharesSeedName.accountSeed, sequence, CryptoNetManager.getChaindId(CryptoNet.BITSHARES), request.getPassword());
                wallets.add(wallet);

                BrainKey brainKey = new BrainKey(bitsharesSeedName.accountSeed, sequence); //TODO chain to use BIP39
                PrivateKeyBackup keyBackup = new PrivateKeyBackup(brainKey.getPrivateKey().getPrivKeyBytes(),
                        sequence, sequence, wallet.getEncryptionKey(request.getPassword()));
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
            e.printStackTrace();
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
            if(folderFile.mkdirs()){
                System.out.println("folder created");
            }else{
                System.out.println("couldn't create folder");
                request.setStatus(CreateBackupRequest.StatusCode.FAILED);
                return;
            }
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
            e.printStackTrace();
            //TODO handle error
        }

        return success;
    }

    private void readBinFile(final ImportBackupRequest request){
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

            WalletBackup walletBackup = deserializeWalletBackup(byteArray,request.getPassword());
            if(walletBackup == null){
                //TODO handle error
                System.out.println("FileBackupManager error walletBackup null");
                request.setStatus(ImportBackupRequest.StatusCode.FAILED);
                return;
            }

            List<BitsharesSeedName> seedNames = new ArrayList<>();

            for(int i = 0; i < walletBackup.getKeyCount(); i++){
                String brainKey = walletBackup.getWallet(i).decryptBrainKey(request.getPassword());
                int sequence = walletBackup.getWallet(i).getBrainkeySequence();
                String accountName = walletBackup.getWallet(i).getPrivateName();
                seedNames.add(new BitsharesSeedName(accountName,brainKey));
            }
            //TODO handle more than one account
            final CrystalDatabase db = CrystalDatabase.getAppDatabase(request.getContext());
            final AccountSeedDao accountSeedDao = db.accountSeedDao();
            for(BitsharesSeedName seedName : seedNames) {
                final ValidateImportBitsharesAccountRequest validatorRequest =
                        new ValidateImportBitsharesAccountRequest(seedName.accountName, seedName.accountSeed);
                validatorRequest.setListener(new CryptoNetInfoRequestListener() {
                    @Override
                    public void onCarryOut() {
                        if (!validatorRequest.getMnemonicIsCorrect()) {
                            request.setStatus(ImportBackupRequest.StatusCode.FAILED); // TODO reason bad seed
                        } else {
                            AccountSeed seed = new AccountSeed();
                            seed.setName(validatorRequest.getAccountName());
                            seed.setType(validatorRequest.getSeedType());
                            seed.setMasterSeed(validatorRequest.getMnemonic());
                            long idSeed = accountSeedDao.insertAccountSeed(seed);
                            if(idSeed >= 0) {
                                GrapheneAccount account = new GrapheneAccount();
                                account.setSeedId(idSeed);
                                account.setName(validatorRequest.getAccountName());
                                BitsharesAccountManager bManger = new BitsharesAccountManager();
                                bManger.importAccountFromSeed(account,request.getContext());


                                request.setStatus(ImportBackupRequest.StatusCode.SUCCEEDED);
                            }else{
                                request.setStatus(ImportBackupRequest.StatusCode.FAILED); //TODO reason couldn't insert seed
                            }
                        }
                    }
                });
                CryptoNetInfoRequests.getInstance().addRequest(validatorRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    /**
     * This part is copied from the graphenej library, to edit possible error.
     * TODO in graphenej library to catch EOFException reading files
     */

    public static WalletBackup deserializeWalletBackup(byte[] input, String password){
        try{
            byte[] publicKey = new byte[33];
            byte[] rawDataEncripted = new byte[input.length - 33];

            System.arraycopy(input, 0, publicKey, 0, 33);
            System.arraycopy(input, 33, rawDataEncripted, 0, rawDataEncripted.length);

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            ECKey randomECKey = ECKey.fromPublicOnly(publicKey);
            byte[] finalKey = randomECKey.getPubKeyPoint().multiply(ECKey.fromPrivate(md.digest(password.getBytes("UTF-8"))).getPrivKey()).normalize().getXCoord().getEncoded();
            MessageDigest md1 = MessageDigest.getInstance("SHA-512");
            finalKey = md1.digest(finalKey);
            byte[] decryptedData = Util.decryptAES(rawDataEncripted, Util.bytesToHex(finalKey).getBytes());

            byte[] checksum = new byte[4];
            System.arraycopy(decryptedData, 0, checksum, 0, 4);
            byte[] compressedData = new byte[decryptedData.length - 4];
            System.arraycopy(decryptedData, 4, compressedData, 0, compressedData.length);

            byte[] decompressedData = decompress(compressedData, Util.LZMA);
            String walletString = new String(decompressedData, "UTF-8");
            System.out.println("Wallet str: "+walletString);
            return new GsonBuilder().create().fromJson(walletString, WalletBackup.class);
        }catch(NoSuchAlgorithmException e){
            System.out.println("NoSuchAlgorithmException. Msg: "+e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException. Msg: "+e.getMessage());
        }
        return null;
    }

    public static byte[] decompress(byte[] inputBytes, int which) {
        InputStream in = null;
        try {
            System.out.println("Bytes: "+Util.bytesToHex(inputBytes));
            ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
            ByteArrayOutputStream output = new ByteArrayOutputStream(16*2048);
            if(which == Util.XZ) {
                in = new XZInputStream(input);
            }else if(which == Util.LZMA){
                in = new LZMAInputStream(input);
            }
            int size;
            try{
                while ((size = in.read()) != -1) {
                    output.write(size);
                }
            }catch(CorruptedInputException e){
                // Taking property byte
                byte[] properties = Arrays.copyOfRange(inputBytes, 0, 1);
                // Taking dict size bytes
                byte[] dictSize = Arrays.copyOfRange(inputBytes, 1, 5);
                // Taking uncompressed size bytes
                byte[] uncompressedSize = Arrays.copyOfRange(inputBytes, 5, 13);

                // Reversing bytes in header
                byte[] header = Bytes.concat(properties, Util.revertBytes(dictSize), Util.revertBytes(uncompressedSize));
                byte[] payload = Arrays.copyOfRange(inputBytes, 13, inputBytes.length);

                // Trying again
                input = new ByteArrayInputStream(Bytes.concat(header, payload));
                output = new ByteArrayOutputStream(2048);
                if(which == Util.XZ) {
                    in = new XZInputStream(input);
                }else if(which == Util.LZMA){
                    in = new LZMAInputStream(input);
                }
                try{
                    while ((size = in.read()) != -1) {
                        output.write(size);
                    }
                }catch(CorruptedInputException ex){
                    System.out.println("CorruptedInputException. Msg: "+ex.getMessage());
                }
            }catch(EOFException e){

            }
            in.close();
            return output.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



}
