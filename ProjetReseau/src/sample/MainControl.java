package sample;

import exceptions.TrameVideException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lecture.Error;
import lecture.Lecture;
import model.couches.Ethernet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static sample.Controller.messageErreur;

public class MainControl implements Initializable {
    @FXML
    private MenuItem itemOpen;

    @FXML
    private MenuItem itemSave;

    @FXML
    private MenuItem itemClear;

    @FXML
    private MenuItem itemAbout;

    @FXML
    private MenuItem itemClose;

    @FXML
    private Button btnscan;

    @FXML
    private Button btnSave;

    @FXML
    private ListView<Ethernet> listTrame;

    @FXML
    private ListView<Error> listErreur;


    private List<Ethernet> trames;

    private Lecture lecture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.init();
        this.configureToolBar();
        this.configureMenuBar();
        this.configureListView();
    }

    private void openFile() {
        try {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT", "*.txt"));
                File f = fileChooser.showOpenDialog(btnSave.getScene().getWindow());
                if (f != null) {
                    lecture = new Lecture(f);
                    this.listErreur.getItems().addAll(lecture.getErrors());
                    this.extraireLesTrames();
                }
            } catch (Exception e) {
                messageErreur(e.getMessage());

            }
        } catch (Exception e) {
            messageErreur(e.getMessage());
        }

    }

    private void configureListView() {
        this.listTrame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.slectedIndex = listTrame.getSelectionModel().getSelectedItem();
                if (Main.slectedIndex != null) {
                    Stage stage = new Stage();

                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                        stage.setResizable(false);
                        stage.setTitle("Couche Ethernet");
                        Scene scene = new Scene(root);
                        stage.setScene(scene);

                    } catch (IOException e) {
                        Controller.messageErreur(e.getMessage());
                    }
                    stage.show();
                }
            }
        });
    }

    private void configureMenuBar() {
        itemClose.setOnAction((e) -> {
            ((Stage) btnSave.getScene().getWindow()).close();
        });
        itemClear.setOnAction((e) -> {
            init();
        });
        itemOpen.setOnAction((actionEvent) -> {
            openFile();
        });
        itemSave.setOnAction((e) -> {
            storeFile();
        });
        itemAbout.setOnAction((e) -> showAboutDialog());
    }

    private void configureToolBar() {
        btnscan.setOnAction((ActionEvent actionEvent) -> {
            openFile();
        });
        btnSave.setOnAction((e) -> {
            storeFile();
        });
    }

    private void init() {
        this.listTrame.getItems().clear();
        this.listErreur.getItems().clear();
        this.trames = null;
        this.lecture = null;
        listTrame.setPlaceholder(new Text("Aucune trame detectée"));
        listErreur.setPlaceholder(new Text("Aucune erreur detectée"));
    }

    private void extraireLesTrames() {
        this.trames = new ArrayList<>();
        if (lecture == null) return;
        Ethernet t;
        for (Integer key : lecture.getData().keySet()) {
            try {
                t = new Ethernet(createArrayFromeList(lecture.getData().get(key)), key);
                this.trames.add(t);
            } catch (Exception e) {
                this.listErreur.getItems().add(new Error(key, e.getMessage()));
            }
        }
        Collections.sort(trames);
        System.out.println(trames.get(trames.size() - 1).fromDataToFile());
        this.listTrame.getItems().addAll(trames);

    }

    private void storeFile() {
        if (trames == null || trames.isEmpty()) return;
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT", "*.txt"));
        File f = chooser.showSaveDialog(btnSave.getScene().getWindow());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (Ethernet trame : this.trames)
                bw.write("\n" + trame.fromDataToFile());
            bw.close();
        } catch (IOException e) {
            messageErreur(e.getMessage());
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Projet Analyseur de Protocoles Réseau ‘Offline’ (LU3IN033)");
        alert.setContentText("Projet réalisé par:\n -Koceila KIRECHE \n -Chems Eddine BENSAFIA");
        alert.showAndWait();
    }

    private String[] createArrayFromeList(List<String> list) {
        String[] result = new String[list.size()];
        int i = 0;
        for (String s : list) {
            result[i] = s;
            i++;
        }
        return result;
    }
}
