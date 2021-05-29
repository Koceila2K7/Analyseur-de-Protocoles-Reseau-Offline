package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.couches.IP;
import model.couches.TCP;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class ControlTcp implements Initializable {
    static final String SourcePortNumber = "Source port number\n0x";
    static final String DestinationPsortNumber = "Destination port number\n0x";
    static final String SequenceNumber = "Sequence number\n0x";
    static final String AcknowledgmentNumber = "Acknowledgment number\n0x";
    static final String THL = "THL\n0x";
    static final String Reserved = "Reserved\n0b ";
    static final String URG = "URG\n";
    static final String ACK = "ACK\n";
    static final String PSH = "PSH\n";
    static final String RST = "RST\n";
    static final String SYN = "SYN\n";
    static final String FIN = "FIN\n";
    static final String Window = "Window\n0x";
    static final String Checksum = "Checksum\n0x";
    static final String UrgentPointer = "Urgent Pointer\n0x";
    static final String Option = "Option\n0x";
    static final String Data = "Data\n0x";


    @FXML
    private Label lblSourcePortNumber;

    @FXML
    private Label lblDestinationPortNumber;

    @FXML
    private Label lblSequenceNumber;

    @FXML
    private Label lblAcknowledgmentNumber;

    @FXML
    private Label lblTHL;

    @FXML
    private Label lblReserved;

    @FXML
    private Label lblU;

    @FXML
    private Label lblA;

    @FXML
    private Label lblP;

    @FXML
    private Label lblR;

    @FXML
    private Label lblS;

    @FXML
    private Label lblF;

    @FXML
    private Label lblWindow;

    @FXML
    private Label lblChecksum;

    @FXML
    private Label lblUrgentPointer;

    @FXML
    private Label lblOption;

    @FXML
    private Label lblData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setDataInWindow(Controller.trame.getData().getData());
    }

    private void setDataInWindow(TCP data) {
        lblSourcePortNumber.setText(SourcePortNumber + data.getSourcePortNumber() +
                "\n" + Integer.parseInt(data.getSourcePortNumber(), 16));
        lblDestinationPortNumber.setText(DestinationPsortNumber + data.getDestinationPortNumber() +
                "\n" + Integer.parseInt(data.getDestinationPortNumber(), 16));
        lblSequenceNumber.setText(SequenceNumber + data.getSquenceNumber() +
                "\n" + "(" + new BigInteger(data.getSquenceNumber(), 16) + ")");
        lblAcknowledgmentNumber.setText(AcknowledgmentNumber + data.getAcknowledgmentNumber() +
                "\n" + new BigInteger(data.getAcknowledgmentNumber(), 16));
        lblTHL.setText(THL + data.getThl());
        lblReserved.setText(Reserved + data.getReserved());

        lblU.setText(URG + data.getURG());
        lblA.setText(ACK + data.getACK());
        lblP.setText(PSH + data.getPSH());
        lblR.setText(RST + data.getRST());
        lblS.setText(SYN + data.getSYN());
        lblF.setText(FIN + data.getFIN());

        lblWindow.setText(Window + data.getWindow() + "\n ( " + this.getIntValue(data.getWindow()) + " )");
        lblChecksum.setText(Checksum + data.getChecksum());
        lblUrgentPointer.setText(UrgentPointer + data.getUrgentPointer());


        lblData.setOnMouseClicked((me) -> {
            showData(data);
        });
    }

    private void showData(TCP data) {
        if (data.getData() != null) {
            try {
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("http.fxml"));
                primaryStage.setTitle("Http");
                primaryStage.setScene(new Scene(root));
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException e) {
                Controller.messageErreur(e.getMessage());
            }
        }
    }

    private BigInteger getIntValue(String hex) {
        return new BigInteger(hex, 16);
    }
}
