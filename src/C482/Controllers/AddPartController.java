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
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

import static C482.Controllers.mainController.confirmDialog;
import static C482.Model.Inventory.getAllParts;

/**
 * Add Part Controller Class. Contains all functions for Adding new Parts and FXML field/button references.
 */
public class AddPartController implements Initializable {

    /**
     * FXML Variable references to application fields.
     */
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


    /**
     * Creating Stage Object for Later Use
     */
    private Stage stage;

    /**
     * Creating Scene Object for Later Use
     */
    private Object scene;

    /**
     * Creating Parts Object for Later Use
     */
    public Parts selectedPart;

    /**
     * Creating Part ID Variable for Later Use
     */
    private int partID;

    /**
     * Constructs a new AddPartController with the given Stage.
     *
     * @param stage the Stage to use for the Add Product window
     */
    public AddPartController(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPartID.setText(String.valueOf(getNewID()));
    }


    /**
     * Handles the save button press event.
     *
     * Errors: Disconnect between FXID and function name. Resolved by matching Name and ID.
     * Had issues with casting Strings to Integers win fields were blank.
     *
     * @param event the ActionEvent that triggered the method
     * @throws IOException if there is an error switching to the main window
     */
    @FXML public void saveButtonPushed(ActionEvent event) throws IOException {
        if (confirmDialog("Save", "Are you sure you would like to save this Part")) {
            savePart();
        }
    }


    /**
     * Closes the current window.
     *
     * @param event triggered event
     * @throws IOException IO Exception
     *
     *
     *
     */
    @FXML public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (confirmDialog("Cancel", "Are you sure you would close")) {
            stage.close();
        }
    }


    /**
     * Triggers when radio button value changes.
     * Changes the Visual cues depending on the radio button value.
     *
     * @param event triggered event
     * @throws IOException IO Exception
     *
     */
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


    /**
     * Validates the user input for the Modify Product form.
     *
     * @param Inv the inventory value as a string
     * @param Min the minimum value as a string
     * @param Max the maximum value as a string
     * @param Name the name value as a string
     * @param Cost the cost value as a string
     * @param partOrMach the part or machine value as a string
     * @param InHouse the Inhouse value as a boolean
     *
     * @return true if the input is valid, false otherwise
     */
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

    /**
     * Processes the new Part Object and saves it to an observable list.
     */
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

    /**
     * Grabs the greatest ID of the existing records and adds 1
     *
     * Errors: Was originally getting the index Size of the List and Incrementing by 1;
     * This caused created an issue with duplicate IDs. Now it gets the Max ID from list and Increments by 1.
     *
     * @return a New ID
     */
    public static int getNewID(){
        try{
            int newID = 1;
            List<Integer> IDs  = getAllParts().stream().map(Parts::getPartId).collect(Collectors.toList());
            newID = Collections.max(IDs);
            if(newID >= 1){
                newID = newID + 1;
            }
            else{
                newID = 1;
            }
            return newID;
        } catch(Exception e) {
            return 1;
        }
    }
}
