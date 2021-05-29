package model.couches;

public class FactoryOptions {
    public static IPOption createOption(String type, String longeur, String valeur) {
        switch (Integer.parseInt(type, 16)) {
            case 0:
                return new EOOL(type, longeur, valeur);
            case 1:
                return new NOP(type, longeur, valeur);
            case 7:
                return new RecordRoute(type, longeur, valeur);
            case 68:
                return new TimeStamp(type, longeur, valeur);
            case 131:
                return new LooseSourceRoute(type, longeur, valeur);
            case 137:
                return new StrictSourceRoute(type, longeur, valeur);
            default:
                return null;
        }
    }
}
