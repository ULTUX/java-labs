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

public class Client extends UnicastRemoteObject implements IClient {

    private JList orderList;
    private JButton newOrderButton;

    protected Client() throws RemoteException, MalformedURLException, NotBoundException {
        IManager manager = (IManager) Naming.lookup("rmi://localhost:1099/manager");
        manager.placeOrder(new Order("Test", Duration.ofMillis(1000), this));
    }

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        new Client();
    }

    @Override
    public void setOrderId(int orderId) throws RemoteException {
        System.out.println("jgfjhgfjhf");
    }

}
