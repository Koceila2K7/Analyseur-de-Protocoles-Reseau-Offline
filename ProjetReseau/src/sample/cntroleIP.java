package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.couches.IP;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class cntroleIP implements Initializable {

    static final String VERSION = "Version\n0x";
    static final String IHL = "IHL\n0x";
    static final String TOS = "TOS\n0x";
    static final String TOTAL_LENGTH = "TOTAL LENGTH\n0x";
    static final String IDENTIFIER = "Identifier\n0x";
    static final String R_DF_MF = "R DF MF\n";
    static final String FRAGMENT_OFFSET = "Fragment offset\n";
    static final String TTL = "TTL\n0x ";
    static final String Protocol = "Protocol\n";
    static final String Header_checksum = "Header Checksum\n0x";
    static final String iP_source = "Source address\n 0x";
    static final String ip_dest = "Destination address\n 0x";
    static final String option = "Option \n";
    static final String DATA = "Data\n";
    @FXML
    private Label lblVersion;

    @FXML
    private Label lblIhl;

    @FXML
    private Label lblTos;

    @FXML
    private Label lbltTotalLength;

    @FXML
    private Label lblIdenficateur;

    @FXML
    private Label lblFlags;

    @FXML
    private Label lblFragementOffest;

    @FXML
    private Label lblTtl;

    @FXML
    private Label lblProtocol;

    @FXML
    private Label lblHeadrChercksum;

    @FXML
    private Label lblSource;

    @FXML
    private Label lblDestination;

    @FXML
    private Label lblOption;

    @FXML
    private Label lblData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Controller.trame != null && Controller.trame.getData() == null) {
            Controller.messageErreur("Les données ne sont pas du protocol IP");
            ((Stage) this.lblData.getScene().getWindow()).close();
            return;
        }
        this.setDataInWindow(Controller.trame.getData());
    }

    private void setDataInWindow(IP data) {
        lblVersion.setText(VERSION + data.getVersion());
        lblIhl.setText(IHL + data.getHeaderLength());
        lblTos.setText(TOS + data.getTos());
        String longeur = data.getTotalLength();
        lbltTotalLength.setText(TOTAL_LENGTH + longeur + "\n" + "(" + Integer.parseInt(longeur, 16) + ") octets");
        lblIdenficateur.setText(IDENTIFIER + data.getID() + "\n" + "(" + Integer.parseInt(data.getID(), 16) + ")");
        lblFlags.setText(R_DF_MF + data.getR() + "  " + data.getDF() + "    " + data.getDM());
        lblFragementOffest.setText(FRAGMENT_OFFSET + data.getFragmentOffset());
        lblTtl.setText(TTL + data.getTtl() + "\n" + Integer.parseInt(data.getTtl(), 16));
        lblProtocol.setText(Protocol + data.getProtocol() + "\n" + IP.getProtocol(data.getProtocol()));
        lblHeadrChercksum.setText(Header_checksum + data.getHeaderCheksum());
        lblSource.setText(iP_source + data.getSrcIP() + "\n" + IP.transformeIpAdress(data.getSrcIP()));
        lblDestination.setText(ip_dest + data.getDstIP() + "\n" + IP.transformeIpAdress(data.getDstIP()));
        lblData.setText(DATA + "(" + IP.getProtocol(data.getProtocol()) + ")");
        if (data.hasOption())
            lblOption.setText(option + data.getOptionsNames().trim());
        lblOption.setOnMouseClicked((e) -> {
            showOptionWindow(data);
        });
        lblData.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!IP.getProtocol(data.getProtocol()).equals("TCP")) {
                    Controller.messageErreur("Notre programme support traite uniquement TCP");
                    return;
                }
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("tcp.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("TCP");
                    stage.show();
                } catch (Exception e) {
                    Controller.messageErreur(e.getMessage());
                }
            }
        });
    }

    private void showOptionWindow(IP data) {
        if (!data.hasOption())
            Controller.messageErreur("Acune option à afficher");
        else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Options");
                stage.show();
            } catch (Exception e) {
                Controller.messageErreur(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
