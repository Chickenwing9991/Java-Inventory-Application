package C482.Model;

public class Outsourced extends Parts{

    private String companyName;


    public Outsourced(int partId, int stock, int min, int max, String name, double cost, String companyName){
        super(partId, stock, min, max, name, cost);

        this.companyName = companyName;
    }

    /**
     * Sets the company name.
     *
     * @param name the company name to set
     */
    public void setCompanyName(String name){
        this.companyName = name;
    }

    /**
     * Returns the company name.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}