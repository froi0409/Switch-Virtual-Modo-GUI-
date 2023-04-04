package NetworkManagement;

import java.io.Serializable;


public class NetworkFrame implements Serializable {
    private String message;
    private String macOrigin;
    private String macDestiny;
    private int type;

    public NetworkFrame(int type) {
        this.type = type;
        if (type == 1) {
            this.message = "Agregando Dispositivo a la Tabla ARP";
        } else {
            this.message = "Buscando Dispositivos en la tabla ARP";
        }
    }
    public NetworkFrame(String message, String macOrigin, String macDestiny) {
        this.message = message;
        this.macOrigin = macOrigin;
        this.macDestiny = macDestiny;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public String getMacOrigin() {
        return macOrigin;
    }

    public String getMacDestiny() {
        return macDestiny;
    }
}
