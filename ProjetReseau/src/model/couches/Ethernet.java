package model.couches;

import exceptions.TrameVideException;
import model.exceptions.DataNoValide;

public class Ethernet implements Comparable<Ethernet>, IToFile {
    private int numLigne;
    public static String TAB = "\n\t\t";

    public static String getType(String type) {
        if (type.equals("0800")) return "( IP )";
        if (type.equals("0806")) return "( ARP )";
        if (type.equals("0835")) return "( RARP )";
        return "none";
    }

    private String macDestination;
    private String macSource;
    private String protocle;
    private IP data = null;

    public Ethernet(String[] trame, int numLigne) throws TrameVideException, DataNoValide {
        if (trame == null)
            throw new TrameVideException("La trame passé en paramètre est vide");
        this.numLigne = numLigne;
        this.macDestination = this.extractMacDestination(trame);
        this.macSource = this.extractMacSource(trame);
        this.protocle = this.extractProtocole(trame);
        if (protocle.equals("0800")) this.data = this.extractIPPaquet(trame);
    }


    private String extractMacDestination(String[] trame) {
        StringBuilder stringBuilder = new StringBuilder(12);
        int cpt = 0;
        for (int i = 0; i < 6; i++)
            stringBuilder.append(trame[cpt++]);
        return stringBuilder.toString();
    }

    private String extractMacSource(String[] trame) {
        StringBuilder stringBuilder = new StringBuilder(12);
        int cpt = 7;
        for (int i = 0; i < 6; i++)
            stringBuilder.append(trame[cpt++]);
        return stringBuilder.toString();
    }

    private String extractProtocole(String[] trame) {
        StringBuilder stringBuilder = new StringBuilder(4);
        stringBuilder.append(trame[12]);
        stringBuilder.append(trame[13]);
        return stringBuilder.toString();
    }

    private IP extractIPPaquet(String[] trame) throws DataNoValide {
        return new IP(trame);
    }

    public String getMacDestination() {
        StringBuilder sb = new StringBuilder();
        sb.append(macDestination.substring(0, 2));
        sb.append(":");
        sb.append(macDestination.substring(2, 4));
        sb.append(":");
        sb.append(macDestination.substring(4, 6));
        sb.append(":");
        sb.append(macDestination.substring(6, 8));
        sb.append(":");
        sb.append(macDestination.substring(8, 10));
        sb.append(":");
        sb.append(macDestination.substring(10, 12));
        return sb.toString();
    }

    public String getMacSource() {
        StringBuilder sb = new StringBuilder();
        sb.append(macSource.substring(0, 2));
        sb.append(":");
        sb.append(macSource.substring(2, 4));
        sb.append(":");
        sb.append(macSource.substring(4, 6));
        sb.append(":");
        sb.append(macSource.substring(6, 8));
        sb.append(":");
        sb.append(macSource.substring(8, 10));
        sb.append(":");
        sb.append(macSource.substring(10, 12));
        return sb.toString();
    }

    public String getProtocle() {
        return protocle;
    }

    public IP getData() {
        return data;
    }


    public int getNumLigne() {
        return numLigne;
    }

    @Override
    public String toString() {
        return "Ethernet:" +
                "Commence à la ligne: " + numLigne +
                " Protocle:" + getType(protocle) + '\'' +
                " Data->" + data;
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTrame commence à la ligne :" + this.numLigne);
        sb.append("\nEthernet II :");
        sb.append(TAB + "Destination : " + this.getMacDestination());
        sb.append(TAB + "Source : " + this.getMacSource());
        sb.append(TAB + "Type : " + this.getType(getProtocle()) + "(0x" + getProtocle() + ")");
        if (data != null) sb.append(data.fromDataToFile());
        return sb.toString();
    }

    @Override
    public int compareTo(Ethernet o) {
        return Integer.compare(this.getNumLigne(), o.getNumLigne());
    }
}
