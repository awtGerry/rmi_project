import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ExecService implements Runnable {
    private String[] dirname;
    private String path;

    public ExecService(String[] dirname, String path) {
        this.dirname = dirname;
        this.path = path;
    }

    public void run() {
        DirCreator dc = new DirCreator();
        dc.new_dir(dirname, path);
    }
}
