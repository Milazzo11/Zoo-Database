/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Aqua extends Environment {

    //private Environment environment;
    private Double salinity;
    private Double pH;
    private Double volume;
    private Double oxygen;

    public Aqua() {
    }

    /**
     * Constructor for Aqua, Ideally when one is blank it just gets sent as blank but it is still sent to the same
     * method, but we could technically make methods for when variables are missing
     * @param salinity the animal salinity
     * @param pH preferred water pH level
     * @param volume enclosure size
     * @param oxygen dissolved oxygen levels in water
     */
    public Aqua(Integer ID, String title, Double tempMax, Double tempMin, String notes, Double salinity, Double pH, Double volume, Double oxygen) {
        super(ID, title, tempMax, tempMin, notes);

        this.salinity = salinity;
        this.pH = pH;
        this.volume = volume;
        this.oxygen = oxygen;
    }

    public Double getSalinity() {
        return salinity;
    }

    public void setSalinity(Double salinity) {
        this.salinity = salinity;
    }

    public Double getPH() {
        return pH;
    }

    public void setPH(Double pH) {
        this.pH = pH;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getOxygen() {
        return oxygen;
    }

    public void setOxygen(Double oxygen) {
        this.oxygen = oxygen;
    }
}









