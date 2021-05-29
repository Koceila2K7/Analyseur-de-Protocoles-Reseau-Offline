package model.couches;

import java.math.BigInteger;

public class TCP implements IToFile {
    public static String TAB = "\n\t\t";
    public static String SPACE = "  ";
    private String[] trame;
    private int index;

    private String SourcePortNumber;
    private String DestinationPortNumber;
    private String SquenceNumber;
    private String AcknowledgmentNumber;
    private String Thl;
    private String Reserved;
    private String URG;
    private String ACK;
    private String PSH;
    private String RST;
    private String SYN;
    private String FIN;
    private String Window;
    private String checksum;
    private String urgentPointer;

    private HTTP data;
    private int debut;

    public TCP(String[] trame, int index) {
        this.index = index;
        this.debut = index;
        this.trame = trame;
        System.out.println(index);
        this.extractSourcePortNumber(trame);
        this.extractDestinationPortNumber(trame);
        this.extractSequenceNumber(trame);
        this.extractAcknowledgment(trame);
        this.extractThlReservedUAPRSF(trame);
        this.extractWindow(trame);
        this.extractCheksum(trame);
        this.extractUrgentPointer(trame);
        this.extractData(trame);
    }

    private void extractData(String[] trame) {
        if (!hasHttp()) return;
        data = new HTTP(trame, debut + (Integer.parseInt(getThl(), 16) * 4), isResqueste());
    }

    private void extractUrgentPointer(String[] trame) {
        this.urgentPointer = trame[index++];
        this.urgentPointer += trame[index++];
    }

    private void extractCheksum(String[] trame) {
        this.checksum = trame[index++];
        this.checksum += trame[index++];
    }

    private void extractWindow(String[] trame) {
        this.Window = trame[index++];
        this.Window += trame[index++];
    }

    private String pseudoThl;
    private String flags;

    private void extractThlReservedUAPRSF(String[] trame) {
        String value = trame[index++];
        pseudoThl = IP.hexToBin(value);
        value += trame[index++];
        flags = value.substring(1);
        this.Thl = value.charAt(0) + "";
        value = IP.hexToBin(value.substring(1));
        this.Reserved = value.substring(0, 6);

        this.URG = value.charAt(6) + "";
        this.ACK = value.charAt(7) + "";
        this.PSH = value.charAt(8) + "";

        this.RST = value.charAt(9) + "";
        this.SYN = value.charAt(10) + "";
        this.FIN = value.charAt(11) + "";

    }

    private void extractAcknowledgment(String[] trame) {
        this.AcknowledgmentNumber = trame[index++];
        this.AcknowledgmentNumber += trame[index++];
        this.AcknowledgmentNumber += trame[index++];
        this.AcknowledgmentNumber += trame[index++];
    }

    private void extractSequenceNumber(String[] trame) {
        this.SquenceNumber = trame[index++];
        this.SquenceNumber += trame[index++];
        this.SquenceNumber += trame[index++];
        this.SquenceNumber += trame[index++];
    }

    private void extractDestinationPortNumber(String[] trame) {
        this.DestinationPortNumber = trame[index++];
        this.DestinationPortNumber += trame[index++];
    }

    private void extractSourcePortNumber(String[] trame) {
        this.SourcePortNumber = trame[index++];
        this.SourcePortNumber += trame[index++];
    }


    public String[] getTrame() {
        return trame;
    }

    public int getIndex() {
        return index;
    }

    public String getSourcePortNumber() {
        return SourcePortNumber;
    }

    public String getDestinationPortNumber() {
        return DestinationPortNumber;
    }

    public String getSquenceNumber() {
        return SquenceNumber;
    }

    public String getAcknowledgmentNumber() {
        return AcknowledgmentNumber;
    }

    public String getThl() {
        return Thl;
    }

    public String getReserved() {
        return Reserved;
    }

    public String getURG() {
        return URG;
    }

    public String getACK() {
        return ACK;
    }

    public String getPSH() {
        return PSH;
    }

    public String getRST() {
        return RST;
    }

    public String getSYN() {
        return SYN;
    }

    public String getFIN() {
        return FIN;
    }

