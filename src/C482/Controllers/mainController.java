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

    // Event Handlers
    @FXML public void addPartPushed(ActionEvent event) throws IOException {
        openScene("Add Part", "/C482/Views/AddPart.fxml", AddPartController.class);
    }

    @FXML public void modifyPartPushed(ActionEvent event) throws IOException {
        Parts selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            return;
        }

        openSceneWithController("Modify Part", "/C482/Views/ModifyPart.fxml", ModifyPartController.class, selectedPart);
    }



    @FXML public void modifyProductPushed(ActionEvent event) throws IOException {
        Products selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
        openSceneWithController("Modify Product", "/C482/Views/ModifyProduct.fxml", ModifyProductController.class, selectedProduct);
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

    @FXML public void deletePartPushed(ActionEvent event) {
        deleteItem(partTableView, "part");
    }

    @FXML public void deleteProductPushed(ActionEvent event) {
        deleteItem(productTableView, "product");
    }

    @FXML private void addProductPushed(ActionEvent event) throws IOException {
        openScene("Add Product", "/C482/Views/AddProduct.fxml", AddProductController.class);
    }

    @FXML public void exitButtonPushed(ActionEvent event) {
        if (confirmDialog("Exit", "Are you sure you would like to exit the program?")) {
            System.exit(0);
        }
    }


    // Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializePartsTable(partTableView, partIDCol, partNameCol, partInvLevelCol, partCostUnitCol, Inventory.getAllParts());
        initializeProductTable(productTableView, productIdCol, productNameCol, productInvLevelCol, productCostUnitCol, Inventory.getAllProducts());
    }

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


    private void searchAndDisplayResults(String term, ObservableList results, String itemType) {
        if (results == null) {
            infoDialog("No Match", "Unable to locate a " + itemType + " name with: " + term);
        } else {
            setItemsTableView(results, itemType);
        }
    }

    private void deleteItem(TableView<?> tableView, String itemType) {
        if (tableView.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No " + itemType + " Selected", "Please choose " + itemType + " from the above list");
            return;
        }
        if (confirmDialog("Warning!", "Would you like to delete this " + itemType + "?")) {
            tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
        }
    }

    private <T> void initializePartsTable(TableView<T> tableView, TableColumn<T, Integer> idColumn, TableColumn<T, String> nameColumn, TableColumn<T, Integer> invLevelColumn, TableColumn<T, Double> costColumn, ObservableList<T> items) {
        if (tableView != null) {
            tableView.setItems(items);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        }
    }

    private <T> void initializeProductTable(TableView<T> tableView, TableColumn<T, Integer> idColumn, TableColumn<T, String> nameColumn, TableColumn<T, Integer> invLevelColumn, TableColumn<T, Double> costColumn, ObservableList<T> items) {
        if (tableView != null) {
            tableView.setItems(items);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        }
    }


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


    public static boolean confirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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
}