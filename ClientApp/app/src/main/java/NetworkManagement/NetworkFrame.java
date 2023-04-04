package NetworkManagement;

import java.io.Serializable;


public class NetworkFrame implements Serializable {
    private String message;
    private int type;

    public NetworkFrame(int type) {
        this.type = type;
        if (type == 1) {
            this.message = "Agregando Dispositivo a la Tabla ARP";
        } else {
            this.message = "Buscando Dispositivos en la tabla ARP";
        }
    }
    public NetworkFrame(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
