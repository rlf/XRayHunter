package dk.lockfuglsang.xrayhunter.coreprotect;

import java.util.List;

/**
 * Callback for asynchronously calls to CoreProtect backend.
 */
public abstract class Callback implements Runnable {
    private List<String[]> data;

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }
}
