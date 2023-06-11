import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server_rmi {
    // public static final String HOST = "192.168.100.8";
    public static final String HOST = "192.168.100.109";
    public static final int PORT = 1099;
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", HOST);
            Registry reg = LocateRegistry.createRegistry(PORT);
            reg.rebind("rmi_server", new server_impl());
            System.out.println("Host: " + HOST + "\nPort: " + PORT);
            System.out.println("Server is ready...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
