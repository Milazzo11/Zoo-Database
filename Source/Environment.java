/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Environment {

    private Integer ID;
    private String title;
    private Double tempMax;
    private Double tempMin;
    private String notes;


    public Environment() {
    }

    /**
     *
     * @param ID
     * @param tempMax
     * @param tempMin
     * @param notes
     */
    public Environment(Integer ID, String title, Double tempMax, Double tempMin, String notes) {
        this.ID = ID;
        this.title = title;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.notes = notes;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String notes) {
        this.title = title;
    }
}








