package pl.edu.pwr.lab9.lib;

import javax.crypto.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class FileEncrypterDecrypter {

    public FileEncrypterDecrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public void rsaEncrypt(String fileName, KeyPair keyPair) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] fileBytes = Files.readAllBytes(FileSystems.getDefault().getPath(fileName));
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);
        try (FileOutputStream stream = new FileOutputStream(fileName)){
            stream.write(encryptedFileBytes);
        }
    }

    public void rsaDecrypt(String fileName, KeyPair keyPair) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encryptedFileBytes = Files.readAllBytes(FileSystems.getDefault().getPath(fileName));
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(decryptedFileBytes);
        }
    }

    public void aesEncrypt(String fileName, Key secret) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] fileBytes = Files.readAllBytes(FileSystems.getDefault().getPath(fileName));
        Cipher encryptCipher = Cipher.getInstance("AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);
        try (FileOutputStream stream = new FileOutputStream(fileName)){
            stream.write(encryptedFileBytes);
        }
    }

    public void rsaDecrypt(String fileName, Key secret) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encryptedFileBytes = Files.readAllBytes(FileSystems.getDefault().getPath(fileName));
        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(decryptedFileBytes);
        }
    }
}
