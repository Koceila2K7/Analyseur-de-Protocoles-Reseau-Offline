package model.couches;

public class StrictSourceRoute implements IPOption{
    private String type;
    private String longeur;
    private String valeur;

    public StrictSourceRoute(String type, String longeur, String valeur) {
        this.type = type;
        this.longeur = longeur;
        this.valeur = valeur;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getLongeur() {
        return null;
    }

    @Override
    public String getValeur() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String fromDataToFile() {
        return null;
    }
}
