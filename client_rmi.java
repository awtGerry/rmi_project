import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class client_rmi {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = in.nextLine();
            Registry reg = LocateRegistry.getRegistry(server_rmi.HOST, server_rmi.PORT);
            System.out.println();

            server_iface server = (server_iface) reg.lookup("rmi_server");
            new Thread(new client_impl(name, server)).start();

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
