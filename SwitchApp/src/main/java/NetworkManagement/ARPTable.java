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
                if(!arpNode.isEnabled() && arpNode.getIp().equals(node.getIp())) {
                    arpNode.setEnabled(true);
                    appUI.updateArpTable(getTableModel());
                    return true;
                }
                return false;
            }
        }
        arpTable.add(node);
        node.setEnabled(true);
        appUI.updateArpTable(getTableModel());
        return true;
    }

    public boolean disconnectDevice(String mac) {
        for(ARPNode arpNode : arpTable) {
            if(arpNode.getMac().equals(mac)) {
                arpNode.setEnabled(false);
                appUI.updateArpTable(getTableModel());
                return true;
            }
        }
        return false;
    }

    public DefaultTableModel getTableModel() {
        String[] columnTitles = {"Interface (IP)", "MAC Address", "Enabled"};
        String[] devices = new String[3];

        DefaultTableModel model = new DefaultTableModel(null, columnTitles);

        for(ARPNode node : arpTable) {
            devices[0] = node.getIp();
            devices[1] = node.getMac();
            devices[2] = node.getEnabled();
            System.out.println(devices[0] + " - " + devices[1]);
            model.addRow(devices);
        }

        return model;
    }

    public ARPNode getDevice(String mac) {
        for(ARPNode node : arpTable) {
            if(node.getMac().equals(mac) && node.isEnabled()) return node;
        }
        return null;
    }

    public ArrayList<ARPNode> getArpTable() {
        return arpTable;
    }
}
