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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static C482.Controllers.mainController.confirmDialog;
import static C482.Controllers.mainController.infoDialog;
import static C482.Model.Inventory.getAllParts;
import static C482.Model.Inventory.getAllProducts;

public class AddProductController implements Initializable {

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


    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Parts> orgPart = FXCollections.observableArrayList();

    private Stage stage;
    private Object scene;
    public Parts selectedPart;
    private int partID;

    @FXML
    private TextField searchParts;

    public AddProductController(Stage stage){
        this.stage = stage;
    }

    public static void infoDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static void infoDialog(String title, String content) {
        infoDialog(title, null, content);
    }

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
    private boolean validateInput(String Inv, String Min, String Max, String Name, String Cost, ObservableList<Parts> List) {
        if (Name.isEmpty() || Inv.isEmpty() || Min.isEmpty() || Max.isEmpty() || Cost.isEmpty()) {
            mainController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (Integer.parseInt(Inv) < Integer.parseInt(Min) || Integer.parseInt(Inv) > Integer.parseInt(Max)) {
            mainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (Integer.parseInt(Max) < Integer.parseInt(Min)) {
            mainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
            return false;
        } else if (!Cost.matches("[+-]?\\d*\\.?\\d+")) {
            mainController.infoDialog("Input Error", "Error in Price field", "The value must be a number");
            mainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input" );
        }

        return true;
    }


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
                if(validateInput(inventoryText, minText, maxText, name, costText, associatedParts)){
                    Products product = new Products(id, inventory, min, max, name, cost);
                    product.addAssociatedPart(associatedParts);
                    Inventory.addProduct(product);
                    stage.close();
                }
            }
        } catch (Exception e) {
            validateInput(inventoryText, minText, maxText, name, costText, associatedParts);
        }
    }

    private void updateTables() {
        partTable.setItems(Inventory.getAllParts());
        associatedTable.setItems(associatedParts);
    }

    @FXML public void cancelButtonPressed(ActionEvent event) throws IOException {
        if (confirmDialog("Cancel", "Are you sure you would close")) {
            stage.close();
        }
    }
    public void updatePartTable() {
        partTable.setItems(Inventory.getAllParts());
    }

    private void updateAssociatedPartTable() {
        associatedTable.setItems(associatedParts);
    }


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

    public static int getNewID(){
        int newID = 1;
        for (int i = 0; i < getAllProducts().size(); i++) {
            newID++;
        }
        return newID;
    }

}
