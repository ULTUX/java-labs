package pl.edu.pwr.lab6.billboard;

import pl.edu.pwr.lab6.libs.IBillboard;
import pl.edu.pwr.lab6.libs.Order;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Billboard extends UnicastRemoteObject implements IBillboard {

    private JTextField hostName;
    private JButton startButton;
    private JButton stopButton;
    private JSpinner capacitySpinner;
    private JTextField billboardName;
    private Map<Integer, Order> ads = new HashMap<>();
    Duration displayInterval = Duration.ofSeconds(2);

    protected Billboard() throws RemoteException {
    }


    @Override
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
        var order = new Order(advertText, displayPeriod);
        ads.put(orderId, order);
    }

    @Override
    public boolean removeAdvertisement(int orderId) throws RemoteException {
        var res = ads.remove(orderId);
        return res != null;
    }


    @Override
    public int[] getCapacity() throws RemoteException {
        return new int[]{(int) capacitySpinner.getValue(), ((int) capacitySpinner.getValue()) - ads.size()};
    }

    @Override
    public void setDisplayInterval(Duration displayInterval) throws RemoteException {
        this.displayInterval = displayInterval;
    }

    @Override
    public boolean start() throws RemoteException {
        return false;
    }

    @Override
    public boolean stop() throws RemoteException {
        return false;
    }

    @Override
    public String toString() {
        return billboardName.getText();
    }
}
