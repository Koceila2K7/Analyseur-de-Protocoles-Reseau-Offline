package model.couches;

import javafx.scene.control.Control;

import java.util.ArrayList;
import java.util.List;

public class HTTP implements IToFile {
    public static String TAB = "\n\t\t";
    public static String JUSTTAB = "\t\t";
    public static String SPACE = "  ";

    public static String getMethode(String hex) {
        hex = hex.toUpperCase();
        if (hex.equals("474554")) return "GET";
        if (hex.equals("48454144")) return "HEAD";
        if (hex.equals("505F5354")) return "POST";
        if (hex.equals("505554")) return "PUT";
        if (hex.equals("44454C455445")) return "DELETE";
        return "none";
    }


    private String[] trame;
    private int index;
    private boolean isRequest;
    private LigneDeReponse ligneDeReponse;
    private LigneDeRequete ligneDeRequete;
    private String corps = "";
    private List<Champ> champs;

    public HTTP(String[] trame, int index, boolean isRequest) {
        this.trame = trame;
        this.index = index;
        this.isRequest = isRequest;
        this.champs = new ArrayList<>();

        if (isRequest) requestConfiguration();
        else responseConfiguration();
    }

    private void responseConfiguration() {
        configureLigneDeReponse();
        extractChamps();
        corpsDeLaResponse();
    }

    private void corpsDeLaResponse() {
        StringBuilder sb = new StringBuilder();
        for (; index < trame.length; index++)
            sb.append(trame[index]);
        this.corps = fromAcssiToString(sb.toString());
    }

    private void configureLigneDeReponse() {
        String line = getLigne();
        String[] tab = line.split("20");
        try {
            this.ligneDeReponse = new LigneDeReponse(tab[0], tab[1], tab[2]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void requestConfiguration() {
        configureLigneDeRequete();
        extractChamps();
        corpsDeRequetePost();
    }

    private void configureLigneDeRequete() {
        String str = getLigne();
        String[] tab = str.toLowerCase().split("20");
        String methode = tab[0];
        String url = tab[1];
        String version = tab[2].substring(0, tab[2].length() - 4);
        this.ligneDeRequete = new LigneDeRequete(methode, url, version);
    }

    private void extractChamps() {
        String line = "";
        while (true) {
            line = getLigne();
            if (isLast(line)) return;
            this.champs.add(getChampFromLigne(line));
        }
    }

    private String corpsPost;

    private void corpsDeRequetePost() {
        if (!getMethode(this.ligneDeRequete.getMethode()).equals("POST")) return;
        StringBuilder sb = new StringBuilder();

        for (; index < trame.length; index++)
            sb.append(trame[index]);

        this.corpsPost = fromAcssiToString(sb.toString());
    }

    private String getLigne() {
        StringBuilder sb = new StringBuilder();
        String value;
        while (true) {
            value = trame[index++];
            if (value.toLowerCase().equals("0d") && trame[index].toLowerCase().equals("0a")) {
                sb.append(value);
                sb.append(trame[index]);
                index++;
                return sb.toString();
            }
            sb.append(value);
        }
    }

    private boolean isLast(String ligne) {
        return ligne.toLowerCase().equals("0d0a");
    }

    private Champ getChampFromLigne(String ligne) {
        ligne = fromAcssiToString(ligne);
        String tab[] = ligne.split(":");
        String nomChamp = tab[0];
        String valeur = tab[1];
        for (int i = 2; i < tab.length; i++) {
            valeur = valeur + ":" + tab[i];
        }

        return new Champ(nomChamp, valeur);
    }

    @Override
    public String fromDataToFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHypertext Transfer Protocol").append(TAB);

        if (isRequest) {
            sb.append(ligneDeRequete.getMethode()).append("  ").append(ligneDeRequete.getUrl()).append("  ").append(ligneDeRequete.getVersion())
                    .append(TAB).append(SPACE)
                    .append("Request Method: ").append(ligneDeRequete.getMethode())
                    .append(TAB).append(SPACE)
                    .append("Request URI: ").append(ligneDeRequete.getUrl())
                    .append(TAB).append(SPACE)
                    .append("Request Version: ").append(ligneDeRequete.getVersion())
            ;
        } else {
            sb.append(ligneDeReponse.getVersion()).append(ligneDeReponse.getCodeStatut()).append(ligneDeReponse.getMessage())
                    .append(TAB).append(SPACE)
                    .append("Response Version: ").append(ligneDeReponse.getVersion())
                    .append(TAB).append(SPACE)
                    .append("Response Status code: ").append(ligneDeReponse.getCodeStatut())
                    .append(TAB).append(SPACE)
                    .append("Response Message: ").append(ligneDeReponse.getMessage());
        }
        sb.append("\n");
        for (Champ ch : champs)
            sb.append(JUSTTAB).append(ch);
        if (isRequest && ligneDeRequete.getMethode().toUpperCase().equals("POST")) sb.append(corpsPost);
        if (!isRequest) sb.append(corps);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "HTTP{" +
                "champs=" + ligneDeRequete +
                '}';
    }

    private String fromAcssiToString(String ascii) {
        StringBuilder sb = new StringBuilder();
        int mod = 1;
        String word = "";
        for (String s : ascii.split("")) {
            word += s;
            if ((mod % 2) == 0) {
                sb.append((char) Integer.parseInt(word, 16));
                word = "";
                mod = 0;
            }
            mod++;
        }

        return sb.toString();


    }
}
