package pl.edu.pwr.lab9;

import pl.edu.pwr.lab9.lib.FileEncrypterDecrypter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private JLabel privateKeyLabel;

    private KeyStore keyStore;

    private File keyStoreLocation;

    public MainFrame() throws HeadlessException {

        super("File encryption/decryption tool");
        System.out.println("In java11");
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
        if (algorithmSelector.getSelectedItem() == null) return;
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
        var passwordDialog = new PasswordDialog("Please provide password for key " + privateKeySelector.getSelectedItem() + ".");
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
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        new MainFrame();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setEnabled(true);
        mainPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel1.setBorder(BorderFactory.createTitledBorder(null, "Transformation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("File");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        inputFileField = new JTextField();
        inputFileField.setEditable(false);
        panel1.add(inputFileField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        inputButton = new JButton();
        inputButton.setEnabled(false);
        inputButton.setText("Select");
        panel1.add(inputButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Algorithm");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        algorithmSelector = new JComboBox();
        algorithmSelector.setEnabled(false);
        panel1.add(algorithmSelector, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        privateKeyLabel = new JLabel();
        privateKeyLabel.setText("Private key");
        panel1.add(privateKeyLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label3 = new JLabel();
        label3.setText("Public key");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        encryptButton = new JButton();
        encryptButton.setEnabled(false);
        encryptButton.setText("Encrypt");
        panel1.add(encryptButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decryptButton = new JButton();
        decryptButton.setEnabled(false);
        decryptButton.setText("Decrypt");
        panel1.add(decryptButton, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        privateKeySelector = new JComboBox();
        privateKeySelector.setEnabled(false);
        panel1.add(privateKeySelector, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        publicKeySelector = new JComboBox();
        publicKeySelector.setEnabled(false);
        panel1.add(publicKeySelector, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Key store options", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("Key store location");
        panel2.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        keyStoreLocField = new JTextField();
        keyStoreLocField.setEditable(false);
        panel2.add(keyStoreLocField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectKeyStoreButton = new JButton();
        selectKeyStoreButton.setText("Select");
        panel2.add(selectKeyStoreButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label5 = new JLabel();
        label5.setText("Key store password");
        panel2.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        passwordField1 = new JPasswordField();
        panel2.add(passwordField1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loadKeyStoreButton = new JButton();
        loadKeyStoreButton.setText("Load");
        panel2.add(loadKeyStoreButton, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}

