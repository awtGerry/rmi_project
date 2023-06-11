import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import javax.swing.*;

public class GUI {
    private static server_iface server;
    private JFrame frame = new JFrame();

    private JTextField path_input = new JTextField();
    private JTextField dirname_input = new JTextField();
    private JLabel txt_dirname = new JLabel();
    private JLabel txt_path = new JLabel();
    private JLabel time_normal = new JLabel();
    private JLabel time_fork = new JLabel();
    private JLabel time_executor = new JLabel();
    private JLabel msg = new JLabel();

    private JButton btn_send = new JButton();
    private JButton btn_limpiar = new JButton();
    private JButton btn_secuential = new JButton();
    private JButton btn_fork = new JButton();
    private JButton btn_executor = new JButton();

    private long time1, time2;
    private String[] dirs;

    public GUI() { // inicializar gui
        // BUTTONS

        btn_send = custom_button(btn_send, "Enviar");
        btn_send.addActionListener((ActionEvent e) -> {
            try {
                server.set_amount(Integer.parseInt(dirname_input.getText().toString()));
            } catch (RemoteException err) {
                err.printStackTrace();
            }
        });

        btn_limpiar = custom_button(btn_limpiar, "Limpiar");
        btn_limpiar.addActionListener((ActionEvent e) -> {
            dirname_input.setText("");
            path_input.setText("");
            DirCreator dc = new DirCreator();
            dc.del_dir(new File(DirCreator.PATH));
            new File(DirCreator.PATH).mkdir();
        });

        btn_secuential = custom_button(btn_secuential, "Normal");
        btn_secuential.addActionListener((ActionEvent e) -> {
            secuential_pressed();
        });

        btn_fork = custom_button(btn_fork, "Fork Join");
        btn_fork.addActionListener((ActionEvent e) -> {
            fork_pressed();
        });
        btn_executor = custom_button(btn_executor, "Executor");
        btn_executor.addActionListener((ActionEvent e) -> {
            executor_pressed();
        });

        txt_dirname = custom_txt(txt_dirname, "Ingresar cantidad de carpetas a crear");
        txt_path = custom_txt(txt_path, "Ruta (dejar vacio para usar una por defecto)");
        time_normal = custom_txt(time_normal, "Tiempo: ");
        time_fork = custom_txt(time_fork, "Tiempo: ");
        time_executor = custom_txt(time_executor, "Tiempo: ");
        msg = custom_txt(msg, ".");

        path_input.setPreferredSize(new Dimension(200, 30));
        path_input.setFont(new Font("Calibri", Font.PLAIN, 14));
        dirname_input.setPreferredSize(new Dimension(200, 30));
        dirname_input.setFont(new Font("Calibri", Font.PLAIN, 14));

        // FRAME & GROUP LAYOUT
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout ly = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(ly);
        frame.setResizable(false);
        gui_groups(ly); // add styles.

        frame.setVisible(true);
        frame.pack();
    }

    /* BUTTONS */
    private void secuential_pressed() {
        try {
            dirs = new String[server.get_amount()];
            create_names("secuencial", dirs);
            DirCreator dc = new DirCreator();
            time1 = System.currentTimeMillis();
            dc.new_dir(
                dirs,
                path_input.getText().toString()
            );
            time2 = System.currentTimeMillis();
            msg = custom_txt(msg, "Secuencial completado.");
            time_normal.setText("Tiempo normal: " + (time2-time1) + " ms.");
        } catch (RemoteException err) {
            err.printStackTrace();
        }
    }

    private void fork_pressed() {
        try {
            ForkJoinPool pool = new ForkJoinPool();
            dirs = new String[server.get_amount()];
            create_names("fork", dirs);
            ForkJoin fork = new ForkJoin(dirs, path_input.getText().toString());
            time1 = System.currentTimeMillis();
            pool.invoke(fork);
            time2 = System.currentTimeMillis();
            msg = custom_txt(msg, "Fork Join completado");
            time_fork.setText("Tiempo Fork: " + (time2-time1) + " ms.");
        } catch (RemoteException err) {
            err.printStackTrace();
        }
    }

