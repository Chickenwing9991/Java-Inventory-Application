package C482.Controllers;

import C482.Model.*;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static C482.Controllers.mainController.confirmDialog;
/**
 * The ModifyProductController class is responsible for handling events and managing the UI
 * for modifying a product in the Inventory Management System.
 *
 * This class provides methods for setting the product to be modified, updating the associated parts
 * table, removing a part from the product, canceling the modification, validating user input,
 * and saving the modified product.
 *
 * The ModifyProductController implements the Initializable interface, which requires an initialize
 * method that sets up the UI elements and table views.
 */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField modifyProductID;
    @FXML
    private TextField modifyProductName;
    @FXML
    private TextField modifyProductInv;
    @FXML
    private TextField modifyProductCost;
    @FXML
    private TextField modifyProductMax;
    @FXML
    private TextField modifyProductMin;
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
    private TableView<Parts> partTable;
    @FXML
    private TableColumn<Parts, Integer> partIDCol;
    @FXML
    private TableColumn<Parts, String> partNameCol;
    @FXML
    private TableColumn<Parts, Integer> partInvCol;
    @FXML
    private TableColumn<Parts, Double> partCostCol;

    private Stage stage;
    private Object scene;
    public Products selectedProduct;
    private int productID;

    @FXML
    private TextField searchParts;

    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Parts> orgPart = FXCollections.observableArrayList();

    private Products modProduct;


    /**
     * Constructs a new ModifyProductController with the given Stage.
     *
     * @param stage the Stage to use for the Modify Product window
     */
    public ModifyProductController(Stage stage){
        this.stage = stage;
    }

    /**
     * Sets the product to be modified and populates the UI elements with its data.
     *
     * @param selectedProduct the product to be modified
     */
    public void setItem(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
        productID = C482.Model.Inventory.getAllProducts().indexOf(selectedProduct);
        modifyProductID.setText(Integer.toString(selectedProduct.getProductId()));
        modifyProductName.setText(selectedProduct.getName());
        modifyProductInv.setText(Integer.toString(selectedProduct.getStock()));
        modifyProductCost.setText(Double.toString(selectedProduct.getCost()));
        modifyProductMax.setText(Integer.toString(selectedProduct.getMax()));
        modifyProductMin.setText(Integer.toString(selectedProduct.getMin()));
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
    }

    /**
     * Updates the part and associated parts table views with the latest data.
     */
    private void updateTables() {
        partTable.setItems(Inventory.getAllParts());
        associatedTable.setItems(associatedParts);
    }


    /**
     * Removes the selected part from the associated parts table.
     *
     * @param event the ActionEvent that triggered the method
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
            mainController.infoDialog("No Selection", "Please choose something to remove");
        }
    }

    /**
     * Cancels the modification and switches back to the main window.
     *
     * @param event the ActionEvent that triggered the method
     * @throws IOException if there is an error switching to the main window
     */
    @FXML
    public void cancelButtonPressed(ActionEvent event) throws IOException {
        if (mainController.confirmDialog("Cancel?", "Are you sure?")) {
            switchToMainWindow();
        }
    }



    /**
     * Initializes the UI elements and table views for the Modify Product window.
     *
     * @param location the URL location of the FXML file
     * @param resources the ResourceBundle used for localization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
            mainController.infoDialog("Select a part", "Select a part to add to the Product");
        }
    }


    /**
    * Handles the save button press event.
    * @param event the ActionEvent that triggered the method
    * @throws IOException if there is an error switching to the main window
    */
    @FXML void saveButtonPressed(ActionEvent event) throws IOException {

            String name = modifyProductName.getText();
            String inventoryText = modifyProductInv.getText();
            String minText = modifyProductMin.getText();
            String maxText = modifyProductMax.getText();
            String costText = modifyProductCost.getText();

            try {

                int productInventory = Integer.parseInt(modifyProductInv.getText());
                int productMinimum = Integer.parseInt(modifyProductMin.getText());
                int productMaximum = Integer.parseInt(modifyProductMax.getText());
                double productCost = Double.parseDouble(modifyProductCost.getText());
                String productName = modifyProductName.getText();

                if (mainController.confirmDialog("Save?", "Would you like to save this part?")) {
                    if (validateInput(inventoryText, minText, maxText, name, costText)) {
                        this.modProduct = selectedProduct;
                        selectedProduct.setProductId(productID);
                        selectedProduct.setName(productName);
                        selectedProduct.setStock(productInventory);
                        selectedProduct.setCost(productCost);
                        selectedProduct.setMax(productMaximum);
                        selectedProduct.setMin(productMinimum);
                        selectedProduct.getAllAssociatedParts().clear();
                        selectedProduct.addAssociatedPart(associatedParts);
                        C482.Model.Inventory.updateProduct(productID, selectedProduct);

                        switchToMainWindow();
                    }
                }
            } catch (Exception e){
                System.out.println(e);
                validateInput(inventoryText, minText, maxText, name, costText);
            }
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
                    mainController.infoDialog("No Match", "Unable to locate a part with ID: " + partId);
                }
            } catch (NumberFormatException e) {
                Parts foundPart = Inventory.lookupPart(searchTerm, null);
                if (foundPart != null) {
                    setItemsTableView(FXCollections.observableArrayList(foundPart), "part");
                } else {
                    mainController.infoDialog("No Match", "Unable to locate a part with name: " + searchTerm);
                }
            }
        }

        else {
            ObservableList<Parts> allparts = Inventory.getAllParts();
            setItemsTableView(allparts, "part");
        }
    }

    /**
     * Switches back to the main window.
     *
     * @throws IOException if there is an error switching to the main window
     */
    private void switchToMainWindow() throws IOException {
        stage.close();
    }


}