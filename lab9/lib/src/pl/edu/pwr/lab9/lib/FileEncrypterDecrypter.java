package pl.edu.pwr.lab9.lib;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;

public class FileEncrypterDecrypter {

    public FileEncrypterDecrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public void rsaEncrypt(String fileName, KeyPair keyPair) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var oStream = new ByteArrayOutputStream();
        try (var iStream = new FileInputStream(fileName)) {
            var key = (RSAPublicKey) keyPair.getPublic();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            var maxEncrypt = (int) Math.floor(key.getModulus().bitLength() / 8.0) - 11;
            while (iStream.available() != 0) {
                byte[] encryptedFileBytes = new byte[maxEncrypt];
                var read = iStream.read(encryptedFileBytes);
                var encryptedTrueSize = Arrays.copyOf(encryptedFileBytes, read);
                encryptedTrueSize = encryptCipher.doFinal(encryptedTrueSize);
                oStream.write(encryptedTrueSize);
            }
            System.out.println(oStream.toString(StandardCharsets.UTF_8));
        }
        try (FileOutputStream stream = new FileOutputStream(fileName, false)){
            stream.write(Base64.getEncoder().encode(oStream.toByteArray()));
        }

    }

    public void rsaDecrypt(String fileName, KeyPair keyPair) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        var oStream = new ByteArrayOutputStream();
        try (var iStream = new FileInputStream(fileName)) {
            var ist = new ByteArrayInputStream(Base64.getDecoder().decode(iStream.readAllBytes()));
            var key = (RSAPublicKey) keyPair.getPublic();
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            var maxDecrypt = (int) Math.floor(key.getModulus().bitLength()/8.0);
            while (ist.available() != 0) {
                byte[] encryptedFileBytes = new byte[maxDecrypt];
                var read = ist.read(encryptedFileBytes);
                byte[] encryptedTrueSize = Arrays.copyOf(encryptedFileBytes, read);
                encryptedTrueSize = decryptCipher.doFinal(encryptedTrueSize);
                oStream.write(encryptedTrueSize);
            }
        }

        try (var foStream = new FileOutputStream(fileName, false)) {
            foStream.write(oStream.toByteArray());
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

    public void aesDecrypt(String fileName, Key secret) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encryptedFileBytes = Files.readAllBytes(FileSystems.getDefault().getPath(fileName));
        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(decryptedFileBytes);
        }
    }
}
