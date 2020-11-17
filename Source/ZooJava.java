import javafx.scene.control.Alert;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */

public class ZooJava {
    private static final String ZOO_DB = "jdbc:sqlite:ZooDB.db";
    private static final String ANIMAL_SQL = "SELECT * FROM ANIMAL WHERE UserID LIKE ? OR Name LIKE ?";
    private static final String FOOD_SQL = "SELECT * FROM FOOD WHERE Name LIKE ?";
    private static final String FOOD_ALL_SQL = "SELECT * FROM FOOD";
    private static final String TYPE_SQL = "SELECT * FROM TYPE WHERE Type LIKE ?";
    private static final String TYPE_ALL_SQL = "SELECT * FROM TYPE";
    private static final String AQUA_SQL = "SELECT * FROM AQUA WHERE Title LIKE ?";
    private static final String AQUA_ALL_SQL = "SELECT * FROM AQUA";
    private static final String LAND_SQL = "SELECT * FROM LAND WHERE Title LIKE ? OR BIOME LIKE ?";
    private static final String LINK_ANIMAL_TYPE_SQL = "INSERT INTO AnimalType (animalID, typeID) VALUES (?,?)";

    private static final String ANIMAL_ID_SQL = "SELECT * FROM ANIMAL WHERE AnimalID=?";
    private static final String FOOD_ID_SQL = "SELECT * FROM FOOD WHERE FoodID=?";
    private static final String TYPE_ID_SQL = "SELECT * FROM TYPE WHERE TypeID=?";
    private static final String LAND_ID_SQL = "SELECT * FROM LAND WHERE LandID=?";
    private static final String AQUA_ID_SQL = "SELECT * FROM AQUA WHERE AquaID=?";

    private static final String DELETE_ANIMAL_SQL = "DELETE FROM animal WHERE AnimalID=?";
    private static final String DELETE_TYPE_FOOD_LINK_SQL = "DELETE FROM TypeFood WHERE TypeID=?";
    private static final String DELETE_TYPE_ENVIRON_LINK_SQL = "DELETE FROM TypeEnvironment WHERE TypeID=?";
    private static final String DELETE_ANIMAL_TYPE_LINK_SQL = "DELETE FROM AnimalType WHERE AnimalID=?";
    private static final String DELETE_ANIMAL_NEEDS_LINK_SQL = "DELETE FROM SpecialNeeds WHERE AnimalID=?";

    private static final String DELETE_SPECIAL_NEEDS_SQL = "DELETE FROM SpecialNeeds WHERE AnimalID=?";

    private static final String DELETE_FOOD_SQL = "DELETE FROM food WHERE FoodID=?";
    private static final String DELETE_TYPE_SQL = "DELETE FROM type WHERE TypeID=?";
    private static final String DELETE_LAND_SQL = "DELETE FROM land WHERE LandID=?";
    private static final String DELETE_AQUA_SQL = "DELETE FROM aqua WHERE AquaID=?";

    private static final String LINK_TYPE_FOOD_SQL = "INSERT INTO TypeFood (typeID, foodID) VALUES (?,?)";
    private static final String LINK_TYPE_ENVIRONMENT_SQL = "INSERT INTO TypeEnvironment (typeID, environID, environType) VALUES (?,?,?)";
    private static final String LINK_ANIMAL_SPECIAL_NEEDS_SQL = "INSERT INTO SpecialNeeds (animalID, needOption, description) VALUES(?,?,?)";

    private static final String INSERT_INTO_ANIMAL_SQL = "INSERT INTO Animal(AnimalID, UserID, Name, Age, Sex, Location, Notes) VALUES(?,?,?,?,?,?,?)";
    private static final String INSERT_INTO_TYPE_SQL = "INSERT INTO Type(typeID, Type, Community, Active, Notes) VALUES(?,?,?,?,?)";
    private static final String INSERT_INTO_FOOD_SQL = "INSERT INTO Food(foodID, name, price, InStock, notes) VALUES(?,?,?,?,?)";
    private static final String INSERT_INTO_LAND_SQL = "INSERT INTO Land(LandID, Title, Biome, TempMax, TempMin, humidity, MinHeight, MinArea, Notes) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String INSET_INTO_AQUA_SQL = "INSERT INTO Aqua(aquaID, Title, Salinity, pH, TempMax, TempMin, Volume, Oxygen, notes) VALUES(?,?,?,?,?,?,?,?,?)";

