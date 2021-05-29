package sample;

import exceptions.TrameVideException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.couches.Ethernet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    final static String MAC_DESTINATIO = "Mac Destination\n ";
    final static String MAC_SOURCE = "Mac Source\n";
    final static String TYPE = "Type\n0x ";
    final static String DATA = "Data\n";

    public static Ethernet trame = null;


    @FXML
    private Label lblMasDest;
    @FXML
    private Label lblMasSource;
    @FXML
    private Label lbltype;
    @FXML
    private Label lblData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trame = Main.slectedIndex;

        lblData.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (trame != null) {
                    try {
                        afficherDataDetail();
                    } catch (IOException e) {
                        messageErreur(e.getMessage());
                    }
                }
            }
        });

        if (trame != null) {
            try {
                remplireLesChamp(trame);
            } catch (Exception e) {
                messageErreur(e.getMessage());
            }
        }

    }

    public static void messageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return;
    }

    public static String[] readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String words[] = ((line = br.readLine()) != null) ? line.split(" ") : null;
        br.close();
        return words;
    }

    private void viderTout() {
        this.trame = null;
        this.lblData.setText("");
        this.lblMasDest.setText("");
        this.lblMasSource.setText("");
        this.lblData.setText("");
    }

    private void remplireLesChamp(Ethernet trame) {
        lblMasDest.setText(MAC_DESTINATIO + trame.getMacDestination());
        lblMasSource.setText(MAC_SOURCE + trame.getMacSource());
        lbltype.setText(TYPE + trame.getProtocle() + "\n" + Ethernet.getType(trame.getProtocle()));
    }

    private void afficherDataDetail() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ip.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Couche IP");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }


}