    private void executor_pressed() {
        try {
            ExecutorService exec = Executors.newFixedThreadPool(10);
            dirs = new String[server.get_amount()];
            create_names("exec", dirs);
            time1 = System.currentTimeMillis();
            exec.execute(new ExecService(dirs, path_input.getText().toString()));
            time2 = System.currentTimeMillis();
            msg = custom_txt(msg, "Executor Service completado");
            time_executor.setText("Tiempo Executor: " + (time2-time1) + " ms.");
        } catch (RemoteException err) {
            err.printStackTrace();
        }
    }

    /* GUI METHODS */
    private JButton custom_button(JButton button, String txt) {
        button.setText(txt);
        button.setPreferredSize(new Dimension(90, 23));
        button.setForeground(new Color(219, 226, 239));
        button.setBackground(new Color(50, 130, 184));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("Calibri", Font.PLAIN, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 23));

        return button;
    }

    private JLabel custom_txt(JLabel label, String txt) {
        label.setText(txt);
        label.setFont(new Font("Calibri", Font.PLAIN, 14));
        label.setForeground(Color.DARK_GRAY);
        label.setOpaque(true);
        return label;
    }

    private void create_names(String name, String[] arr) {
        for (int i=0; i<arr.length; i+=1) {
            arr[i] = (name + (i+1));
        }
    }

    private void gui_groups(GroupLayout ly) { // clean up
        // Horizontal group
        ly.setHorizontalGroup(
            ly.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(ly.createSequentialGroup()
                .addContainerGap()
                .addGroup(ly.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                    // INPUTS
                    .addGroup(ly.createSequentialGroup()
                        .addComponent(txt_dirname)
                        .addGap(15)
                        .addComponent(dirname_input)
                        .addGap(15)
                        .addComponent(btn_send)
                        .addGap(15)
                    )
                    .addGroup(ly.createSequentialGroup()
                        .addComponent(txt_path)
                        .addGap(21)
                        .addComponent(path_input)
                        .addGap(113)
                    )

                    // BUTTONS
                    .addGroup(ly.createSequentialGroup()
                        .addComponent(btn_secuential)
                        .addGap(20)
                        .addComponent(btn_fork)
                        .addGap(20)
                        .addComponent(btn_executor)
                        .addGap(20)
                        .addComponent(btn_limpiar)
                    )

                    // TIMES
                    .addGroup(ly.createSequentialGroup()
                        .addComponent(time_normal)
                        .addGap(20)
                        .addComponent(time_fork)
                        .addGap(20)
                        .addComponent(time_executor)
                    )

                    // RESPONSE
                    .addComponent(msg)

                )
            )
        );

        // Vertical group
        ly.setVerticalGroup(
            ly.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(ly.createSequentialGroup()
                .addContainerGap()
                // INPUTS
                .addGroup(ly.createParallelGroup(GroupLayout.Alignment.BASELINE) // aliniar
                    .addComponent(txt_dirname)
                    .addComponent(dirname_input)
                    .addComponent(btn_send)
                )
                .addGap(15)
                .addGroup(ly.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_path)
                    .addComponent(path_input)
                )

                // BUTTONS
                .addGap(15)
                .addGroup(ly.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_secuential)
                    .addComponent(btn_fork)
                    .addComponent(btn_executor)
                    .addComponent(btn_limpiar)
                )

                // TIMES
                .addGap(15)
                .addGroup(ly.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(time_normal)
                    .addComponent(time_fork)
                    .addComponent(time_executor)
                )

                // RESPONSE
                .addGap(15)
                .addComponent(msg)

            )
        );
    } // end of gui_groups

    /* MAIN */
    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Input your name:");
        GUI gui = new GUI();
        try {
            Registry registry = LocateRegistry.getRegistry(server_rmi.HOST, server_rmi.PORT);
            server = (server_iface) registry.lookup("rmi_server");
            new Thread(new client_impl(name, server)).start();
        } catch (HeadlessException | NotBoundException | RemoteException err) {
            err.printStackTrace();
        }
    }

}
