package NetworkManagement;

public class ARPNode {
    private String ip;
    private String mac;
    private boolean enabled;

    public ARPNode(String ip, String mac, boolean enabled) {
        this.ip = ip;
        this.mac = mac;
        this.enabled = enabled;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
