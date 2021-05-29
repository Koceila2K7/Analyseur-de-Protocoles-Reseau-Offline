package model.couches;

public class LigneDeRequete {
    private String methode;
    private String url;
    private String version;

    public LigneDeRequete(String methode, String url, String version) {
        this.methode = fromAcssiToString(methode);
        this.url = fromAcssiToString(url);
        this.version = fromAcssiToString(version);
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

    public String getMethode() {
        return methode;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "LigneDeRequete{" +
                "methode='" + methode + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
