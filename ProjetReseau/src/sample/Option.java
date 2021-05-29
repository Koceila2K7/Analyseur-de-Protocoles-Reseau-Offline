package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Option implements Initializable {
    @FXML
    private TextArea txtOption;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtOption.setEditable(false);
        txtOption.setText(Main.slectedIndex.getData().fromDataToFileOptions() + "\n\n");
    }
}