    public String getWindow() {
        return Window;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getUrgentPointer() {
        return urgentPointer;
    }

    public HTTP getData() {
        return data;
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();
        int length = trame.length - debut;
        length -= getIntValue(getThl()).intValue() * 4;
        sb.append("\nTransmission Control Protocol, Src Port: ").append(getIntValue(getSourcePortNumber()))
                .append(", Dst Port: ").append(getIntValue(getDestinationPortNumber()))
                .append(", Len: ").append(length)
                .append(TAB)
                .append("Source Port: ")
                .append(getIntValue(getSourcePortNumber()))
                .append(TAB)
                .append("Destination Port: ")
                .append(getIntValue(getDestinationPortNumber()))
                .append(TAB)
                .append("Sequence Number (raw):")
                .append(getIntValue(getSquenceNumber()))
                .append(TAB)
                .append("Acknowledgment number (raw): ")
                .append(getIntValue(getAcknowledgmentNumber()))
                .append(TAB)
                .append(pseudoThl.substring(0, 4))
                .append(" .... = Header Length: ")
                .append(getIntValue(getThl()).intValue() * 4)
                .append(" bytes (")
                .append(getIntValue(getThl()))
                .append(")")
                .append(TAB)
                .append("Flags: 0x").append(flags).append("(").append(nameOfFlags()).append(")")
                .append(TAB).append(SPACE)
                .append(Reserved.substring(0, 3)).append(". .... .... = Reserved: ").append(isSet(Reserved.substring(0, 3)))
                .append(TAB).append(SPACE)
                .append("...").append(Reserved.charAt(3)).append(" .... .... = Nonce: ").append(isSet(Reserved.charAt(3) + ""))
                .append(TAB).append(SPACE)
                .append(".... ").append(Reserved.charAt(4)).append("... .... = Congestion Window Reduced (CWR): ").append(isSet(Reserved.charAt(4) + ""))
                .append(TAB).append(SPACE)
                .append(".... .").append(Reserved.charAt(5)).append(".. .... = ECN-Echo: ").append(isSet(Reserved.charAt(5) + ""))
                .append(TAB).append(SPACE)
                .append(".... ..").append(URG).append(". .... = Urgent: ").append(isSet(URG))
                .append(TAB).append(SPACE)
                .append(".... ...").append(ACK).append(" .... = Acknowledgment: ").append(isSet(ACK))
                .append(TAB).append(SPACE)
                .append(".... .... ").append(PSH).append("... = Push: ").append(isSet(PSH))
                .append(TAB).append(SPACE)
                .append(".... .... .").append(RST).append(".. = Rest: ").append(isSet(RST))
                .append(TAB).append(SPACE)
                .append(".... .... ..").append(SYN).append(". = Syn: ").append(isSet(SYN))
                .append(TAB).append(SPACE)
                .append(".... .... ...").append(FIN).append(" = Fin: ").append(isSet(FIN))
                .append(TAB).append(SPACE)
                .append("[TCP Flags: ").append(getFlagSentence()).append("]")
                .append(TAB).append("Window: ").append(getIntValue(Window))
                .append(TAB).append("[Calculated window size: ").append(getIntValue(Window)).append("]")
                .append(TAB).append("Checksum: 0x").append(checksum).append(" [unverified]")
                .append(TAB).append("[Checksum Status: Unverified]")
                .append(TAB).append("Urgent Pointer: ").append(getIntValue(urgentPointer))
        ;
        if (data != null) sb.append(data.fromDataToFile());
        return sb.toString();
    }

    private BigInteger getIntValue(String hex) {
        return new BigInteger(hex, 16);
    }

    private String nameOfFlags() {
        StringBuilder sb = new StringBuilder();
        if (this.URG.equals("1")) sb.append(" URG ");
        if (ACK.equals("1")) sb.append(" ACK ");
        if (PSH.equals("1")) sb.append(" PSH ");
        if (RST.equals("1")) sb.append(" RST ");
        if (SYN.equals("1")) sb.append(" SYN ");
        if (FIN.equals("1")) sb.append(" FIN ");
        return sb.toString();
    }

    private String isSet(String bin) {
        return (Integer.parseInt(bin, 2) == 0) ? "Not set" : "Set";
    }

    private String getFlagSentence() {
        StringBuilder sb = new StringBuilder();
        sb.append("......");
        if (URG.equals("1")) sb.append("U");
        else sb.append(".");
        if (ACK.equals("1")) sb.append("A");
        else sb.append(".");
        if (PSH.equals("1")) sb.append("P");
        else sb.append(".");
        if (RST.equals("1")) sb.append("R");
        else sb.append(".");
        if (SYN.equals("1")) sb.append("S");
        else sb.append(".");
        if (FIN.equals("1")) sb.append("F");
        else sb.append(".");
        return sb.toString();
    }

    private Boolean hasHttp() {
        return ((getIntValue(SourcePortNumber).intValue() == 80) || (getIntValue(DestinationPortNumber).intValue() == 80));
    }

    private boolean isResqueste() {
        return (getIntValue(DestinationPortNumber).intValue() == 80);
    }


}



