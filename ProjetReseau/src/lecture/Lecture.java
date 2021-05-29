package lecture;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lecture {
    public static final String ERROR_LIGNE_VIDE = "Ligne incomplète";
    public static final String ERROR_CARACTERE_NON_HEX = "Un Caractère non hex dans une ligne";
    private File file;
    private Integer nbLigne;
    private Map<Integer, List<String>> data;
    private List<Error> errors;

    public Lecture(File file) {
        if (file == null) {
            messageErreur("Le fichier est vide");
            return;
        }
        nbLigne = 0;
        this.errors = new ArrayList<>();
        this.data = new HashMap<>();
        lire(file);
    }


    private void lire(File file) {
        String courantline;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            while ((courantline = saut(br)) != null) {
                this.lectureDeTrameCourant(courantline, br);
            }
            br.close();
        } catch (IOException e) {
            messageErreur(e.getMessage());
        }

    }

    private String saut(BufferedReader br) throws IOException {
        String line;
        String offset;

        while ((line = br.readLine()) != null) {

            this.nbLigne++;
            if (line.length() > 4) {
                offset = line.substring(0, 4);

                if (validateOffest(offset) && getDecValue(offset) == 0) {

                    return line;
                }
            }
        }
        return null;
    }


    public static void messageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return;
    }

    public Integer getNbLigne() {
        return nbLigne;
    }

    public Map<Integer, List<String>> getData() {
        return data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    private boolean isOctet(String str) {
        if (str.length() != 2) return false;
        try {
            Integer.parseInt(str, 16);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private int getDecValue(String hex) {
        return Integer.parseInt(hex, 16);
    }

    private String getOffest(String ligne) {
        if (ligne.length() >= 4) {
            String offest = ligne.substring(0, 4);
            if (validateOffest(offest))
                return offest;
        }
        return null;
    }

    private void lectureDeTrameCourant(String ligne, BufferedReader br) throws IOException {

        String prec, courant, octet;
        prec = ligne;
        List<String> trameCourante = new ArrayList<>();
        Integer ligneDebut = nbLigne;
        int cpt;
        String[] octets;
        while ((courant = br.readLine()) != null) {

            this.nbLigne++;

            if (this.getOffest(courant) == null) {

                try {
                    octets = removeOffset(prec).split(" ");
                } catch (Exception e) {
                    return;
                }
                for (String s : octets) {
                    if (!isOctet(s)) break;
                    trameCourante.add(s);
                }
                if (!trameCourante.isEmpty()) {
                    data.put(ligneDebut, trameCourante);
                }
                return;
            }
            cpt = getDecValue(this.getOffest(courant));
            try {
                octets = removeOffset(prec).split(" ");
            } catch (Exception e) {
                this.errors.add(new Error(nbLigne - 1, "Ligne incomplète"));
                return;
            }
            if (cpt < trameCourante.size()) {
                this.errors.add(new Error(nbLigne - 1, "Offset non valide"));
                return;
            }
            try {
                cpt = cpt - trameCourante.size();
                for (int i = 0; i < cpt; i++) {
                    octet = octets[i];
                    if (isOctet(octet)) {
                        trameCourante.add(octet);
                    } else {
                        this.errors.add(new Error(nbLigne - 1, ERROR_CARACTERE_NON_HEX));
                        return;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                this.errors.add(new Error(nbLigne - 1, ERROR_LIGNE_VIDE));
                return;
            }
            prec = courant;
        }
        try {
            octets = removeOffset(prec).split(" ");
        } catch (Exception e) {
            return;
        }
        for (String s : octets) {
            if (!isOctet(s)) break;
            trameCourante.add(s);
        }
        if (!trameCourante.isEmpty()) {
            data.put(ligneDebut, trameCourante);
        }
    }

    private boolean validateOffest(String s) {
        try {
            Integer.parseInt(s, 16);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String removeOffset(String s) throws Exception {
        if (s.length() <= 7)
            throw new Exception();
        return s.substring(7);
    }


}
