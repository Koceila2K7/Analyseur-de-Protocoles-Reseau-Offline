package model.couches;

public class LigneDeReponse {
    private String version;
    private String codeStatut;
    private String message;

    public LigneDeReponse(String version, String codeStatut, String message) {
        this.version = version;
        this.codeStatut = codeStatut;
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public String getCodeStatut() {
        return codeStatut;
    }

    public String getMessage() {
        return message;
    }
}
