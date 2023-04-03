package NetworkManagement;

import java.io.Serializable;

public class NetworkFrame implements Serializable {
    private String text;

    public NetworkFrame(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
