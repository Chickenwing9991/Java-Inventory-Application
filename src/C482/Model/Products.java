package C482.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Products {

    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();
    private int Id;
    private int stock;
    private int min;
    private int max;
    private String name;
    private double cost;

    public Products(int Id, int stock, int min, int max, String name, double cost) {
        this.Id = Id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.cost = cost;
    }

    public Products() {
        this(0, 0, 0, 0, null, 0.00);
    }

    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public int getProductId() {
        return this.Id;
    }

    /**
     * Sets the product ID.
     *
     * @param Id the ID to set
     */
    public void setProductId(int Id) {
        this.Id = Id;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product price.
     *
     * @return the product price
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the product price.
     *
     * @param price the price to set
     */
    public void setCost(double price) {
        this.cost = price;
    }

    /**
     * Returns the product stock level.
     *
     * @return the product stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the product stock level.
     *
     * @param stock the stock level to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the product minimum stock level.
     *
     * @return the product minimum stock level
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the product minimum stock level.
     *
     * @param min the minimum stock level to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Returns the product maximum stock level.
     *
     * @return the product maximum stock level
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the product maximum stock level.
     *
     * @param max the maximum stock level to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds a part to the list of associated parts for this product.
     *
     * @param part the part to add
     */
    public void addAssociatedPart(ObservableList<Parts> part) {
        this.associatedParts.addAll(part);
    }

    /**
     * Returns a list of all associated parts for this product.
     *
     * @return the list of associated parts
     */
    public ObservableList<Parts> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Deletes a part from the list of associated parts for this product.
     *
     * @param selectedPart the part to delete
     * @return true if the part was successfully deleted, false otherwise
     */
    public boolean deletePart(Parts selectedPart) {
        this.associatedParts.remove(selectedPart);

        return true;
    }
}