/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Animal {

    private Integer ID;
    private String userID;
    private String name;
    private String age;
    private String sex;
    private String location;
    private String notes;


    public Animal() {
    }

    /**
     * Constructor for Animal, Ideally when one is blank it just gets sent as blank but it is still sent to the same
     * method, but we could technically make methods for when variables are missing
     * @param ID the animal id number
     * @param name the animal name
     * @param location animal location
     * @param notes animal notes
     * @param sex animal sex
     * @param age animal age
     */
    public Animal(Integer ID, String userID, String name, String age, String sex, String location, String notes) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.location = location;
        this.notes = notes;
        //move around for formatting
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}