    private static final String UPDATE_ANIMAL_TYPE_SQL = "UPDATE AnimalType SET TypeID=? WHERE AnimalID=?";
    private static final String UPDATE_ANIMAL_SQL = "UPDATE animal SET UserID=?, Name=?, Age=?, Sex=?, Location=?, Notes=? WHERE AnimalID=?";
    private static final String UPDATE_TYPE_SQL = "UPDATE TYPE SET Type=?, Community=?, Active=?, Notes=? WHERE TypeID=?";
    private static final String UPDATE_FOOD_SQL = "UPDATE food SET Name=?, Price=?, InStock=?, Notes=? WHERE FoodID=?";
    private static final String UPDATE_LAND_SQL = "UPDATE land SET Title=?, Biome=?, TempMax=?, TempMin=?, Humidity=?, MinHeight=?, MinArea=?, Notes=? WHERE LandID=?";
    private static final String UPDATE_AQUA_SQL = "UPDATE aqua SET Title=?, Salinity=?, pH=?, TempMax=?, TempMin=?, Volume=?, Oxygen=?, notes=? WHERE aquaID=?";

    private static final String GET_SEXOPTIONS = "SELECT Option FROM SEXOPTIONS";
    private static final String GET_ACTIVEOPTIONS = "SELECT Option FROM ACTIVEOPTIONS";
    private static final String GET_SPECIALNEEDSOPTIONS = "SELECT Condition FROM SPECIALNEEDSOPTIONS";

    /**
     * connects to a database named ZooDB.db
     * @return
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(ZOO_DB);

    }//getConnection

    /**
     * Connect to the ZooDB.db database
     *
     * @return the Connection object
     */
    private Connection connect() {

        Connection conn = null;

        try {
            conn = getConnection();
        } catch (SQLException e) {
            error(e.getMessage());
        }

        return conn;
    }

