package C482.Controllers;

import C482.Model.Inventory;
import C482.Model.Parts;
import C482.Model.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static C482.Controllers.mainController.confirmDialog;
import static C482.Controllers.mainController.infoDialog;
import static C482.Model.Inventory.getAllParts;
import static C482.Model.Inventory.getAllProducts;


/**
 * Add Product Controller Class. Contains all functions for Adding new Products and FXML field/button references.
 */
public class AddProductController implements Initializable {

    /**
     * FXML Variable references to application fields.
     */
    @FXML
    private TextField addProductID;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInv;
    @FXML
    private TextField addProductCost;
    @FXML
    private TextField addProductMax;
    @FXML
    private TextField addProductMin;

    @FXML
    private TableView<Parts> partTable;
    @FXML
    private TableColumn<Parts, Integer> partIDCol;
    @FXML
    private TableColumn<Parts, String> partNameCol;
    @FXML
    private TableColumn<Parts, Integer> partInvCol;
    @FXML
    private TableColumn<Parts, Double> partCostCol;

    @FXML
    private TableView<Parts> associatedTable;
    @FXML
    private TableColumn<Parts, Integer> associatedIDCol;
    @FXML
    private TableColumn<Parts, String> associatedNameCol;
    @FXML
    private TableColumn<Parts, Integer> associatedInvCol;
    @FXML
    private TableColumn<Parts, Double> associatedCostCol;

    @FXML
    private TextField searchParts;

    /**
     * Creates Observable Lists for Associated Parts.
     */
    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Parts> orgPart = FXCollections.observableArrayList();

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
     * Constructs a new AddProductController with the given Stage.
     *
     * @param stage the Stage to use for the Add Product window
     */
    public AddProductController(Stage stage){
        this.stage = stage;
    }

    /**
     * Sets the Table Views to the Corresponding Lists
     *
     * @param items the ObservableList to set
     * @param itemType the type part or product
     */
    private <T> void setItemsTableView(ObservableList<T> items, String itemType) {
        if (itemType.equals("part")) {
            partTable.setItems((ObservableList<Parts>) items);
        } else if (itemType.equals("product")) {
            partTable.setItems((ObservableList<Parts>) items);
        } else {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.WARNING, "Unknown item type: " + itemType);
        }
    }

    /**
     * Searches for a part in the Inventory based on the search term.
     *
     * @param event the ActionEvent that triggered the method
     */
    @FXML
    public void searchPartPushed(ActionEvent event) {
        String searchTerm = searchParts.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                int partId = Integer.parseInt(searchTerm);
                Parts foundPart = Inventory.lookupPart(null, partId);
                if (foundPart != null) {
                    setItemsTableView(FXCollections.observableArrayList(foundPart), "part");
                } else {
                    infoDialog("No Match", "Unable to locate a part with ID: " + partId);
                }
            } catch (NumberFormatException e) {
                Parts foundPart = Inventory.lookupPart(searchTerm, null);
                if (foundPart != null) {
                    setItemsTableView(FXCollections.observableArrayList(foundPart), "part");
                } else {
                    infoDialog("No Match", "Unable to locate a part with name: " + searchTerm);
                }
            }
        }

        else {
            ObservableList<Parts> allparts = Inventory.getAllParts();
            setItemsTableView(allparts, "part");
        }
    }


    /**
     * Adds the selected part to the associated parts table.
     *
     * @param event the ActionEvent that triggered the method
     */
    @FXML
    public void addButtonPressed(ActionEvent event) {
        Parts selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            updateTables();
        } else {
            infoDialog("Select a part", "Select a part to add to the Product");
        }
    }

    /**
     *
     * Triggers when delete button is pressed.
     * Removes selected item from table and list.
     *
     * @param event Triggered Event
     *
     *
     */
    @FXML
    public void removeButtonPressed(ActionEvent event) {
        Parts selectedPart = associatedTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            boolean confirmed = confirmDialog("Deleting Parts", "Are you sure you want to delete " + selectedPart.getName() + " from the Product?");
            if (confirmed) {
                associatedParts.remove(selectedPart);
                updateTables();
            }
        } else {
            infoDialog("No Selection", "Please choose something to remove");
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
     * @return true if the input is valid, false otherwise
     */
    private boolean validateInput(String Inv, String Min, String Max, String Name, String Cost) {
        if (Name.isEmpty() || Inv.isEmpty() || Min.isEmpty() || Max.isEmpty() || Cost.isEmpty()) {
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
     * Handles the save button press event.
     *
     * Errors: Disconnect between FXID and function name. Resolved by matching Name and ID.
     * Had issues with casting Strings to Integers win fields were blank.
     *
     * @param event the ActionEvent that triggered the method
     * @throws IOException if there is an error switching to the main window
     */
    @FXML
    public void saveButtonPressed(ActionEvent event) throws IOException {

        String name = addProductName.getText();
        String inventoryText = addProductInv.getText();
        String minText = addProductMin.getText();
        String maxText = addProductMax.getText();
        String costText = addProductCost.getText();

        try {

            int id = getNewID();
            int inventory = Integer.parseInt(inventoryText);
            int min = Integer.parseInt(minText);
            int max = Integer.parseInt(maxText);
            double cost = Double.parseDouble(costText);


            boolean confirmed = confirmDialog("Save?", "Would you like to save this part?");
            if (confirmed) {
                if(validateInput(inventoryText, minText, maxText, name, costText)){
                    Products product = new Products(id, inventory, min, max, name, cost);
                    product.addAssociatedPart(associatedParts);
                    Inventory.addProduct(product);
                    stage.close();
                }
            }
        } catch (Exception e) {
            validateInput(inventoryText, minText, maxText, name, costText);
        }
    }

    private void updateTables() {
        partTable.setItems(Inventory.getAllParts());
        associatedTable.setItems(associatedParts);
    }


    /**
     * Cancels the modification and switches back to the main window.
     *
     * Errors: Disconnect between FXID and function name. Resolved by matching Name and ID.
     *
     *
     * @param event the ActionEvent that triggered the method
     * @throws IOException if there is an error switching to the main window
     */
    @FXML public void cancelButtonPressed(ActionEvent event) throws IOException {
        if (confirmDialog("Cancel", "Are you sure you would close")) {
            stage.close();
        }
    }


    /**
     * Updates the part table view with the latest data from the Inventory.
     */
    public void updatePartTable() {
        partTable.setItems(Inventory.getAllParts());
    }

    /**
     * Updates the associated parts table view with the latest data.
     */
    private void updateAssociatedPartTable() {
        associatedTable.setItems(associatedParts);
    }

    /**
     * Initializes the UI elements and table views for the Add Product window.
     *
     * @param location the URL location of the FXML file
     * @param resources the ResourceBundle used for localization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addProductID.setText(String.valueOf(getNewID()));
        orgPart = Inventory.getAllParts();

        //Same Table from Main Screen
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        partTable.setItems(orgPart);

        //Associated Parts Table
        associatedIDCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        associatedTable.setItems(associatedParts);


        updatePartTable();
        updateAssociatedPartTable();

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
            List<Integer> IDs  = getAllProducts().stream().map(Products::getProductId).collect(Collectors.toList());
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
