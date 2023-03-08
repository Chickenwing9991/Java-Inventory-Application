package C482.Model;

public abstract class Parts {

    private int partId;
    private int stock;
    private int min;
    private int max;
    private String name;
    private double cost;

    public Parts(int partId, int stock, int min, int max, String name, double cost){
        this.partId = partId;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.cost = cost;
    }

    /**
     * Returns the part ID.
     *
     * @return the part ID
     */
    public int getPartId() {
        return partId;
    }

    /**
     * Sets the part ID.
     *
     * @param id the ID to set
     */
    public void setPartId(int id) {
        this.partId = id;
    }

    /**
     * Returns the part name.
     *
     * @return the part name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the part name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the part cost.
     *
     * @return the part cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the part cost.
     *
     * @param price the cost to set
     */
    public void setCost(double price) {
        this.cost = price;
    }

    /**
     * Returns the part stock level.
     *
     * @return the part stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the part stock level.
     *
     * @param stock the stock level to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the part minimum stock level.
     *
     * @return the part minimum stock level
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the part minimum stock level.
     *
     * @param min the minimum stock level to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Returns the part maximum stock level.
     *
     * @return the part maximum stock level
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the part maximum stock level.
     *
     * @param max the maximum stock level to set
     */
    public void setMax(int max) {
        this.max = max;
    }

}