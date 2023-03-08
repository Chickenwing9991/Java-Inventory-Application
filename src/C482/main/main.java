package C482.main;

import C482.Model.InHouse;
import C482.Model.Inventory;
import C482.Model.Outsourced;
import C482.Model.Products;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

    // Add Parts InHouse
        Inventory.addPart(new InHouse(1, 20, 1, 50, "Logitech MX Master 3", 99.99, 35));
        Inventory.addPart(new InHouse(2, 25, 1, 50, "Corsair K70 RGB MK.2", 139.99, 22));
        Inventory.addPart(new InHouse(3, 15, 1, 50, "Seagate BarraCuda 2TB", 64.99, 48));
        Inventory.addPart(new InHouse(4, 10, 1, 50, "Intel Core i7-11700K", 429.99, 10));

    // Add Parts OutSourced
        Inventory.addPart(new Outsourced(5, 10, 1, 50, "MSI GeForce RTX 3080", 1099.99, "MSI"));
        Inventory.addPart(new Outsourced(6, 5, 1, 50, "AMD Ryzen 9 5950X", 749.99, "AMD"));
        Inventory.addPart(new Outsourced(7, 20, 1, 50, "G.Skill Ripjaws V 32GB DDR4", 179.99, "G.Skill"));
        Inventory.addPart(new Outsourced(8, 25, 1, 50, "ASUS ROG Strix X570-E", 299.99, "ASUS"));
        Inventory.addPart(new Outsourced(9, 5, 1, 50, "EVGA SuperNOVA 850W G3", 169.99, "EVGA"));
        Inventory.addPart(new Outsourced(10, 20, 1, 50, "NZXT H510i", 109.99, "NZXT"));
        Inventory.addPart(new Outsourced(11, 25, 1, 50, "Samsung 970 EVO Plus 1TB", 169.99, "Samsung"));

    // Add Products
        Inventory.addProduct(new Products(1, 10, 1, 50, "Gaming PC Bundle", 2399.99));
        Inventory.addProduct(new Products(2, 8, 1, 50, "Streaming PC Bundle", 1699.99));
        Inventory.addProduct(new Products(3, 8, 1, 50, "Home Office Bundle", 999.99));


        System.setProperty("javafx.fxml.trace", "true");
        Parent root = FXMLLoader.load(getClass().getResource("/C482/Views/MainForm.fxml"));
        Scene scene = new Scene(root, 1280, 580);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}