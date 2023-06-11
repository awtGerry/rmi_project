import java.io.File;

public class DirCreator {
    private static final String VERDE = "\u001B[32m";
    private static final String ROJO = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static final String PATH = "/home/gerry/awesometimes/learning/java/rmi_project/dirs";

    public DirCreator() {}

    public void new_dir(String[] dirname, String path) {
        if (path.isEmpty()) { // if path is empty use the default one
            // path = "/home/gerry/Documents/ceti/5sem/paralela/3Parcial/rmi_project/dirs";
            path = PATH;
        }
        for (int i=0; i<dirname.length; i+=1) {
            File file = new File(path + "/" + dirname[i]);
            if (file.mkdir() == true) {
                System.out.println(VERDE + "Directorio " + dirname[i] + " creado en:" + path + RESET);
            } else {
                System.out.println(ROJO + "Error: No se pudo crear el directorio " + dirname[i] +
                    " Asegurarse que la ruta exista o no haya un directorio con el mismo nombre." + RESET);
            }
        }
    }

    public boolean del_dir(File filename) {
        if (filename.isDirectory()) {
            File[] files = filename.listFiles();
            for (File file : files) {
                del_dir(file);
            }
        }
        return filename.delete();
    }

}
