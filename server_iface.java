import java.rmi.Remote;
import java.rmi.RemoteException;

public interface server_iface extends Remote {
    public void send_message(String message) throws RemoteException;
    public void set_amount(int number) throws RemoteException;
    public int get_amount() throws RemoteException;
    public void register_client(client_iface client) throws RemoteException;
}
