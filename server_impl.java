import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class server_impl extends UnicastRemoteObject implements server_iface {
    ArrayList<client_iface> clients;
    int amount;

    public server_impl() throws RemoteException {
        clients = new ArrayList<client_iface>();
    }

    public void send_message(String message) throws RemoteException {
        for (client_iface client : clients) {
            client.send(message);
        }
    }

    public void set_amount(int number) throws RemoteException {
        for (client_iface client : clients) {
            client.send_amount(number);
            this.amount = number;
        }
    }

    public int get_amount() throws RemoteException {
        return amount;
    }

    public void register_client(client_iface client) throws RemoteException {
        clients.add(client);
    }
}
