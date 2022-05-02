package pl.edu.pwr.lab9;

import pl.edu.pwr.lab9.lib.FileEncrypterDecrypter;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class MainFrame extends JFrame {
    private JTextField inputFileField;
    private JButton inputButton;
    private JComboBox algorithmSelector;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel mainPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    public MainFrame() throws HeadlessException {

        super("File encryption/decryption tool");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    public static void main(String[] args) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        FileInputStream is = new FileInputStream("/home/ultux/IdeaProjects/wnowak_252700_java/lab9/keystore");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(is, "password".toCharArray());
        Key key = ks.getKey("main-key", "password".toCharArray());
        var secret = ks.getKey("mykey", "password".toCharArray());
        PublicKey pkey = null;
        if (key instanceof PrivateKey) {
            var cert = ks.getCertificate("main-key");
            pkey = cert.getPublicKey();
            System.out.println(key);
        }
        try {
            var encDec = new FileEncrypterDecrypter();
            encDec.rsaDecrypt("/home/ultux/IdeaProjects/wnowak_252700_java/lab9/gui/src/test.txt", secret);
        } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
        new MainFrame();
    }
}
