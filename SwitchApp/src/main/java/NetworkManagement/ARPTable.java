package NetworkManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ARPTable {
    private HashMap<String, String> arpTable;
    public ARPTable () {
        arpTable = new HashMap<>();
    }

    public void insertDevice(String mac, String ip) {
        arpTable.put(mac, ip);
    }
}
