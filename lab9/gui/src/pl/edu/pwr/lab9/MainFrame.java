package pl.edu.pwr.lab9;

import pl.edu.pwr.lab9.lib.FileEncrypterDecrypter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
    private JLabel privateKeyLabel;

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
        algorithmSelector.addActionListener(e -> handleSelectionChange());
        encryptButton.addActionListener(e -> handleEncryptBtnClicked());
        decryptButton.addActionListener(e -> handleDecryptBtnClicked());

    }

    private void handleDecryptBtnClicked() {
        if (algorithmSelector.getSelectedItem() == null) return;
        switch ((String) algorithmSelector.getSelectedItem()) {
            case "RSA":
                var privKey = getPrivateKey();
                if (privKey == null) return;
                try {
                    new FileEncrypterDecrypter().rsaDecrypt(inputFileField.getText(), (PrivateKey) privKey);
                } catch (NoSuchPaddingException | IllegalBlockSizeException | IOException | NoSuchAlgorithmException |
                         BadPaddingException | InvalidKeyException | ClassCastException e) {
                    JOptionPane.showMessageDialog(this, "Could not RSA decrypt selected file.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                break;
            case "AES":
                var secret = getPrivateKey();
                if (secret == null) return;
                try {
                    new FileEncrypterDecrypter().aesDecrypt(inputFileField.getText(), secret);
                } catch (NoSuchPaddingException | IllegalBlockSizeException | IOException | NoSuchAlgorithmException |
                         BadPaddingException | InvalidKeyException e) {
                    JOptionPane.showMessageDialog(this, "Could not AES decrypt selected file.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "No accepted algorithm selected.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

    private void handleEncryptBtnClicked() {
        if (algorithmSelector.getSelectedItem() ==  null) return;
        switch ((String) algorithmSelector.getSelectedItem()) {
            case "RSA":
                var publicKey = getPublicKey();
                if (publicKey == null) return;
                try {
                    new FileEncrypterDecrypter().rsaEncrypt(inputFileField.getText(), publicKey);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeyException |
                         IllegalBlockSizeException | BadPaddingException e) {
                    JOptionPane.showMessageDialog(this, "Could not RSA encrypt selected file.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                break;
            case "AES":
                var secret = getPrivateKey();
                try {
                    new FileEncrypterDecrypter().aesEncrypt(inputFileField.getText(), secret);
                } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                         BadPaddingException | InvalidKeyException e) {
                    JOptionPane.showMessageDialog(this, "Could not AES encrypt selected file.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "No accepted algorithm selected.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

    private Key getPrivateKey() {
        var passwordDialog = new PasswordDialog("Please provide password for key "+privateKeySelector.getSelectedItem()+".");
        passwordDialog.pack();
        passwordDialog.setVisible(true);
        var password = passwordDialog.getPassword();
        if (password == null) return null;
        try {
            return keyStore.getKey((String) privateKeySelector.getSelectedItem(), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            JOptionPane.showMessageDialog(this, "Could not open selected key.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate((String) publicKeySelector.getSelectedItem()).getPublicKey();
        } catch (KeyStoreException e) {
            JOptionPane.showMessageDialog(this, "Could not open selected key.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    private void handleSelectionChange() {
        if (algorithmSelector.getSelectedItem() == null) return;
        switch ((String) algorithmSelector.getSelectedItem()) {
            case "AES":
                publicKeySelector.setEnabled(false);
                privateKeyLabel.setText("Secret key");
                break;
            case "RSA":
                publicKeySelector.setEnabled(true);
                privateKeyLabel.setText("Private key");
                break;
        }
    }

    private void selectFile() {
        var fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        var res = fileChooser.showDialog(this, "Select");
        if (res == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.getSelectedFile();
            inputFileField.setText(file.toString());
            encryptButton.setEnabled(true);
            decryptButton.setEnabled(true);
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
            inputButton.setEnabled(true);
            privateKeySelector.setEnabled(true);
            publicKeySelector.setEnabled(true);
            algorithmSelector.setEnabled(true);


        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            JOptionPane.showMessageDialog(this, "Could not load selected keystore.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        var policyPath = Paths.get("./lab9/gui/java.policy");
        System.setProperty("java.security.policy", policyPath.toAbsolutePath().toString());
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        new MainFrame();
    }
}
