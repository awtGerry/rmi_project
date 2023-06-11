import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.io.File;

public class ForkJoin extends RecursiveAction {
    private String[] dirname;
    private String path;
    private int start, end;
    private final int limit = 3;

    public ForkJoin(String[] dirname, String path) {
        this.dirname = dirname;
        this.path = path;
        this.start = 0;
        this.end = dirname.length;
    }

    protected void compute() {
        if (end > limit) {
            ForkJoinTask.invokeAll(fork_dir());
        } else {
            DirCreator dc = new DirCreator();
            dc.new_dir(dirname, path);
        }
    }

    private ArrayList<ForkJoin> fork_dir() {
        ArrayList<ForkJoin> subtask = new ArrayList<>();
        int mid = (end + 1) / 2;
        String[] s1 = Arrays.copyOfRange(dirname, start, mid);
        String[] s2 = Arrays.copyOfRange(dirname, mid, end);
        subtask.add(new ForkJoin(s1, path));
        subtask.add(new ForkJoin(s2, path));
        return subtask;
    }

}
