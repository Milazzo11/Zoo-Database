/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Land extends Environment{

    private String biome;
    private Double humidity;
    private Double minArea;
    private Double minHeight;

    public Land() {
    }

    /**
     * Constructor for Land, Ideally when one is blank it just gets sent as blank but it is still sent to the same
     * method, but we could technically make methods for when variables are missing
     * @param biome the biome name
     * @param humidity land humidity
     * @param minArea minimum land area
     * @param minHeight minimum land height
     */
    public Land(Integer ID, String title, Double tempMax, Double tempMin, String notes, String biome,
                Double humidity, Double minArea, Double minHeight) {

        super(ID, title, tempMax, tempMin, notes);

        this.biome = biome;
        this.humidity = humidity;
        this.minArea = minArea;
        this.minHeight = minHeight;
        //move around for formatting
    }

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getMinArea() {
        return minArea;
    }

    public void setMinArea(Double minArea) {
        this.minArea = minArea;
    }

    public Double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Double minHeight) {
        this.minHeight = minHeight;
    }

}







