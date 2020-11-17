/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Type {

    private Integer ID;
    private String type;
    private Integer community;
    private String active;
    private String notes;


    public Type() {
    }

    /**
     * @param ID
     * @param type
     * @param community type
     * @param active
     * @param notes
     */
    public Type(Integer ID, String type, Integer community, String active, String notes) {
        this.ID = ID;
        this.type = type;
        this.community = community;
        this.active = active;
        this.notes = notes;
        //move around for formatting
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCommunity() {
        return community;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String city) {
        this.notes = notes;
    }
}







