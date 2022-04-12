package pl.edu.pwr.lab6.client;

import pl.edu.pwr.lab6.libs.*;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Client extends UnicastRemoteObject implements IClient {

    private JList<Order> orderList;
    private final DefaultListModel<Order> listModel = new DefaultListModel<>();
    private final Map<Integer, Order> orderMap = new HashMap<>();
    private JTextField hostField;
    private JTextField nameField;
    private JButton cancelSelectedButton;
    private JButton newOrderButton;
    private JTextField portField;
    private JButton connectButton;
    private JPanel mainPanel;
    private transient IManager manager;
    private transient final UIUtils uiUtils;
    private Order awaitingOrder;

    protected Client() throws RemoteException, MalformedURLException, NotBoundException {
        uiUtils = new UIUtils(null);
        connectButton.addActionListener(e -> connectClicked());

        orderList.setModel(listModel);
        newOrderButton.addActionListener(e -> addOrderClicked());
        cancelSelectedButton.addActionListener(e -> cancelSelectedClicked());
    }

    private void cancelSelectedClicked() {
        var order = orderList.getSelectedValue();
        try {
            var entry = orderMap.entrySet().stream()
                    .filter(entryF -> entryF.getValue().equals(order))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("Selected order does not exist in order map, this should not happen."));
            manager.withdrawOrder(entry.getKey());
            orderMap.remove(entry.getKey());
            listModel.removeElement(entry.getValue());
            uiUtils.showSuccessMessage("Successfully cancelled order.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addOrderClicked() {
        var adText = JOptionPane.showInputDialog("Please provide advert text.");
        var displayPeriod = JOptionPane.showInputDialog("Please provide display period (in seconds).");
        var order = new Order(adText, Duration.ofSeconds(Long.parseLong(displayPeriod)), this);
        try {
            awaitingOrder = order;
            manager.placeOrder(order);
        } catch (RemoteException e) {
            uiUtils.showErrorMessage("Could not place an order.");
        }
    }

    private void connectClicked() {
        try {
            var path = Paths.get("./lab6/remote-client/keystore");
            System.setProperty("javax.net.ssl.trustStore", path.toAbsolutePath().toString());
            System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
            System.setProperty("javax.net.ssl.keyStore", path.toAbsolutePath().toString());
            System.setProperty("javax.net.ssl.keyStorePassword", "passwd");
            Registry reg = LocateRegistry.getRegistry(hostField.getText(), Integer.parseInt(portField.getText()), new SslRMIClientSocketFactory());
            manager = (IManager) reg.lookup("manager");
        } catch (NotBoundException | RemoteException  e) {
            e.printStackTrace();
            uiUtils.showErrorMessage("Could not connect to rmi registry.");
        }
    }


    @Override
    public void setOrderId(int orderId) throws RemoteException {
        if (orderId == -1){
            uiUtils.showErrorMessage("Could not create new order.");
            return;
        }
        listModel.addElement(awaitingOrder);
        orderMap.put(orderId, awaitingOrder);
        Thread orderThread = new Thread(() -> {
            try {
                Thread.sleep(awaitingOrder.displayPeriod.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
                uiUtils.showErrorMessage("Could not initialize sleep on order awaiting thread.");
                Thread.currentThread().interrupt();
            }
            orderMap.remove(orderId);
            listModel.removeElement(awaitingOrder);
        });
        orderThread.start();
        uiUtils.showSuccessMessage("Successfully placed new order with id: "+orderId);
    }

    public Container getPanel1() {
        return mainPanel;
    }
}
