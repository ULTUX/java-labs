package pl.edu.pwr.lab6.libs;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

public class RMISSLServerSocketFactory implements RMIServerSocketFactory {


    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return SSLServerSocketFactory.getDefault().createServerSocket();
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else return obj != null && getClass() == obj.getClass();
    }
}
