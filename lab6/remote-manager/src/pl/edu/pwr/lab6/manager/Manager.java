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
import java.util.ArrayList;
import java.util.List;

public class Manager extends UnicastRemoteObject implements IManager {

    private JPanel panel1;
    private JList list1;
    List<IBillboard> billboardList = new ArrayList<>();

    protected Manager() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("//localhost:1099/manager", this);

    }

    @Override
    public int bindBillboard(IBillboard billboard) throws RemoteException {
        billboardList.add(billboard);
        return billboardList.indexOf(billboard);
    }

    @Override
    public boolean unbindBillboard(int billboardId) throws RemoteException {
        try {
            billboardList.remove(billboardId);
            return true;
        }
        catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    @Override
    public boolean placeOrder(Order order) throws RemoteException {
        System.out.println("GOT ORDER"+ order.toString());
        order.client.setOrderId(45);
        return false;
    }

    @Override
    public boolean withdrawOrder(int orderId) throws RemoteException {
        return false;
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
