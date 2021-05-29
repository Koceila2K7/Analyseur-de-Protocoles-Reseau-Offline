package lecture;

public class Error {
    private int numLigne;
    private String typeErreur;

    public Error(int numLigne, String typeErreur) {
        this.numLigne = numLigne;
        this.typeErreur = typeErreur;
    }

    @Override
    public String toString() {
        return "Erreur{" +
                "à la Ligne=" + numLigne +
                ", de type='" + typeErreur + '\'' +
                '}';
    }
}
