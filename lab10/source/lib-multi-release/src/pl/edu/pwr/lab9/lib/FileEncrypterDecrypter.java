package pl.edu.pwr.lab9.lib;

import javax.crypto.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;

public class FileEncrypterDecrypter {

    public FileEncrypterDecrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public void rsaEncrypt(String fileName, PublicKey pubKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try (FileInputStream iStream = new FileInputStream(fileName)) {
            RSAPublicKey key = (RSAPublicKey) pubKey;
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pubKey);
            int maxEncrypt = (int) Math.floor(key.getModulus().bitLength() / 8.0) - 11;
            while (iStream.available() != 0) {
                byte[] encryptedFileBytes = new byte[maxEncrypt];
                int read = iStream.read(encryptedFileBytes);
                byte[] encryptedTrueSize = Arrays.copyOf(encryptedFileBytes, read);
                encryptedTrueSize = encryptCipher.doFinal(encryptedTrueSize);
                oStream.write(encryptedTrueSize);
            }
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            System.out.println(decoder.decode(ByteBuffer.wrap(oStream.toByteArray())));
        }
        try (FileOutputStream stream = new FileOutputStream(fileName, false)){
            stream.write(Base64.getEncoder().encode(oStream.toByteArray()));
        }

    }

    public void rsaDecrypt(String fileName, PrivateKey privateKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try (FileInputStream iStream = new FileInputStream(fileName)) {
            byte[] targetArr = new byte[iStream.available()];
            iStream.read(targetArr);
            ByteArrayInputStream ist = new ByteArrayInputStream(Base64.getDecoder().decode(targetArr));
            RSAPrivateKey key = (RSAPrivateKey) privateKey;
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            int maxDecrypt = (int) Math.floor(key.getModulus().bitLength()/8.0);
            while (ist.available() != 0) {
                byte[] encryptedFileBytes = new byte[maxDecrypt];
                int read = ist.read(encryptedFileBytes);
                byte[] encryptedTrueSize = Arrays.copyOf(encryptedFileBytes, read);
                encryptedTrueSize = decryptCipher.doFinal(encryptedTrueSize);
                oStream.write(encryptedTrueSize);
            }
        }

        try (FileOutputStream foStream = new FileOutputStream(fileName, false)) {
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
