package model.couches;

public class Champ {
    private String nom;
    private String valeur;

    public Champ(String nom, String valeur) {
        this.nom = (nom);
        this.valeur = (valeur);
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

    public String getNom() {
        return nom;
    }

    public String getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return nom + ":" + valeur;
    }
}
