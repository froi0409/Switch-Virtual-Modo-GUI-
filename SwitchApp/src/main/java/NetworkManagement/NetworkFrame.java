package NetworkManagement;

import java.io.Serializable;
import java.util.zip.CRC32;


public class NetworkFrame implements Serializable {
    private String message;
    private String macOrigin;
    private String macDestiny;
    private String crc;
    /*
     * 1. Connect Device
     * 2. Own Client Message
     * 3. Client Message
     * 4. Switch Unicast Message
     * 5. Add Device Error
     * 6. Cannot find device error
     * */
    private int type;

    public NetworkFrame(int type, String macOrigin) {
        this.type = type;
        this.macOrigin = macOrigin;
        if (type == 1) {
            this.message = "Agregando Dispositivo a la Tabla ARP";
            this.crc = generateCrc(message);
        }
        /*else {
            this.message = "Buscando Dispositivos en la tabla ARP";
        }*/
    }
    public NetworkFrame(String message, String macOrigin, String macDestiny) {
        this.message = message;
        this.macOrigin = macOrigin;
        this.macDestiny = macDestiny;
        this.type = 3;
        this.crc = generateCrc(message);
    }

    public NetworkFrame(int type, String message, String macOrigin, String macDestiny) {
        this.message = message;
        this.macOrigin = macOrigin;
        this.macDestiny = macDestiny;
        this.type = type;
        this.crc = generateCrc(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.crc = generateCrc(message);
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

    public String renderMessage() {
        String text = (type == 3) ? ("Mensaje Cliente\nOrigen: " + macOrigin + "\n") : "Mensaje Switch\n";
        text += "Destino: " + macDestiny + "\n";
        text += "CRC: " + getCrc() + "\n";
        text += message + "\n\n";
        return text;
    }

    private String generateCrc(String data) {
        CRC32 crc = new CRC32();
        crc.update(data.getBytes());
        return String.valueOf(crc.getValue());
    }

    public String getCrc() {
        return crc;
    }
}
