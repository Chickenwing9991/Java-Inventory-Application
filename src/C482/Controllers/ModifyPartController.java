package C482.Controllers;

import C482.Model.InHouse;
import C482.Model.Outsourced;
import C482.Model.Parts;
import C482.Model.Inventory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

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

    public ModifyPartController(Stage stage){
        this.stage = stage;
    }

    public void setItem(Parts selectedPart) {
        this.selectedPart = selectedPart;
        partID = C482.Model.Inventory.getAllParts().indexOf(selectedPart);
        modifyID.setText(Integer.toString(selectedPart.getPartId()));
        modifyName.setText(selectedPart.getName());
        modifyInv.setText(Integer.toString(selectedPart.getStock()));
        modifyCost.setText(Double.toString(selectedPart.getCost()));
        modifyMax.setText(Integer.toString(selectedPart.getMax()));
        modifyMin.setText(Integer.toString(selectedPart.getMin()));
        if (selectedPart instanceof InHouse) {
            InHouse ih = (InHouse) selectedPart;
            modifyInHouse.setSelected(true);
            this.inhouseoroutsourced.setText("Machine ID");
            modifyMachineID.setText(Integer.toString(ih.getMachineID()));
        } else {
            Outsourced os = (Outsourced) selectedPart;
            modifyOutSourced.setSelected(true);
            this.inhouseoroutsourced.setText("Company Name");
            modifyMachineID.setText(os.getCompanyName());
        }
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        if (mainController.confirmDialog("Save?", "Would you like to save this part?")) {
            savePart();
        }
    }

    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        if (mainController.confirmDialog("Cancel?", "Are you sure?")) {
            switchToMainWindow();
        }
    }

    @FXML
    public void InHouseOrOutsourced(ActionEvent event) throws IOException{
        Object source = event.getSource();
        RadioButton radioButton = (RadioButton) source;
        String selected = radioButton.getText();

        if (selected.equals("In House")){
            this.inhouseoroutsourced.setText("Machine ID");
        }
        else{
            this.inhouseoroutsourced.setText("Company Name");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private boolean validateInput() {
        int partInventory = Integer.parseInt(modifyInv.getText());
        int partMin = Integer.parseInt(modifyMin.getText());
        int partMax = Integer.parseInt(modifyMax.getText());

        if (partMax < partMin) {
            mainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            return false;
        } else if (partInventory < partMin || partInventory > partMax) {
            mainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            return false;
        }

        if (modifyInHouse.isSelected()) {
            try {
                int machineID = Integer.parseInt(modifyMachineID.getText());
            } catch (NumberFormatException e) {
                mainController.infoDialog("Input Error", "Check Machine ID ", "Machine ID can only contain numbers 0-9");
                return false;
            }
        }

        return true;
    }

    private boolean validateInput(String Inv, String Min, String Max, String Name, String Cost, String partOrMach, boolean InHouse) {
        if (Name.isEmpty() || Inv.isEmpty() || Min.isEmpty() || Max.isEmpty() || Cost.isEmpty() || partOrMach.isEmpty()) {
            mainController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (!Inv.matches("^-?\\d+$")) {
            mainController.infoDialog("Input Error", "Error in Inventory field", "The value must be a Whole number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input");
            return false;
        } else if (!Max.matches("^-?\\d+$")) {
            mainController.infoDialog("Input Error", "Error in Max field", "The value must be a Whole number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input");
            return false;
        } else if (!Min.matches("^-?\\d+$")) {
            mainController.infoDialog("Input Error", "Error in Min field", "The value must be a Whole number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input");
            return false;
        } else if (InHouse == true && !partOrMach.matches("\\d+")) {
            mainController.infoDialog("Input Error", "Error in Machine ID field", "The ID must be a number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (!Cost.matches("[+-]?\\d*\\.?\\d+")) {
            mainController.infoDialog("Input Error", "Error in Price field", "The value must be a number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (Integer.parseInt(Max) < Integer.parseInt(Min)) {
            mainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input");
            return false;
        } else if (Integer.parseInt(Inv) < Integer.parseInt(Min) || Integer.parseInt(Inv) > Integer.parseInt(Max)) {
            mainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        }

        return true;
    }


    private void savePart() {

        String name = modifyName.getText();
        String inventoryText = modifyInv.getText();
        String minText = modifyMin.getText();
        String maxText = modifyMax.getText();
        String costText = modifyCost.getText();
        String idOrNameText = modifyMachineID.getText();

        try {

            int id = partID;
            double cost = Double.parseDouble(costText);
            int stock = Integer.parseInt(inventoryText);
            int min = Integer.parseInt(minText);
            int max = Integer.parseInt(maxText);
            String companyName = null;
            int machineId = 0;

            if (idOrNameText.matches(".*[a-zA-Z]+.*")){
                companyName = idOrNameText;
            }
            else{
                machineId = Integer.parseInt(idOrNameText);
            }

            if (validateInput(inventoryText, minText, maxText, name, costText, idOrNameText, modifyInHouse.isSelected())) {
                if (modifyInHouse.isSelected()) {
                    InHouse newPart = new InHouse(id, stock, min, max, name, cost, machineId);
                    Inventory.updatePart(partID, newPart);
                } else {
                    Outsourced newPart = new Outsourced(id, stock, min, max, name, cost, companyName);
                    Inventory.updatePart(partID, newPart);
                }

                stage.close();
            }
        }

        catch (Exception e){
            System.out.println(e);
            validateInput(inventoryText, minText, maxText, name, costText, idOrNameText, modifyInHouse.isSelected());
        }
    }


    private void switchToMainWindow() throws IOException {
        stage.close();
    }


}