    /**
     * readAnimalBasics reads and returns animal arrayList from the ZooDB db
     *
     * @returns ArrayList of animals
     */
    public ArrayList<Animal> readAnimals(String query) {

        ArrayList<Animal> animals = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(ANIMAL_SQL);

            stmt.setString(1, query + "%");
            stmt.setString(2, query + "%");


            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                animals.add(new Animal(

                        rs.getInt("AnimalID"),
                        rs.getString("UserID"),
                        rs.getString("Name"),
                        rs.getString("Age"),
                        rs.getString("Sex"),
                        rs.getString("Location"),
                        rs.getString("Notes")
                ));

            }// while

        }// try

        catch (SQLException e) {
            error(e.getMessage());
        }
        return animals;

    }// readAnimals

    public ArrayList<Food> readFoods(String query) {

        ArrayList<Food> foods = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt;

            if (!query.equals("")) {
                stmt = conn.prepareStatement(FOOD_SQL);
                stmt.setString(1, query + "%");
            } else
                stmt = conn.prepareStatement(FOOD_ALL_SQL);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                foods.add(new Food(
                        rs.getInt("FoodID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getBoolean("InStock"),
                        rs.getString("Notes")
                ));

            }// while

        }// try
        catch (SQLException e) {
            error(e.getMessage());
        }
        return foods;

    }// readFoods

    public ArrayList<Type> readTypes(String query) {

        ArrayList<Type> types = new ArrayList<>();

        try {
            Connection conn = getConnection();
            PreparedStatement stmt;

            if (!query.equals("")) {
                stmt = conn.prepareStatement(TYPE_SQL);
                stmt.setString(1, query + "%");
            } else
                stmt = conn.prepareStatement(TYPE_ALL_SQL);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                types.add(new Type(

                        rs.getInt("TypeID"),
                        rs.getString("Type"),
                        rs.getInt("Community"),
                        rs.getString("Active"),
                        rs.getString("Notes")
                ));
            }// while
        }// try
        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return types;
    }// readTypes

    public ArrayList<Land> readLands(String query) {

        ArrayList<Land> lands = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(LAND_SQL);

            stmt.setString(1, query + "%");
            stmt.setString(2, query + "%");

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                lands.add(new Land(
                        rs.getInt("LandID"),
                        rs.getString("Title"),
                        rs.getDouble("TempMax"),
                        rs.getDouble("TempMin"),
                        rs.getString("Notes"),
                        rs.getString("Biome"),
                        rs.getDouble("Humidity"),
                        rs.getDouble("MinHeight"),
                        rs.getDouble("MinArea")
                ));
            }// while
        }// try
        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return lands;
    }// readLands


    public ArrayList<Aqua> readAquas(String query, boolean isSearch) {

        ArrayList<Aqua> aquas = new ArrayList<>();

        try {
            Connection conn = getConnection();
            PreparedStatement stmt;




            if (!("aquatic".contains(query.toLowerCase()))) {

                stmt = conn.prepareStatement(AQUA_SQL);
                stmt.setString(1, query + "%");

            } else if (isSearch)
                stmt = conn.prepareStatement(AQUA_ALL_SQL);

            else {

                stmt = conn.prepareStatement(AQUA_SQL);
                stmt.setString(1, query + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                aquas.add(new Aqua(
                        rs.getInt("AquaID"),
                        rs.getString("Title"),
                        rs.getDouble("TempMax"),
                        rs.getDouble("TempMin"),
                        rs.getString("Notes"),
                        rs.getDouble("Salinity"),
                        rs.getDouble("pH"),
                        rs.getDouble("Volume"),
                        rs.getDouble("Oxygen")
                ));
            }// while
        }// try
        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return aquas;
    }// readAquas

    public ArrayList<String> readSexOptions() {

        ArrayList<String> sexOptions = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(GET_SEXOPTIONS);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                sexOptions.add(
                        rs.getString("Option")
                );

            }// while

        }// try

        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return sexOptions;

    }// readSexOptions

    public ArrayList<String> readActiveOptions() {

        ArrayList<String> activeOptions = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(GET_ACTIVEOPTIONS);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                activeOptions.add(
                        rs.getString("Option")
                );

            }// while

        }// try

        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return activeOptions;

    }// readActiveOptions


    public ArrayList<String> readSpecialNeedsOptions() {

        ArrayList<String> specialNeedsOptions = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(GET_SPECIALNEEDSOPTIONS);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                specialNeedsOptions.add(
                        rs.getString("Condition")
                );

            }// while

        }// try

        catch (SQLException e) {
            error(e.getMessage());
            //e.printStackTrace();
        }
        return specialNeedsOptions;

    }// readActiveOptions

    //0 for Animal
    // 1 for Food
    // 2 for Type
    // 3 for Land
    // 4 (or anything else) for Aqua
    public boolean readIDs(String query, int i){

        if(i == 0){

            try {
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(ANIMAL_ID_SQL);

                stmt.setString(1, query);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){//if rs.next() == true meaning that another animal shares the animal ID
                    return true;

                }else{
                    return false;
                }

            }// try

            catch (SQLException e) {
                error(e.getMessage());
                //e.printStackTrace();
            }
            return false;

        }else if(i == 1){

            try {
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(FOOD_ID_SQL);

                stmt.setString(1, query);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){//if rs.next() == true meaning that another animal shares the animal ID
                    return true;

                }else{
                    return false;
                }

            }// try

            catch (SQLException e) {
                error(e.getMessage());
                //e.printStackTrace();
            }
            return false;

        }else if(i == 2){

            try {
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(TYPE_ID_SQL);

                stmt.setString(1, query);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){//if rs.next() == true meaning that another animal shares the animal ID
                    return true;

                }else{
                    return false;
                }

            }// try

            catch (SQLException e) {
                error(e.getMessage());
                //e.printStackTrace();
            }
            return false;

        }else if(i == 3){

            try {
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(LAND_ID_SQL);

                stmt.setString(1, query);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){//if rs.next() == true meaning that another animal shares the animal ID
                    return true;

                }else{
                    return false;
                }

            }// try

            catch (SQLException e) {
                error(e.getMessage());
                //e.printStackTrace();
            }
            return false;

        }else{

            try {
                Connection conn = getConnection();

                PreparedStatement stmt = conn.prepareStatement(AQUA_ID_SQL);

                stmt.setString(1, query);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){//if rs.next() == true meaning that another animal shares the animal ID
                    return true;

                }else{
                    return false;
                }

            }// try

            catch (SQLException e) {
                error(e.getMessage());
                //e.printStackTrace();
            }
            return false;

        }

    }


    /**
     * Insert a new row into the animal table table
     */
    private void insertAnimalMethod(Integer animalID, String userID, String name, String age, String sex, String location, String notes) {

        try  {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_ANIMAL_SQL);
            pstmt.setInt(1, animalID);
            pstmt.setString(2, userID);
            pstmt.setString(3, name);
            pstmt.setString(4, age);
            pstmt.setString(5, sex);
            pstmt.setString(6, location);
            pstmt.setString(7, notes);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage()); System.out.println(e);
        }
    }

    /**
     * This method inserts and animal and will link the animal to type
     */
    public void insertAnimal(Integer animalID, String userID, Integer typeID, String name, String age, String sex, String location, String notes, HashMap<String, String> specialNeeds){
        insertAnimalMethod(animalID, userID, name, age, sex, location, notes);
        linkTableMethod(LINK_ANIMAL_TYPE_SQL, animalID, typeID);


        ArrayList<String> elements = readSpecialNeedsOptions();

        for (int i = 0, n = elements.size(); i < n; i++) {
            linkNeedsTableMethod(LINK_ANIMAL_SPECIAL_NEEDS_SQL, animalID, elements.get(i), specialNeeds.get(elements.get(i)));
        }
    }


    /**
     * This method should create a new type
     * @param type
     */
    public void insertTypeMethod(Integer typeID, String type, Integer community, String active, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_TYPE_SQL);

            pstmt.setInt(1, typeID);
            pstmt.setString(2, type);
            pstmt.setInt(3, community);
            pstmt.setString(4, active);
            pstmt.setString(5, notes);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }


    public void insertType(Integer typeID, ArrayList<Integer> foodIDs, String type, Integer community, String active, String notes, Integer environID, Integer environClass) {

        insertTypeMethod(typeID, type, community, active, notes);

        for (int i = 0, n = foodIDs.size(); i < n; i++) {
            linkTableMethod(LINK_TYPE_FOOD_SQL, typeID, foodIDs.get(i));
        }


        if (environClass != 0)
            linkTripleTableMethod(LINK_TYPE_ENVIRONMENT_SQL, typeID, environID, environClass);





    }



    public void insertFood(Integer foodID, String name, double price, boolean InStock, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_FOOD_SQL);
            pstmt.setInt(1, foodID);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setBoolean(4, InStock);
            pstmt.setString(5, notes);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }


    public void insertLand(Integer landID, String title, String biome, double tempMax, double tempMin, double humidity, double minHeight, double minArea, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_LAND_SQL);
            pstmt.setInt(1, landID);
            pstmt.setString(2, title);
            pstmt.setString(3, biome);
            pstmt.setDouble(4, tempMax);
            pstmt.setDouble(5, tempMin);
            pstmt.setDouble(6, humidity);
            pstmt.setDouble(7, minHeight);
            pstmt.setDouble(8, minArea);
            pstmt.setString(9, notes);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }


    public void insertAqua(Integer aquaID, String title, double salinity, double pH, double tempMax, double tempMin, double volume, double oxygen, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(INSET_INTO_AQUA_SQL);
            pstmt.setInt(1, aquaID);
            pstmt.setString(2, title);
            pstmt.setDouble(3, salinity);
            pstmt.setDouble(4, pH);
            pstmt.setDouble(5, tempMax);
            pstmt.setDouble(6, tempMin);
            pstmt.setDouble(7, volume);
            pstmt.setDouble(8, oxygen);
            pstmt.setString(9, notes);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void editAnimalMethod(Integer animalID, String userID, String name, String age, String sex, String location, String notes) {




        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_ANIMAL_SQL);
            pstmt.setString(1, userID);
            pstmt.setString(2, name);
            pstmt.setString(3, age);
            pstmt.setString(4, sex);
            pstmt.setString(5, location);
            pstmt.setString(6, notes);
            pstmt.setInt(7, animalID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }


    public void editAnimal(Integer animalID, String userID, String name, String age, String sex, String location, String notes, Integer typeID, HashMap<String, String> specialNeeds) {
        editAnimalMethod(animalID, userID, name, age, sex, location, notes);
        updateLinkage(UPDATE_ANIMAL_TYPE_SQL, animalID, typeID);

        deleteLink(animalID, DELETE_SPECIAL_NEEDS_SQL);

        ArrayList<String> elements = readSpecialNeedsOptions();

        for (int i = 0, n = elements.size(); i < n; i++) {
            linkNeedsTableMethod(LINK_ANIMAL_SPECIAL_NEEDS_SQL, animalID, elements.get(i), specialNeeds.get(elements.get(i)));
        }

    }



    private void editTypeMethod(Integer typeID, String type, Integer community, String active, String notes) {




        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_TYPE_SQL);
            pstmt.setString(1, type);
            pstmt.setInt(2, community);
            pstmt.setString(3, active);
            pstmt.setString(4, notes);
            pstmt.setInt(5, typeID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void editType(Integer typeID, ArrayList<Integer> foodIDs, String type, Integer community, String active, String notes, Integer environID, Integer environClass) {

        editTypeMethod(typeID, type, community, active, notes);
        deleteLink(typeID, DELETE_TYPE_FOOD_LINK_SQL);
        deleteLink(typeID, DELETE_TYPE_ENVIRON_LINK_SQL);

        for (int i = 0, n = foodIDs.size(); i < n; i++) {
            linkTableMethod(LINK_TYPE_FOOD_SQL, typeID, foodIDs.get(i));
        }

        if (environClass != 0)
            linkTripleTableMethod(LINK_TYPE_ENVIRONMENT_SQL, typeID, environID, environClass);


    }


    public void editFood(Integer foodID, String name, double price, boolean inStock, String notes) {




        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_FOOD_SQL);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setBoolean(3, inStock);
            pstmt.setString(4, notes);
            pstmt.setInt(5, foodID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void editLand(Integer landID, String title, String biome, double tempMax, double tempMin, double humidity, double minHeight, double minArea, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_LAND_SQL);
            pstmt.setString(1, title);
            pstmt.setString(2, biome);
            pstmt.setDouble(3, tempMax);
            pstmt.setDouble(4, tempMin);
            pstmt.setDouble(5, humidity);
            pstmt.setDouble(6, minHeight);
            pstmt.setDouble(7, minArea);
            pstmt.setString(8, notes);
            pstmt.setInt(9, landID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }





    public void editAqua(Integer aquaID, String title, double salinity, double pH, double tempMax, double tempMin, double volume, double oxygen, String notes) {


        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_AQUA_SQL);
            pstmt.setString(1, title);
            pstmt.setDouble(2, salinity);
            pstmt.setDouble(3, pH);
            pstmt.setDouble(4, tempMax);
            pstmt.setDouble(5, tempMin);
            pstmt.setDouble(6, volume);
            pstmt.setDouble(7, oxygen);
            pstmt.setString(8, notes);
            pstmt.setInt(9, aquaID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void deleteAnimal(Integer animalID) {

        deleteAnimalMethod(animalID);
        deleteLink(animalID, DELETE_ANIMAL_TYPE_LINK_SQL);
        deleteLink(animalID, DELETE_ANIMAL_NEEDS_LINK_SQL);

    }

    private void deleteAnimalMethod(Integer ID) { //unlink all da stuf

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(DELETE_ANIMAL_SQL);
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    private void deleteLink(Integer ID, String sql) {

        try (Connection conn = this.connect();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage());
        }
    }



    public void deleteFood(Integer ID) {

        try (Connection conn = this.connect();

             PreparedStatement pstmt = conn.prepareStatement(DELETE_FOOD_SQL)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage());
        }
    }


    public void deleteType(Integer ID) {

        deleteTypeMethod(ID);
        deleteLink(ID, DELETE_TYPE_FOOD_LINK_SQL);
        deleteLink(ID, DELETE_TYPE_ENVIRON_LINK_SQL);

    }

    public void deleteTypeMethod(Integer ID) {

        try (Connection conn = this.connect();

             PreparedStatement pstmt = conn.prepareStatement(DELETE_TYPE_SQL)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void deleteLand(Integer ID) {

        try (Connection conn = this.connect();

             PreparedStatement pstmt = conn.prepareStatement(DELETE_LAND_SQL)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void deleteAqua(Integer ID) {

        try (Connection conn = this.connect();

             PreparedStatement pstmt = conn.prepareStatement(DELETE_AQUA_SQL)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    public void error(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("The action was unable to be performed.  Make sure the database file is present"+ message);
        alert.showAndWait();
    }

    /**
     * This method adds a row in a linked table with only 2 columns
     */
    private void linkTableMethod(String sql, Integer ID1, Integer ID2) {

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, String.valueOf(ID1));
            pstmt.setString(2, String.valueOf(ID2));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    /**
     * This method adds a row in a linked table with 3 columns
     */
    private void linkTripleTableMethod(String sql, Integer ID1, Integer ID2, Integer extraInfo) {

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, String.valueOf(ID1));
            pstmt.setString(2, String.valueOf(ID2));
            pstmt.setString(3, String.valueOf(extraInfo));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }

    /**
     * This method adds a row in a linked table with 3 columns
     */
    private void linkNeedsTableMethod(String sql, Integer ID, String option, String description) {

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, String.valueOf(ID));
            pstmt.setString(2, option);
            pstmt.setString(3, description);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            //error expected in certain cases, where it will be caught and code will continue
        }
    }

    public String getTextElement(String sql, String loc, Integer ID1) {

        String val = "";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, ID1);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                val = rs.getString(loc);
            }


        } catch (SQLException e) {
            error(e.getMessage());
        }

        return val;

    }

    public ArrayList<String> getTextElements(String sql, String loc, Integer ID1) {

        ArrayList<String> val = new ArrayList<String>();

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, ID1);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                val.add(rs.getString(loc));
            }


        } catch (SQLException e) {
            error(e.getMessage());
        }

        return val;

    }

    public Food getFoodElement(String sql, Integer ID1) {

        Food val = new Food();

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, ID1);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){//while rs.next() == true meaning that there is another entry after

                val = (new Food(
                        rs.getInt("FoodID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getBoolean("InStock"),
                        rs.getString("Notes")
                ));

            }// while


        } catch (SQLException e) {
            error(e.getMessage());
        }

        return val;

    }

    public Integer getLinkedElement(String sql, String loc, Integer ID1) {

        Integer ID2 = 0;

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, ID1);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                ID2 = rs.getInt(loc);
            }
        } catch (SQLException e) {
            error(e.getMessage());
        }

        return ID2;

    }

    public ArrayList<Integer> getLinkedElements(String sql, String loc, Integer ID1) {

        ArrayList<Integer> ID2 = new ArrayList<Integer>();

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, ID1);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                ID2.add(rs.getInt(loc));
            }

        } catch (SQLException e) {
            error(e.getMessage());
        }

        return ID2;

    }

    public void updateLinkage(String sql, Integer ID1, Integer ID2) {

        try {

            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ID2);
            pstmt.setInt(2, ID1);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            error(e.getMessage());
        }
    }
}