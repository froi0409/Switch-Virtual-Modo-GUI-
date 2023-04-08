package NetworkManagement;

import javax.swing.table.DefaultTableModel;
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

    public DefaultTableModel getTableModel() {
        String[] columnTitles = {"Interface (IP)", "MAC Address"};
        String[] devices = new String[2];

        DefaultTableModel model = new DefaultTableModel();

        for(ARPNode node : arpTable) {
            devices[0] = node.getIp();
            devices[1] = node.getMac();

            model.addRow(devices);
        }

        return model;
    }

}
