package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Http implements Initializable {
    @FXML
    private TextArea txtHttp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtHttp.setEditable(false);
        txtHttp.setText(Main.slectedIndex.getData().getData().getData().fromDataToFile());
    }
}
