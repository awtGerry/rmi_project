import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class client_impl extends UnicastRemoteObject implements client_iface, Runnable {
    server_iface server;
    String name;
    ArrayList<String> msgs = new ArrayList<String>();

    public client_impl(String name, server_iface server) throws RemoteException {
        this.name = name;
        this.server = server;
        server.register_client(this);
    }

    public void send(String msg) throws RemoteException {
        msgs.add(msg);
        System.out.println(msg);
    }

    public void send_amount(int number) throws RemoteException {
        System.out.println("El servidor ha enviado: " + number);
    }

    public int get_amount() throws RemoteException {
        return server.get_amount();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        String msg;
        long start, end;
        String[] dirs;
        while (true) {
            try {
                msg = sc.nextLine();
                if (isInteger(msg)) {
                    server.set_amount(Integer.parseInt(msg));
                }
                else {
                    switch (msg) {
                        case "normal":
                            dirs = new String[get_amount()];
                            create_names("secuencial", dirs);
                            start = System.currentTimeMillis();
                            DirCreator dc = new DirCreator();
                            dc.new_dir(dirs, "");
                            end = System.currentTimeMillis();
                            System.out.println("normal time: " + (end - start) + "ms");
                            break;
                        case "fork":
                            ForkJoinPool pool = new ForkJoinPool();
                            dirs = new String[get_amount()];
                            create_names("fork", dirs);
                            ForkJoin fj = new ForkJoin(dirs, "");
                            start = System.currentTimeMillis();
                            pool.invoke(fj);
                            end = System.currentTimeMillis();
                            System.out.println("fork time: " + (end - start) + "ms");
                            break;
                        case "executor":
                            ExecutorService executor = Executors.newFixedThreadPool(10);
                            dirs = new String[get_amount()];
                            create_names("executor", dirs);
                            start = System.currentTimeMillis();
                            executor.execute(new ExecService(dirs, ""));
                            end = System.currentTimeMillis();
                            System.out.println("executor time: " + (end - start) + "ms");
                            break;
                        case "delete":
                            DirCreator dc2 = new DirCreator();
                            dc2.del_dir(new File(DirCreator.PATH));
                            new File(DirCreator.PATH).mkdir();
                            System.out.println("Default directory deleted and created again");
                            break;
                        default:
                            server.send_message(name + ": " + msg + ": Command not found");
                            break;
                    }
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void create_names(String name, String[] arr) {
        for (int i=0; i<arr.length; i+=1) {
            arr[i] = (name + (i+1));
        }
    }

}
