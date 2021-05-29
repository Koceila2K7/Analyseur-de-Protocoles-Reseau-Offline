package model.couches;

public class EOOL implements IPOption {
    public static String TAB = "\n\t\t";
    public static String JUSTTAB = "\t\t";
    public static String SPACE = "  ";
    private String type;
    private String longeur;
    private String valeur;


    public EOOL(String type, String longeur, String valeur) {
        this.type = type;
        this.longeur = longeur;
        this.valeur = valeur;
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
        return "EOOL";
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();
        String bin = IP.hexToBin(type);
        sb.append(TAB).append(SPACE)
                .append("Ip Option - End of Options List (EOL)")
                .append(TAB).append(SPACE).append(SPACE)
                .append(bin.charAt(0)).append("... .... = Copy on fragmentation: No")
                .append(TAB).append(SPACE).append(SPACE)
                .append(".").append(bin.charAt(1)).append(bin.charAt(2)).append(". .... = Class: Control (0)")
                .append(TAB).append(SPACE).append(SPACE)
                .append("...").append(bin.charAt(3)).append(" ").append(bin.substring(4)).append("= Number: End of Option (EOL) (0)");
        return sb.toString();
    }
}
