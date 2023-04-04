package C482.Controllers;

import C482.Model.Inventory;
import C482.Model.Parts;
import C482.Model.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class mainController implements Initializable {

    // Parts Table
    @FXML
    private TableView<Parts> partTableView;
    @FXML
    private TableColumn<Parts, Integer> partIDCol;
    @FXML
    private TableColumn<Parts, String> partNameCol;
    @FXML
    private TableColumn<Parts, Integer> partInvLevelCol;
    @FXML
    private TableColumn<Parts, Double> partCostUnitCol;
    @FXML
    private TextField searchParts;

    // Products Table
    @FXML
    private TableView<Products> productTableView;
    @FXML
    private TableColumn<Products, Integer> productIdCol;
    @FXML
    private TableColumn<Products, String> productNameCol;
    @FXML
    private TableColumn<Products, Integer> productInvLevelCol;
    @FXML
    private TableColumn<Products, Double> productCostUnitCol;
    @FXML
    private TextField searchProducts;

    // Buttons and Fields
    private Parent scene;

    /**
     * @param event
     * Triggers when the add button under the Parts table is clicked.
     * A screen that allows you to enter the details for the new part appears.
     *
     * Runtime Error: Was not passing in the controller correctly. Causing buttons not to work.
     *
     */
    @FXML public void addPartPushed(ActionEvent event) throws IOException {
        openScene("Add Part", "/C482/Views/AddPart.fxml", AddPartController.class);
    }

    /**
     *
     * @param event
     * @throws IOException
     *
     * Is Triggered when the Modify Part button is pressed.
     * Future Enhancement: Combine modifiedPushed function into 1 dynamic function.
     *
     *
     */
    @FXML public void modifyPartPushed(ActionEvent event) throws IOException {
        Parts selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            return;
        }

        openSceneWithController("Modify Part", "/C482/Views/ModifyPart.fxml", ModifyPartController.class, selectedPart);
    }


    /**
     *
     * @param event
     * @throws IOException
     *
     * Is Triggered when the Modify Product button is pressed.
     * Future Enhancement: Combine modifiedPushed function into 1 dynamic function.
     *
     *
     */
    @FXML public void modifyProductPushed(ActionEvent event) throws IOException {
        Products selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        openSceneWithController("Modify Product", "/C482/Views/ModifyProduct.fxml", ModifyProductController.class, selectedProduct);
    }


    /**
     *
     * @param event
     * Triggers when enter is pressed while the search box is focused. Searches for Part Name or Product ID
     * of a part then returns the matching results.
     *
     * Runtime Error: Originally didnt convert the productId to int which caused a casting error when searching.
     * Future Enhancement: Add the ability to search in all columns for a match from the search field.
     *
     *
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
     * Triggers when enter is pressed while the search box is focused. Searches for Product Name or Product ID
     * of a product then returns the matching results.
     *
     * Runtime Error: Originally didnt convert the productId to int which caused a casting error when searching.
     * Future Enhancement: Add the ability to search in all columns for a match from the search field.
     *
     *
     */
    @FXML
    public void searchProductPushed(ActionEvent event) {
        String searchTerm = searchProducts.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                int productId = Integer.parseInt(searchTerm);
                Products foundProduct = Inventory.lookupProduct(null, productId);
                if (foundProduct != null) {
                    setItemsTableView(FXCollections.observableArrayList(foundProduct), "part");
                } else {
                    infoDialog("No Match", "Unable to locate a part with ID: " + productId);
                }
            } catch (NumberFormatException e) {
                Products foundProduct = Inventory.lookupProduct(searchTerm, null);
                if (foundProduct != null) {
                    setItemsTableView(FXCollections.observableArrayList(foundProduct), "product");
                } else {
                    infoDialog("No Match", "Unable to locate a product with name: " + searchTerm);
                }
            }
        }

        else {
            ObservableList<Products> allproducts = Inventory.getAllProducts();
            setItemsTableView(allproducts, "product");
        }
    }

    /**
     * @param event
     * Triggers when the delete button under the Parts table is clicked.
     *
     * Runtime Error: Misspelled button reference.
     *
     */
    @FXML public void deletePartPushed(ActionEvent event) {
        deleteItem(partTableView, "part");
    }

    /**
     * @param event
     * Triggers when the delete button under the Products table is clicked.
     *
     * Runtime Error: Misspelled button reference.
     *
     */
    @FXML public void deleteProductPushed(ActionEvent event) {
        deleteItem(productTableView, "product");
    }


    /**
     * @param event
     * Triggers when the add button under the Products table is clicked.
     * A screen that allows you to enter the details for the new product appears.
     *
     * Runtime Error: Was not passing in the controller correctly. Causing buttons not to work.
     *
     */
    @FXML private void addProductPushed(ActionEvent event) throws IOException {
        openScene("Add Product", "/C482/Views/AddProduct.fxml", AddProductController.class);
    }

    /**
     *
     * @param event
     *
     * Closes the Application
     */
    @FXML public void exitButtonPushed(ActionEvent event) {
        if (confirmDialog("Exit", "Are you sure you would like to exit the program?")) {
            System.exit(0);
        }
    }


    /**
     *
     * @param location
     * @param resources
     *
     * Initializes Both Parts and Product Table.
     *
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializePartsTable(partTableView, partIDCol, partNameCol, partInvLevelCol, partCostUnitCol, Inventory.getAllParts());
        initializeProductTable(productTableView, productIdCol, productNameCol, productInvLevelCol, productCostUnitCol, Inventory.getAllProducts());
    }

    /**
     *
     * @param title
     * @param fxmlFile
     * @param controllerClass
     * @param <T>
     * @throws IOException
     *
     *
     * Handles passing of class instance Controller to corresponding screen.
     * In this case the class is only use for passing instance of Stage object to be able to destory later
     *
     *
     *
     */
    private <T> void openScene(String title, String fxmlFile, Class<T> controllerClass) throws IOException {
        T controller;
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            T newClass = controllerClass.getDeclaredConstructor(Stage.class).newInstance(stage);

            loader.setController(newClass);
            controller = loader.getController();
            scene = loader.load();


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();
    }


    /**
     *
     * @param title
     * @param fxmlFile
     * @param controllerClass
     * @param selectedItem
     * @param <T>
     * @throws IOException
     *
     * Handles passing of class instance Controller to corresponding screen.
     *
     */
    private <T> void openSceneWithController(String title, String fxmlFile, Class<T> controllerClass, Object selectedItem) throws IOException {
        T controller = null;
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            T newClass = controllerClass.getDeclaredConstructor(Stage.class).newInstance(stage);
            loader.setController(newClass);
            controller = loader.getController();

            System.out.println(controller);
            scene = loader.load();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to load FXML file: " + fxmlFile, e);
            return;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (controllerClass.isInstance(controller)) {
            try {
                Object itemClass = null;

                if(selectedItem instanceof Parts) {
                    itemClass = Parts.class;
                }

                if(selectedItem instanceof Products){
                    itemClass = Products.class;
                }

                Method setItemMethod = controllerClass.getMethod("setItem", (Class<T>) itemClass);
                setItemMethod.invoke(controller, selectedItem);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to set item for controller: " + controllerClass.getName(), e);
            }
        } else {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Incompatible controller class for FXML file: " + fxmlFile);
        }

        stage.setTitle(title);
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     *
     * @param tableView
     * @param itemType
     *
     * Takes two inputs. The specific table view and item type.
     * Then gets the currently selected row and delete the item.
     *
     *
     */
    private void deleteItem(TableView<?> tableView, String itemType) {
        if (tableView.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No " + itemType + " Selected", "Please choose " + itemType + " from the above list");
            return;
        }
        if (confirmDialog("Warning!", "Would you like to delete this " + itemType + "?")) {
            if(itemType == "product"){
                Products selectedProduct = (Products) tableView.getSelectionModel().getSelectedItem();
                if(selectedProduct.getAllAssociatedParts().isEmpty()){
                    tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                }
                else{
                    infoDialog("Deletion Error", "Associated Part Issue", "You cannot delete a product with an existing associated part");
                }
            }

            if(itemType == "part"){
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
            }
        }
    }


    /**
     *
     * @param tableView
     * @param idColumn
     * @param nameColumn
     * @param invLevelColumn
     * @param costColumn
     * @param items
     * @param <T>
     *
     *
     * Initializes the Parts Table. Adds the Columns for the Part objects.
     * Then the table view is ready to display the parts.
     *
     */
    private <T> void initializePartsTable(TableView<T> tableView, TableColumn<T, Integer> idColumn, TableColumn<T, String> nameColumn, TableColumn<T, Integer> invLevelColumn, TableColumn<T, Double> costColumn, ObservableList<T> items) {
        if (tableView != null) {
            tableView.setItems(items);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        }
    }


    /**
     *
     * @param tableView
     * @param idColumn
     * @param nameColumn
     * @param invLevelColumn
     * @param costColumn
     * @param items
     * @param <T>
     *
     *
     * Initializes the Parts Table. Adds the Columns for the Part objects.
     * Then the table view is ready to display the parts.
     *
     */
    private <T> void initializeProductTable(TableView<T> tableView, TableColumn<T, Integer> idColumn, TableColumn<T, String> nameColumn, TableColumn<T, Integer> invLevelColumn, TableColumn<T, Double> costColumn, ObservableList<T> items) {
        if (tableView != null) {
            tableView.setItems(items);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        }
    }


    /**
     *
     * @param items
     * @param itemType
     * @param <T>
     *
     *
     * Assigns the Corresponding Observable Lists to the table views.
     *
     */
    private <T> void setItemsTableView(ObservableList<T> items, String itemType) {
        if (itemType.equals("part")) {
            partTableView.setItems((ObservableList<Parts>) items);
        } else if (itemType.equals("product")) {
            productTableView.setItems((ObservableList<Products>) items);
        } else {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.WARNING, "Unknown item type: " + itemType);
        }
    }

    /**
     *
     * @param title
     * @param content
     *
     *
     * Creates a popup for user confirmation.
     *
     *
     * @return bool
     */
    public static boolean confirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     *
     * @param title
     * @param header
     * @param content
     *
     *
     * Creates a popup to show user infomation.
     *
     */
    public static void infoDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param title
     * @param content
     *
     * Creates a popup to show user infomation.
     */
    public static void infoDialog(String title, String content) {
        infoDialog(title, null, content);
    }
}