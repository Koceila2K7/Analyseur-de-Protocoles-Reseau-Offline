package model.couches;

import javafx.scene.control.Control;
import model.exceptions.DataNoValide;
import sample.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class IP implements IToFile {
    public static String TAB = "\n\t\t";
    private int index = 14;

    private String version;
    private String headerLength;
    private String tos;
    private String TotalLength;
    private String ID;
    private String R, DF, DM, fragmentOffset;
    private String ttl;
    private String protocol;
    private String headerCheksum;
    private String srcIP;
    private String dstIP;

    private List<IPOption> options;
    private TCP data;


    public IP(String[] trame) throws DataNoValide {

        this.extractVersionAndIhl(trame);
        this.extractTos(trame);
        this.extactTotalLength(trame);
        this.extractIdenfier(trame);
        this.extractFlagsAndFragmentOffest(trame);
        this.extractTtl(trame);
        this.extractProtocol(trame);
        this.extractHeaderChecksum(trame);
        this.extractSourceAddress(trame);
        this.extractDestinationAddress(trame);
        options = new ArrayList<>();
        this.extractOption(trame);
        this.extractDataTcp(trame);
    }

    private void extractDataTcp(String[] trame) {
        if (!protocol.equals("06")) return;

        try {
            int cpt = Integer.parseInt(this.headerLength, 16) * 4;
            data = new TCP(trame, 14 + cpt);
        } catch (Exception e) {
            Controller.messageErreur(e.getMessage());
        }
    }

    private void extractDestinationAddress(String[] trame) {
        this.dstIP = trame[index++];
        this.dstIP += trame[index++];
        this.dstIP += trame[index++];
        this.dstIP += trame[index++];
    }

    private void extractSourceAddress(String[] trame) {
        this.srcIP = trame[index++];
        this.srcIP += trame[index++];
        this.srcIP += trame[index++];
        this.srcIP += trame[index++];
    }

    private void extractHeaderChecksum(String[] trame) {
        this.headerCheksum = trame[index++];
        this.headerCheksum += trame[index++];
    }

    private void extractProtocol(String[] trame) {
        this.protocol = trame[index++];
    }

    private void extractTtl(String[] trame) {
        this.ttl = trame[index++];
    }

    private void extractIdenfier(String[] trame) {
        this.ID = trame[index++];
        this.ID += trame[index++];
    }

    private String flagsAndOffset;

    private void extractFlagsAndFragmentOffest(String[] trame) {
        String value = trame[index++];
        value += trame[index++];
        this.flagsAndOffset = new String(value);
        String premierChar = value.charAt(0) + "";
        premierChar = hexToBin(premierChar);
        this.R = premierChar.charAt(0) + "";
        this.DF = premierChar.charAt(1) + "";
        this.DM = premierChar.charAt(2) + "";

        value = value.substring(1, 3);
        value = premierChar.charAt(3) + value;
        fragmentOffset = Integer.parseInt(value, 16) + "";

    }

    private void extractVersionAndIhl(String[] trame) {
        String value = trame[index++];
        version = value.charAt(0) + "";
        headerLength = value.charAt(1) + "";
    }

    private void extractTos(String[] trame) {
        tos = trame[index++];
    }

    private void extactTotalLength(String[] trame) throws DataNoValide {
        TotalLength = trame[index++];
        TotalLength += trame[index++];
        this.verificationData(trame, TotalLength);
    }

    public String getVersion() {
        return version;
    }

    public String getHeaderLength() {
        return headerLength;
    }

    public String getTos() {
        return tos;
    }

    public String getTotalLength() {
        return TotalLength;
    }

    public String getID() {
        return ID;
    }

    public String getR() {
        return R;
    }

    public String getDF() {
        return DF;
    }

    public String getDM() {
        return DM;
    }

    public String getFragmentOffset() {
        return fragmentOffset;
    }

    public String getTtl() {
        return ttl;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeaderCheksum() {
        return headerCheksum;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public String getDstIP() {
        return dstIP;
    }

    public static String hexToBin(String hex) {
        hex = hex.toUpperCase();
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    public static String transformeIpAdress(String hex) {
        String result;
        String part = hex.substring(0, 2);
        result = Integer.parseInt(part, 16) + ".";
        part = hex.substring(2, 4);
        result += Integer.parseInt(part, 16) + ".";
        part = hex.substring(4, 6);
        result += Integer.parseInt(part, 16) + ".";
        part = hex.substring(6, 8);
        result += Integer.parseInt(part, 16);
        return result;
    }

    public static String getProtocol(String hex) {
        hex = hex.toLowerCase();
        if (hex.equals("01")) return "ICMP";
        if (hex.equals("02")) return "IGMP";
        if (hex.equals("06")) return "TCP";
        if (hex.equals("08")) return "EGP";
        if (hex.equals("09")) return "IGP";
        if (hex.equals("11")) return "UDP";
        if (hex.equals("24")) return "XTP";
        if (hex.equals("2e")) return "RSVP";
        return "none";
    }

    @Override
    public String toString() {
        return "IP : " +
                " -> protocol='" + getProtocol(protocol);
    }

    private void verificationData(String trame[], String totalLength) throws DataNoValide {

        int tl = Integer.parseInt(totalLength, 16);
        if ((tl != (trame.length - 14))) {
            throw new DataNoValide("Donnée non Valide");

        }

    }

    public TCP getData() {
        return data;
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nInternet Protocol Version ").append(getIntValue(getVersion())).append(", Src: ").append(IP.transformeIpAdress(getSrcIP())).append(", Dst: ").append(IP.transformeIpAdress(getDstIP()));
        sb.append(TAB).append(hexToBin(getVersion())).append(" ....= Version: ").append(Integer.parseInt(getVersion(), 16));
        sb.append(TAB).append(".... ").append(hexToBin(getHeaderLength())).append("= Header Length: ").append(Integer.parseInt(getHeaderLength(), 16) * 4).append(" bytes (").append(Integer.parseInt(getHeaderLength(), 16)).append(")");
        sb.append(TAB).append("Differentiated Services Field: 0x").append(getTos()).append("(DSCP: CS0, ECN: Not-ECT)");
        String x = hexToBin(getTos());
        sb.append(TAB).append("  ").append(x.substring(0, 4)).append(" ").append(x.substring(4, 6)).append("..").append(" = ").append("Differentiated Services Codepoint: Default (").append(Integer.parseInt(x.substring(0, 6), 2)).append(")");
        x = x.substring(6, 8);
        sb.append(TAB).append("  ").append(".... ..").append(x).append(" = Explicit Congestion Notification: Not ECN-Capable Trasport (").append(Integer.parseInt(x, 2)).append(")");
        sb.append(TAB).append("Total Length: ").append(getIntValue(getTotalLength()));
        sb.append(TAB).append("Identification: 0x").append(getID()).append(" (").append(getIntValue(getID())).append(")");
        sb.append(TAB).append("Flags: 0x").append(this.flagsAndOffset);
        sb.append(TAB).append("  ").append(getR()).append("... .... = Reserved bit: ").append(isSet(getR()));
        sb.append(TAB).append("  .").append(getDF()).append(".. .... = Don't fragment: ").append(isSet(getDF()));
        sb.append(TAB).append("  ..").append(getDM()).append(". .... = More fragments: ").append(isSet(getDM()));
        sb.append(TAB).append("Fragment Offset: ").append(getIntValue(getFragmentOffset()));
        sb.append(TAB).append("Time to Live: ").append(getIntValue(getTtl()));
        sb.append(TAB).append("Protocol: ").append(getProtocol(getProtocol())).append(" (").append(getIntValue(protocol)).append(")");
        sb.append(TAB).append("Header Checksum: 0x").append(getHeaderCheksum()).append(" [validation disabled]");
        sb.append(TAB).append("[Header checksum status: Unverified");
        sb.append(TAB).append("Source Address: ").append(transformeIpAdress(getSrcIP()));
        sb.append(TAB).append("Destination Address: ").append(transformeIpAdress(getDstIP()))
                .append(fromDataToFileOptions());
        if (Integer.parseInt(protocol, 16) == 6) sb.append(data.fromDataToFile());
        return sb.toString();
    }

    public String fromDataToFileOptions() {
        StringBuilder sb = new StringBuilder();

        if (hasOption()) {
            int l = (getIntValue(headerLength).intValue() * 4) - 20;
            sb.append(TAB).append("Options: (").append(l).append(" bytes)").append(getOptionsNames());
            for (IPOption option : options)
                sb.append(option.fromDataToFile());
        }
        return sb.toString();
    }

    public String getOptionsNames() {
        StringBuilder sb = new StringBuilder();
        for (IPOption op : this.options)
            sb.append(" ").append(op.getName());
        return sb.toString();
    }

    private String isSet(String bit) {
        return (Integer.parseInt(bit) == 0) ? "Not set" : "Set";
    }

    private BigInteger getIntValue(String hex) {
        return new BigInteger(hex, 16);
    }


    public boolean hasOption() {
        return (getIntValue(headerLength).intValue() * 4) > 20;
    }

    private void extractOption(String trame[]) {
        String type;
        String longeur;
        StringBuilder valeur;
        while (ilExiteDautreOption()) {
            type = trame[index++];
            if (type.equals("00")) {
                this.options.add(extractEOOL(type, trame));
                return;
            }
            longeur = trame[index++];
            valeur = new StringBuilder();
            int cpt = getIntValue(longeur).intValue();
            System.out.println(cpt);
            while (cpt != 2) {
                valeur.append(trame[index++]);
                cpt--;
            }

            this.options.add(FactoryOptions.createOption(type, longeur, valeur.toString()));
        }
        System.out.println("Le nombre d'o^ption" + options.size());
    }

    private IPOption extractEOOL(String type, String[] trame) {
        String longeur = "";
        String valeur = "";
        while (ilExiteDautreOption()) {
            if (longeur.equals("")) {
                longeur = trame[index++];
            } else {
                valeur += trame[index++];
            }
        }
        return FactoryOptions.createOption(type, longeur, valeur);
    }

    private boolean ilExiteDautreOption() {
        int cpt = Integer.parseInt(this.headerLength, 16) * 4;
        return index < (14 + cpt);

    }
}
