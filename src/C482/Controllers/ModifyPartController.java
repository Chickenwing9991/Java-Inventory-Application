package C482.Controllers;

import C482.Model.InHouse;
import C482.Model.Outsourced;
import C482.Model.Parts;
import C482.Model.Inventory;
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
            if (validateInput()) {
                savePart();
                switchToMainWindow();
            }
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

    private void savePart() {
        int id = Integer.parseInt(modifyID.getText());
        String name = modifyName.getText();
        double cost = Double.parseDouble(modifyCost.getText());
        int stock = Integer.parseInt(modifyInv.getText());
        int min = Integer.parseInt(modifyMin.getText());
        int max = Integer.parseInt(modifyMax.getText());
        int machineId = 0;
        String companyName = "";

        if (modifyInHouse.isSelected()) {
            machineId = Integer.parseInt(modifyMachineID.getText());
            InHouse newPart = new InHouse(id, stock, min, max, name, cost, machineId);
            Inventory.updatePart(partID, newPart);
        } else {
            companyName = modifyMachineID.getText();
            Outsourced newPart = new Outsourced(id, stock, min, max, name, cost, companyName);
            Inventory.updatePart(partID, newPart);
        }
    }

    private void switchToMainWindow() throws IOException {
        stage.close();
    }


}