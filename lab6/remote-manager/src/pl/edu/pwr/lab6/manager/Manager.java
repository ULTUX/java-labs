package pl.edu.pwr.lab6.manager;

import pl.edu.pwr.lab6.libs.IBillboard;
import pl.edu.pwr.lab6.libs.IManager;
import pl.edu.pwr.lab6.libs.Order;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Manager extends UnicastRemoteObject implements IManager {

    private JPanel panel1;
    private JList<IBillboard> list1;
    private DefaultListModel<IBillboard> listModel = new DefaultListModel<>();
    Map<Integer, IBillboard> billboardList = new HashMap();

    protected Manager() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("//localhost:1099/manager", this);
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
        System.out.println("Place order called for order");
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
