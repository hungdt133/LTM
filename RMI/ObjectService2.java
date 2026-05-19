package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectService2 extends Remote {
    public OrderTotal requestObject(String studentCode, String qCode)
            throws RemoteException;

    public boolean submitObject(String studentCode, String qCode,
                                OrderTotal object)
            throws RemoteException;
}