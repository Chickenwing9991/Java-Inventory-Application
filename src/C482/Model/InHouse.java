package C482.Model;

public class InHouse extends Parts{

    private int machineID;


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



