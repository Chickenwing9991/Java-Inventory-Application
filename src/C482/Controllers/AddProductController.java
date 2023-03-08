package C482.Controllers;

import C482.Model.Parts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    @FXML
    private RadioButton modifyOutSourced;
    @FXML
    private RadioButton modifyInHouse;
    @FXML
    private Label inhouseoroutsourced;
    @FXML
    private TextField modifyID;
    @FXML
    private TextField modifyName;
    @FXML
    private TextField modifyInv;
    @FXML
    private TextField modifyCost;
    @FXML
    private TextField modifyMax;
    @FXML
    private TextField modifyMin;
    @FXML
    private TextField modifyMachineID;

    private Stage stage;
    private Object scene;
    public Parts selectedPart;
    private int partID;

    public AddProductController(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
