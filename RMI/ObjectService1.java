package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;

public interface ObjectService1 extends Remote {
    public Serializable requestObject(String studentCode, String qCode) throws RemoteException;
    public void submitObject(String studentCode, String qCode, Serializable obj) throws RemoteException;
}