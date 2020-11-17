/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Food {

    private Integer foodID;
    private String name; //protein, carb, fats, vitamins, minerals, water, fiber, etc.
    private Double price;
    private boolean stock;
    private String notes;

    Food() {
    }

    Food(Integer foodID, String name, Double price, boolean stock, String notes) {
        this.foodID = foodID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.notes = notes;
    }

    public Integer getFoodID(){
        return foodID;
    }

    public void setFoodID(Integer foodID) {
        this.foodID = foodID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice(){
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean getStock(){
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}











