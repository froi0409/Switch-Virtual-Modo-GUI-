package NetworkManagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressManager {


    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");
    public boolean isMacAdress(String[] macAddress) {
        if(macAddress.length > 6) {
            return false; // MAC Address only has 6 hexadecimal digits
        } else {
            // Check if each value is hexadecimal
            for (String hex: macAddress) {
                final Matcher matcher = HEXADECIMAL_PATTERN.matcher(hex);
                if(!matcher.matches()) {
                    return false;
                }
            }
        }

        return true;
    }

    public String getMacAddress(String[] macAddress) {
        String macAux = "";
        for (String mac: macAddress) {
            macAux += mac + ":";
        }
        macAux = macAux.substring(0, macAux.length()-1);
        return macAux;
    }
}
