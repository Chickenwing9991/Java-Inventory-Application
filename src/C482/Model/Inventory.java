package C482.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Parts> allParts = FXCollections.observableArrayList();
    private static ObservableList<Products> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to the list of all parts.
     *
     * @param part the part being added to the list
     */
    public static void addPart(Parts part) {
        allParts.add(part);
    }

    /**
     * Adds a product to the list of all products.
     *
     * @param product the product being added to the list
     */
    public static void addProduct(Products product) {
        allProducts.add(product);
    }


    /**
     * Searches for a part with the specified name or ID.
     *
     * @param name the name of the part to search for
     * @param Id   the ID of the part to search for
     * @return the matching part, or null if no match is found
     * @throws IllegalArgumentException if both search terms are null
     */
    public static Parts lookupPart(String name, Integer id) {
        for (Parts part : allParts) {
            if (id != null && part.getPartId() == id) {
                return part;
            } else if (name != null && part.getName().toLowerCase().contains(name.toLowerCase())) {
                return part;
            }
        }
        return null;
    }




    /**
     * Searches for a product with the specified name or ID.
     *
     * @param name the name of the product to search for
     * @param Id   the ID of the product to search for
     * @return the matching product, or null if no match is found
     * @throws IllegalArgumentException if both search terms are null
     */
    public static Products lookupProduct(String name, Integer id) {
        for (Products product : allProducts) {
            if (id != null && product.getProductId() == id) {
                return product;
            } else if (name != null && product.getName().toLowerCase().contains(name.toLowerCase())) {
                return product;
            }
        }
        return null;
    }


    /**
     * Returns a list of all parts.
     *
     * @return the list of all parts
     */
    public static ObservableList<Parts> getAllParts() {
        return allParts;
    }

    /**
     * Returns a list of all products.
     *
     * @return the list of all products
     */
    public static ObservableList<Products> getAllProducts() {
        return allProducts;
    }

    /**
     * Updates a part at the specified index with the specified part.
     *
     * @param index        the index of the part to update
     * @param selectedPart the part to update with
     */
    public static void updatePart(int index, Parts selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product at the specified index with the specified product.
     *
     * @param index           the index of the product to update
     * @param selectedProduct the product to update with
     */
    public static void updateProduct(int index, Parts selectedProduct) {
        allParts.set(index, selectedProduct);
    }

    /**
     * Deletes a part from the list of all parts.
     *
     * @param selectedPart the part to delete
     * @return true if the part was successfully deleted, false otherwise
     */
    public static boolean deletePart(Parts selectedPart) {
        allParts.remove(selectedPart);

        return true;
    }
}
