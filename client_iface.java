import java.rmi.Remote;
import java.rmi.RemoteException;

public interface client_iface extends Remote {
    public void send(String msg) throws RemoteException;
    public void send_amount(int number) throws RemoteException;
    public int get_amount() throws RemoteException;
}
