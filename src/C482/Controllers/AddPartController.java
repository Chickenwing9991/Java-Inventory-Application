package C482.Controllers;

import C482.Model.InHouse;
import C482.Model.Inventory;
import C482.Model.Outsourced;
import C482.Model.Parts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static C482.Controllers.mainController.confirmDialog;

public class AddPartController implements Initializable {

    @FXML
    private RadioButton addOutsource;
    @FXML
    private RadioButton addInHouse;
    @FXML
    private Label inhouseoroutsourced;
    @FXML
    private TextField addPartID;
    @FXML
    private TextField addPartName;
    @FXML
    private TextField addPartInv;
    @FXML
    private TextField addPartCost;
    @FXML
    private TextField addPartMax;
    @FXML
    private TextField addPartMin;
    @FXML
    private TextField addPartMachOrName;
    @FXML
    private Button addPartSave;
    @FXML
    private Button addPartCancel;

    private Stage stage;
    private Object scene;
    public Parts selectedPart;
    private int partID;

    public AddPartController(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML public void saveButtonPushed(ActionEvent event) throws IOException {
        if (confirmDialog("Save", "Are you sure you would like to save this Part")) {
            savePart();
            stage.close();
        }
    }


    @FXML public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (confirmDialog("Cancel", "Are you sure you would close")) {
            stage.close();
        }
    }

    @FXML
    public void InHouseOrOutsourced(ActionEvent event) throws IOException{
        Object source = event.getSource();
        RadioButton radioButton = (RadioButton) source;
        String selected = radioButton.getText();

        if (selected.equals("In House")){
            this.inhouseoroutsourced.setText("Machine ID");
            this.addPartMachOrName.setPromptText("Machine ID Here");
        }
        else{
            this.inhouseoroutsourced.setText("Company Name");
            this.addPartMachOrName.setPromptText("Company Name Here");
        }
    }

    @FXML
    private void savePart() {
        int id = Integer.parseInt(this.addPartID.getText());
        String name = addPartName.getText();
        double cost = Double.parseDouble(addPartCost.getText());
        int stock = Integer.parseInt(addPartInv.getText());
        int min = Integer.parseInt(addPartMin.getText());
        int max = Integer.parseInt(addPartMax.getText());
        int machineId = 0;
        String companyName = "";

        if (addInHouse.isSelected()) {
            machineId = Integer.parseInt(addPartMachOrName.getText());
            InHouse newPart = new InHouse(id, stock, min, max, name, cost, machineId);
            Inventory.addPart(newPart);
        } else {
            companyName = addPartMachOrName.getText();
            Outsourced newPart = new Outsourced(id, stock, min, max, name, cost, companyName);
            Inventory.addPart(newPart);
        }
    }
}
