package NetworkManagement;

import UI.ServerAppUI;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ARPTable {

    private ArrayList<ARPNode> arpTable;
    private ServerAppUI appUI;

    public ARPTable(ServerAppUI appUI) {
        arpTable = new ArrayList<>();
        this.appUI = appUI;
        appUI.updateArpTable(getTableModel());
    }

    public boolean addDevice(ARPNode node) {
        // verify that the MAC address is not added
        for (ARPNode arpNode: arpTable) {
            if(node.getMac().equals(arpNode.getMac())) {
                return false;
            }
        }
        arpTable.add(node);
        appUI.updateArpTable(getTableModel());
        return true;
    }

    public DefaultTableModel getTableModel() {
        String[] columnTitles = {"Interface (IP)", "MAC Address"};
        String[] devices = new String[2];

        DefaultTableModel model = new DefaultTableModel(null, columnTitles);

        for(ARPNode node : arpTable) {
            devices[0] = node.getIp();
            devices[1] = node.getMac();
            System.out.println(devices[0] + " - " + devices[1]);
            model.addRow(devices);
        }

        return model;
    }

}
