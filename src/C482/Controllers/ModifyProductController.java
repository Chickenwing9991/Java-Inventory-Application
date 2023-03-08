package C482.Controllers;

import C482.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

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

    private Stage stage;
    private Object scene;
    public Products selectedProduct;
    private int productID;

    public ModifyProductController(Stage stage){
        this.stage = stage;
    }

    public void setItem(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
        productID = C482.Model.Inventory.getAllProducts().indexOf(selectedProduct);
        modifyID.setText(Integer.toString(selectedProduct.getProductId()));
        modifyName.setText(selectedProduct.getName());
        modifyInv.setText(Integer.toString(selectedProduct.getStock()));
        modifyCost.setText(Double.toString(selectedProduct.getCost()));
        modifyMax.setText(Integer.toString(selectedProduct.getMax()));
        modifyMin.setText(Integer.toString(selectedProduct.getMin()));
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private boolean validateInput() {
        int productInventory = Integer.parseInt(modifyInv.getText());
        int productMin = Integer.parseInt(modifyMin.getText());
        int productMax = Integer.parseInt(modifyMax.getText());

        if (productMax < productMin) {
            mainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            return false;
        } else if (productInventory < productMin || productInventory > productMax) {
            mainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            return false;
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
    }

    private void switchToMainWindow() throws IOException {
        stage.close();
    }


}