package NetworkManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ARPTable {

    private ArrayList<ARPNode> arpTable;

    public ARPTable() {
        arpTable = new ArrayList<>();
    }

    public boolean addDevice(ARPNode node) {
        // verify that the MAC address is not added
        for (ARPNode arpNode: arpTable) {
            if(node.getMac().equals(arpNode.getMac())) {
                return false;
            }
        }
        arpTable.add(node);
        return true;
    }

}
