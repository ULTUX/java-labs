package pl.edu.pwr.lab9;

import pl.edu.pwr.lab9.lib.FileEncrypterDecrypter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class MainFrame extends JFrame {
    private JTextField inputFileField;
    private JButton inputButton;
    private JComboBox<String> algorithmSelector;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel mainPanel;
    private JComboBox<String> privateKeySelector;
    private JComboBox<String> publicKeySelector;
    private JTextField keyStoreLocField;
    private JButton selectKeyStoreButton;
    private JPasswordField passwordField1;
    private JButton loadKeyStoreButton;

    private KeyStore keyStore;

    private File keyStoreLocation;

    public MainFrame() throws HeadlessException {

        super("File encryption/decryption tool");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
        selectKeyStoreButton.addActionListener(e -> selectKeyStore());
        loadKeyStoreButton.addActionListener(e -> fetchKeys());
        inputButton.addActionListener(e -> selectFile());
        var algSelectModel = new DefaultComboBoxModel<String>();
        algorithmSelector.setModel(algSelectModel);
        algSelectModel.addElement("RSA");
        algSelectModel.addElement("AES");

    }

    private void selectFile() {
        var fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        var res = fileChooser.showDialog(this, "Select");
        if (res == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.getSelectedFile();
            inputFileField.setText(file.toString());
        }

    }

    private void selectKeyStore() {
        var keyStoreLocChooser = new JFileChooser();
        keyStoreLocChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        keyStoreLocChooser.setMultiSelectionEnabled(false);
        var result = keyStoreLocChooser.showDialog(this, "Open");
        if (result == JFileChooser.APPROVE_OPTION) {
            keyStoreLocation = keyStoreLocChooser.getSelectedFile();
            keyStoreLocField.setText(keyStoreLocation.toString());
        }
    }

    private void fetchKeys() {
        if (passwordField1.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Empty password is not allowed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (var inputStream = new FileInputStream(keyStoreLocation)) {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, passwordField1.getPassword());

            var privKeyModel = new DefaultComboBoxModel<String>();
            var pubKeyModel = new DefaultComboBoxModel<String>();
            privateKeySelector.setModel(privKeyModel);
            publicKeySelector.setModel(pubKeyModel);
            keyStore.aliases().asIterator().forEachRemaining(s -> {
                privKeyModel.addElement(s);
                Certificate cert = null;
                try {
                    cert = keyStore.getCertificate(s);
                if (cert != null) {
                    pubKeyModel.addElement(s);
                }
                } catch (KeyStoreException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            JOptionPane.showMessageDialog(this, "Could not load selected keystore.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
//        FileInputStream is = new FileInputStream("/home/ultux/IdeaProjects/wnowak_252700_java/lab9/keystore");
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        ks.load(is, "password".toCharArray());
//        System.out.println(ks.getKey("main-key", "password".toCharArray()).getAlgorithm());
//
//        Key key = ks.getKey("main-key", "password".toCharArray());
//        var secret = ks.getKey("secret", "password".toCharArray());
//        PublicKey pkey = null;
//        if (key instanceof PrivateKey) {
//            var cert = ks.getCertificate("main-key");
//            pkey = cert.getPublicKey();
//        }
//        try {
//            var encDec = new FileEncrypterDecrypter();
//            encDec.rsaDecrypt("/home/ultux/IdeaProjects/wnowak_252700_java/lab9/gui/src/test.txt", new KeyPair(pkey, (PrivateKey) key));
//        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
        new MainFrame();
    }
}
