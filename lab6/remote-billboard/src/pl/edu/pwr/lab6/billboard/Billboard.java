package pl.edu.pwr.lab6.billboard;

import pl.edu.pwr.lab6.libs.IBillboard;
import pl.edu.pwr.lab6.libs.IManager;
import pl.edu.pwr.lab6.libs.Order;
import pl.edu.pwr.lab6.libs.UIUtils;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Billboard extends UnicastRemoteObject implements IBillboard {

    private JTextField hostField;
    private JButton startButton;
    private JButton stopButton;
    private JSpinner capacitySpinner;
    private JTextField billboardName;
    private JButton connectButton;
    private JTextField portField;
    private JPanel mainPanel;
    private JTextArea adDisplay;
    private final Map<Integer, Order> ads = new ConcurrentHashMap<>();
    private final Map<Order, Long> adDisplayTime = new ConcurrentHashMap<>();
    Duration displayInterval = Duration.ofSeconds(2);
    private transient IManager manager;
    private final UIUtils uiUtils;
    private Thread adCycle;

    protected Billboard() throws RemoteException {
        uiUtils = new UIUtils(mainPanel);
        connectButton.addActionListener(e -> connectClicked());
        startButton.addActionListener(e -> {
            try {
                start();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
        stopButton.addActionListener(e -> {
            try {
                stop();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    public Container getPanel1() {
        return mainPanel;
    }

    private class AdCycleTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                ads.forEach((integer, order) -> {
                    adDisplay.setText(order.advertText);
                    try {
                        Thread.sleep(displayInterval.toMillis());
                        adDisplay.setText("");
                        long remainingAdDisplayTime;
                        if (adDisplayTime.containsKey(order)){
                            remainingAdDisplayTime = adDisplayTime.get(order) - displayInterval.toSeconds();
                        }
                        else {
                            remainingAdDisplayTime = order.displayPeriod.toSeconds() - displayInterval.toSeconds();
                        }
                        if (remainingAdDisplayTime < 0){
                            ads.remove(integer);
                            adDisplayTime.remove(order);
                            System.out.println("Ad cycle finished, removing");
                        }
                        else {
                            adDisplayTime.put(order, remainingAdDisplayTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                });
            }
        }
    }

    private void connectClicked() {
        try {
            var path = Paths.get("./lab6/remote-billboard/keystore");
            System.out.println(path.toAbsolutePath().toString());
            System.setProperty("javax.net.ssl.trustStore", path.toAbsolutePath().toString());
            System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
            System.setProperty("javax.net.ssl.keyStore", path.toAbsolutePath().toString());
            System.setProperty("javax.net.ssl.keyStorePassword", "passwd");
            Registry reg = LocateRegistry.getRegistry("localhost", 1099, new SslRMIClientSocketFactory());
            manager = (IManager) reg.lookup("manager");
            manager.bindBillboard(this);
        } catch (NotBoundException | RemoteException e) {
            uiUtils.showErrorMessage("Could not connect to rmi registry.");
            e.printStackTrace();
        }
    }


    @Override
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
        System.out.println("Add advert called for text:"+ advertText);
        var order = new Order(advertText, displayPeriod);
        if (getCapacity()[1] > 0){
            ads.put(orderId, order);
            return true;
        }
        return false;
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
        if (adCycle != null && adCycle.isAlive()) return false;
        adCycle = new Thread(new AdCycleTask());
        adCycle.start();
        return true;
    }

    @Override
    public boolean stop() throws RemoteException {
        if (adCycle == null || !adCycle.isAlive()) return false;
        adCycle.stop();
        return true;
    }

    @Override
    public String toString() {
        return billboardName.getText();
    }
}
