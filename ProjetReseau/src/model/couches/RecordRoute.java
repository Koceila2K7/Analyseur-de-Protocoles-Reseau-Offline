package model.couches;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RecordRoute implements IPOption {
    public static String TAB = "\n\t\t";
    public static String JUSTTAB = "\t\t";
    public static String SPACE = "  ";
    public List<String> ipList;
    private String type;
    private String longeur;
    private String valeur;
    private String pointer;

    public RecordRoute(String type, String longeur, String valeur) {
        ipList = new ArrayList<>();
        this.type = type;
        this.longeur = longeur;
        this.pointer = valeur.substring(0, 2);
        this.valeur = valeur.substring(2);
        this.extractIpAddress();
    }

    private void extractIpAddress() {
        String adr = "";
        int cpt = 1;

        for (String hex : valeur.split("")) {
            adr += hex;
            if (adr.length() == 8) {
                ipList.add(IP.transformeIpAdress(adr));
                adr = "";
            }
            cpt++;
        }
    }

    private String emptedOrRecorded(String adr) {
        for (String hex : adr.split(""))
            if (!hex.equals("0")&&!hex.equals(".")) return "Recorded Route: ";
        return "Empty Route: ";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLongeur() {
        return longeur;
    }

    @Override
    public String getValeur() {
        return valeur;
    }

    @Override
    public String getName() {
        return "Record Route";
    }

    private BigInteger getIntValue(String hex) {
        return new BigInteger(hex, 16);
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();

        sb.append(TAB).append(SPACE)
                .append("Ip Option - Record Route (").append(getIntValue(longeur)).append(")")
                .append(TAB).append(SPACE).append(SPACE)
                .append("Type: ").append(getIntValue(type))
                .append(TAB).append(SPACE).append(SPACE)
                .append("Length: ").append(getIntValue(longeur))
                .append(TAB).append(SPACE).append(SPACE)
                .append("Pointer: ").append(getIntValue(pointer));

        for (String ip : ipList) {
            sb.append(TAB).append(SPACE).append(SPACE)
                    .append(emptedOrRecorded(ip)).append(ip);
        }
        return sb.toString();
    }
}
