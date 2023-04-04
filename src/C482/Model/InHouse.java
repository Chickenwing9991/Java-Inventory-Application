package C482.Model;

/**
 * InHouse Class Inherits from Parts Class.
 * Used when Inhouse radio button is selected.
 */
public class InHouse extends Parts{

    /**
     * Creating Machine ID Variable for Later Use.
     */
    private int machineID;


    /**
     * InHouse Class Constructor
     * @param partId Part ID Value
     * @param stock Stock Value
     * @param min Min Value
     * @param max Max Value
     * @param name Name Value
     * @param cost Cost Value
     * @param machineID Machine ID Value
     */
    public InHouse(int partId, int stock, int min, int max, String name, double cost, int machineID){
        super(partId, stock, min, max, name, cost);

        this.machineID = machineID;
    }

    /**
     * Sets the machine ID.
     *
     * @param Id the machine ID to set
     */
    public void setMachineId(int Id){
        this.machineID = Id;
    }

    /**
     * Returns the machine ID.
     *
     * @return the machine ID
     */
    public int getMachineID(){
        return machineID;
    }
}



