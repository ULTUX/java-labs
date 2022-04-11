package pl.edu.pwr.lab6.client;

import pl.edu.pwr.lab6.libs.IClient;
import pl.edu.pwr.lab6.libs.IManager;
import pl.edu.pwr.lab6.libs.Order;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Client extends UnicastRemoteObject implements IClient {

    private JList<Order> orderList;
    private DefaultListModel<Order> listModel = new DefaultListModel<>();
    private Map<Integer, Order> orderMap = new HashMap<>();
    private JTextField hostField;
    private JTextField nameField;
    private JButton cancelSelectedButton;
    private JButton newOrderButton;
    private JTextField portField;
    private JButton connectButton;
    private JPanel mainPanel;
    private transient IManager manager;
    Order awaitingOrder;

    protected Client() throws RemoteException, MalformedURLException, NotBoundException {
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
            showSuccessMessage("Successfully cancelled order.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addOrderClicked() {
        var adText = JOptionPane.showInputDialog("Please provide advert text.");
        var displayPeriod = JOptionPane.showInputDialog("Please provide display period (in seconds).");
        var order = new Order(adText, Duration.ofSeconds(Long.parseLong(displayPeriod)), this);
        try {
            manager.placeOrder(order);
            awaitingOrder = order;
        } catch (RemoteException e) {
            showErrorMessage("Could not place an order.");
        }
    }

    private void connectClicked() {
        try {
            manager = (IManager) Naming.lookup("rmi://"+hostField.getText()+":"+portField.getText()+"/manager");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            showErrorMessage("Could not connect to rmi registry.");
        }
    }


    @Override
    public void setOrderId(int orderId) throws RemoteException {
        listModel.addElement(awaitingOrder);
        orderMap.put(orderId, awaitingOrder);
        Thread orderThread = new Thread(() -> {
            try {
                Thread.sleep(awaitingOrder.displayPeriod.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
                showErrorMessage("Could not initialize sleep on order awaiting thread.");
                Thread.currentThread().interrupt();
            }
            orderMap.remove(orderId);
            listModel.removeElement(awaitingOrder);
        });
        orderThread.start();
        showSuccessMessage("Successfully placed new order with id: "+orderId);
    }

    public void showSuccessMessage(String message){
        JOptionPane.showMessageDialog(mainPanel, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
