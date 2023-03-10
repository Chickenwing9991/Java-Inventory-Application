package C482.Controllers;

import C482.Model.InHouse;
import C482.Model.Inventory;
import C482.Model.Outsourced;
import C482.Model.Parts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static C482.Controllers.mainController.confirmDialog;
import static C482.Model.Inventory.getAllParts;

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
        addPartID.setText(String.valueOf(getNewID()));
    }

    @FXML public void saveButtonPushed(ActionEvent event) throws IOException {
        if (confirmDialog("Save", "Are you sure you would like to save this Part")) {
            savePart();
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

    private boolean validateInput(String Inv, String Min, String Max, String Name, String Cost, String partOrMach, boolean InHouse) {
        if (Name.isEmpty() || Inv.isEmpty() || Min.isEmpty() || Max.isEmpty() || Cost.isEmpty() || partOrMach.isEmpty()) {
            mainController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (Integer.parseInt(Max) < Integer.parseInt(Min)) {
            mainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (Integer.parseInt(Inv) < Integer.parseInt(Min) || Integer.parseInt(Inv) > Integer.parseInt(Max)) {
            mainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (InHouse == true && !partOrMach.matches("\\d+")) {
            mainController.infoDialog("Input Error", "Error in Machine ID field", "The ID must be a number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (!Cost.matches("[+-]?\\d*\\.?\\d+")) {
            mainController.infoDialog("Input Error", "Error in Price field", "The value must be a number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
        } else if (!) {
            
        }
        return true;
    }

    @FXML
    private void savePart() {

        String name = addPartName.getText();
        String inventoryText = addPartInv.getText();
        String minText = addPartMin.getText();
        String maxText = addPartMax.getText();
        String costText = addPartCost.getText();
        String idOrNameText = addPartMachOrName.getText();

        try {

            int id = getNewID();
            double cost = Double.parseDouble(addPartCost.getText());
            int stock = Integer.parseInt(addPartInv.getText());
            int min = Integer.parseInt(addPartMin.getText());
            int max = Integer.parseInt(addPartMax.getText());
            int machineId = 0;
            String companyName = "";

            if (validateInput(inventoryText, minText, maxText, name, costText, idOrNameText, addInHouse.isSelected())) {
                if (addInHouse.isSelected()) {
                    machineId = Integer.parseInt(addPartMachOrName.getText());
                    InHouse newPart = new InHouse(id, stock, min, max, name, cost, machineId);
                    Inventory.addPart(newPart);
                } else {
                    companyName = addPartMachOrName.getText();
                    Outsourced newPart = new Outsourced(id, stock, min, max, name, cost, companyName);
                    Inventory.addPart(newPart);
                }

                stage.close();
            }
        }
        catch (Exception e){
            validateInput(inventoryText, minText, maxText, name, costText, idOrNameText, addInHouse.isSelected());
        }
    }

    public static int getNewID(){
        int newID = 1;
        for (int i = 0; i < getAllParts().size(); i++) {
            newID++;
        }
        return newID;
    }

}
