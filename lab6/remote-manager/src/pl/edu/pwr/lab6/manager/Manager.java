package pl.edu.pwr.lab6.manager;

import pl.edu.pwr.lab6.libs.*;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.swing.*;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Manager extends UnicastRemoteObject implements IManager {

    private JPanel panel1;
    private JList<IBillboard> list1;
    private final DefaultListModel<IBillboard> listModel = new DefaultListModel<>();
    private final Map<Integer, IBillboard> billboardList = new HashMap<>();

    protected Manager() throws RemoteException {
        var path = Paths.get("./lab6/remote-manager/keystore");
        System.setProperty("javax.net.ssl.trustStore", path.toAbsolutePath().toString());
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
        System.setProperty("javax.net.ssl.keyStore", path.toAbsolutePath().toString());
        System.setProperty("javax.net.ssl.keyStorePassword", "passwd");

        Registry reg = LocateRegistry.createRegistry(1099, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, null, true));
        reg.rebind("manager", this);
        list1.setModel(listModel);
    }

    @Override
    public int bindBillboard(IBillboard billboard) throws RemoteException {
        int id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        billboardList.put(id, billboard);
        listModel.addElement(billboard);
        return id;
    }

    @Override
    public boolean unbindBillboard(int billboardId) throws RemoteException {
        try {
            billboardList.remove(billboardId);
            listModel.removeElement(billboardList.get(billboardId));
            return true;
        }
        catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    @Override
    public boolean placeOrder(Order order) throws RemoteException {
        var wasAdded = new AtomicBoolean(false);
        billboardList.forEach((id, iBillboard) -> {
            try {
                if (iBillboard.getCapacity()[1] > 0){
                    int orderId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
                    iBillboard.addAdvertisement(order.advertText, order.displayPeriod, orderId);
                    order.client.setOrderId(orderId);
                    wasAdded.set(true);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        if (!wasAdded.get()) order.client.setOrderId(-1);
        return wasAdded.get();
    }

    @Override
    public boolean withdrawOrder(int orderId) throws RemoteException {
        billboardList.forEach((integer, iBillboard) -> {
            try {
                iBillboard.removeAdvertisement(orderId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        return false;
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
