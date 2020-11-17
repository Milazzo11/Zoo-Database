import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Max Milazzo, Zoe Greenwald, William Guo, Ella Ramos-O'Neill, and Gavin Zhang
 */
public class Main extends Application {

    private static ZooJava myDB = new ZooJava();
    private static int searchMode = 0;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Zoo Database");

        Pane addPadding = new Pane();
        BorderPane mainRoot = new BorderPane();
        VBox root = new VBox(20);
        root.setPadding(new Insets(15,15,15,15));
        Button addElement = new Button("Add +");
        Button manageAnimal = new Button("Animals");
        Button manageType = new Button("Species");
        Button manageEnvironment = new Button("Environments");
        Button manageFood = new Button("Foods");

        Label mainText = new Label("Zoo Database");
        //declares main styling elements for the page

        HBox searchControl = new HBox(7);
        HBox buttonHolder = new HBox(5);
        TextField searchBar = new TextField();
        Button searchButton = new Button("Search");
        //styles elements used to create search bar

        VBox searchResults = new VBox(2);
        ScrollPane resultsArea = new ScrollPane();
        //declared variables used to style search result information box

        Label mode = new Label("                   Mode: ");
        mode.setStyle("-fx-font: 18 arial;");
        mainText.setStyle("-fx-font: 35 arial;");
        manageAnimal.setStyle("-fx-underline: true;");

        buttonHolder.getChildren().addAll(addElement, mode, manageAnimal, manageType, manageEnvironment, manageFood);
        addPadding.getChildren().addAll(buttonHolder);
        buttonHolder.setLayoutX(15);
        buttonHolder.setLayoutY(-15);
        //styles buttons

        resultsArea.setPrefSize(475, 115);
        resultsArea.setContent(searchResults);
        resultsArea.setVisible(false);
        //styles scroll pane that will house search results

        mainRoot.setLeft(root);
        root.getChildren().addAll(mainText, searchControl, resultsArea);
        searchControl.getChildren().addAll(searchBar, searchButton);
        //styles remaining elements

        mainRoot.setBottom(addPadding);
        //puts add element button into the scene

        Scene scene = new Scene(mainRoot, 585, 325);
        primaryStage.setScene(scene);
        primaryStage.show();
        //initializes stage

        initializeListeners(addElement, manageAnimal, manageType, manageEnvironment, manageFood, searchButton, searchBar, searchResults, resultsArea);
        //creates needed listeners

        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });


    }

    /**
     *
     * @param addElement
     * @param manageAnimal
     * @param manageType
     * @param manageEnvironment
     * @param manageFood
     * @param searchButton
     * @param searchBar
     * @param searchResults
     * @param resultsArea
     *
     *
     */
    public void initializeListeners(Button addElement, Button manageAnimal, Button manageType, Button manageEnvironment, Button manageFood, Button searchButton, TextField searchBar, VBox searchResults, ScrollPane resultsArea) {

        setAddElementActions(addElement);

        manageAnimal.setOnAction(e -> {
            setManageListeners(manageAnimal, manageType, manageEnvironment, manageFood, "true", "false", "false", "false", 0);
        });

        manageType.setOnAction(e -> {
            setManageListeners(manageAnimal, manageType, manageEnvironment, manageFood, "false", "true", "false", "false", 1);
        });

        manageEnvironment.setOnAction(e -> {
            setManageListeners(manageAnimal, manageType, manageEnvironment, manageFood, "false", "false", "true", "false", 2);
        });

        manageFood.setOnAction(e -> {
            setManageListeners(manageAnimal, manageType, manageEnvironment, manageFood, "false", "false", "false", "true", 3);
        });

        setSearchButtonActions (searchButton, searchBar, searchResults, resultsArea);

    }

    /**
     *
     * @param manageAnimal
     * @param manageType
     * @param manageEnvironment
     * @param manageFood
     * @param manageAnimalStr
     * @param manageTypeStr
     * @param manageEnvironmentStr
     * @param manageFoodStr
     * @param modeNum
     *
     *
     */
    public void setManageListeners (Button manageAnimal, Button manageType, Button manageEnvironment, Button manageFood, String manageAnimalStr, String manageTypeStr, String manageEnvironmentStr, String manageFoodStr, int modeNum){

        manageAnimal.setStyle("-fx-underline: "+ manageAnimalStr+";");
        manageType.setStyle("-fx-underline: "+ manageTypeStr+";");
        manageEnvironment.setStyle("-fx-underline: "+ manageEnvironmentStr+";");
        manageFood.setStyle("-fx-underline: "+ manageFoodStr+";");

        searchMode = modeNum;
    }

    /**
     *
     * @param addElement The add button
     *
     * This method pops up a different window depending on the mode selected (animal, Species, environment, food)
     */
    public void setAddElementActions (Button addElement) {
        addElement.setOnAction(e -> {
            switch (searchMode) {

                case 0:
                    addNewAnimalPrompt(false, null);
                    break;
                case 1:
                    addNewTypePrompt(false, null);
                    break;
                case 2:
                    addNewEnvironmentPrompt(false, null, null);
                    break;
                case 3:
                    addNewFoodPrompt(false, null);
            }
        });
    }

    /**
     *
     * @param searchButton the search button on the main page
     * @param searchBar the text field where the user inputs search characters
     * @param searchResults the VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     *
     * This method directs what happens when the search button is clicked, again depending on the mode
     */
    public void setSearchButtonActions (Button searchButton, TextField searchBar, VBox searchResults, ScrollPane resultsArea){
        searchButton.setOnAction(e -> {
            Label noEntry = new Label("No Text Entered.  You probably need to do that if you want to find anything...");
            noEntry.setTextFill(Color.web("#ff0000"));

            resultsArea.setVisible(true);
            searchResults.getChildren().clear();

            if (!(searchBar.getText().equals(""))) {
                switch (searchMode) {
                    case 0:
                        searchAnimalData(searchBar.getText(), searchResults, resultsArea, searchBar);
                        break;
                    case 1:
                        searchTypeData(searchBar.getText(), searchResults, resultsArea, searchBar);
                        break;
                    case 2:
                        searchEnvironmentData(searchBar.getText(), searchResults, resultsArea, searchBar);
                        break;
                    case 3:
                        searchFoodData(searchBar.getText(), searchResults, resultsArea, searchBar);
                }
            } else searchResults.getChildren().add(noEntry);
        });}

    /**
     *
     * @param query the user text that is used to search
     * @param searchResults the VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * The general method that searches animals
     */
    public void searchAnimalData(String query, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        int numOfResults = 0;
        int numOfResultsStorage;
        ArrayList<Animal> animals = myDB.readAnimals(query);
        ArrayList<Button> selectionButtons = new ArrayList<Button>();
        Label results = new Label("No Results Found  :(");

        results.setTextFill(Color.web("#ff0000"));
        String s = "s";

        for (int i = 0;i < animals.size();i++) {
            //if (animals.get(i).getUserID().toLowerCase().contains(query.toLowerCase()) || animals.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
            numOfResults++;
            selectionButtons.add(new Button("Select"));
            //}
        }

        if (numOfResults != 0) {
            if (numOfResults == 1)
                s = "";

            results.setText(numOfResults + " result" + s + " found:");
            results.setTextFill(Color.web("#008000"));

        }

        searchResults.getChildren().addAll(results, new Label(""));

        numOfResultsStorage = numOfResults;

        searchAnimalLoop(animals, searchResults, selectionButtons, numOfResults, numOfResultsStorage, resultsArea, searchBar);
    }

    /**
     *
     * @param animals ArrayList including all of the animals that fit the query
     * @param searchResults the VBox in which the search results are put
     * @param selectionButtons ArrayList of buttons with the text select that are put with each animal that fits the query
     * @param numOfResults The number of results
     * @param numOfResultsStorage The number of results left to be added to storage
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * This method goes through all of the animals and searches depending on the query
     */
    private void searchAnimalLoop(ArrayList<Animal> animals, VBox searchResults, ArrayList<Button> selectionButtons, int numOfResults, int numOfResultsStorage, ScrollPane resultsArea, TextField searchBar) {
        for (int i = 0;i < animals.size();i++) {

            Integer typeID = myDB.getLinkedElement("SELECT TypeID FROM AnimalType WHERE AnimalID=?", "TypeID", animals.get(i).getID());
            String type = myDB.getTextElement("SELECT Type FROM Type WHERE TypeID=?", "Type", typeID);

            searchResults.getChildren().addAll(new Separator(), new Label("Name: " + animals.get(i).getName() + "   |   " + "Species: " + type + "  " + " |   ID: " + animals.get(i).getUserID()), selectionButtons.get(numOfResults - numOfResultsStorage), new Label(""));

            final Animal locator = animals.get(i);

            selectionButtons.get(numOfResults - numOfResultsStorage).setOnAction(e -> {
                addNewAnimalPrompt(true, locator);
                searchResults.getChildren().clear();
                resultsArea.setVisible(false);
                searchBar.setText("");
            });
            numOfResultsStorage--;

        }
    }

    /**
     *
     * @param query the text that the user inputs to search for
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * This method searches through the type
     */
    public void searchTypeData(String query, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        int numOfResults = 0;
        int numOfResultsStorage;
        ArrayList<Type> types = myDB.readTypes(query);
        ArrayList<Button> selectionButtons = new ArrayList<Button>();
        Label results = new Label("No Results Found  :(");

        results.setTextFill(Color.web("#ff0000"));
        String s = "s";

        for (int i = 0;i < types.size();i++) {

            numOfResults++;
            selectionButtons.add(new Button("Select"));
        }

        if (numOfResults != 0) {
            if (numOfResults == 1)
                s = "";

            results.setText(numOfResults + " result" + s + " found:");
            results.setTextFill(Color.web("#008000"));
        }

        searchResults.getChildren().addAll(results, new Label(""));

        numOfResultsStorage = numOfResults;

        searchTypeLoop(types, selectionButtons, numOfResults, numOfResultsStorage, searchResults, resultsArea, searchBar);

    }

    /**
     *
     * @param types ArrayList including all of the types that fit the query
     * @param searchResults the VBox in which the search results are put
     * @param selectionButtons ArrayList of buttons with the text select that are put with each animal that fits the query
     * @param numOfResults The number of results
     * @param numOfResultsStorage The number of results left to be added to storage
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * This method goes through all of the types and searches depending on the query
     */
    private void searchTypeLoop( ArrayList<Type> types, ArrayList<Button> selectionButtons, int numOfResults, int numOfResultsStorage, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        for (int i = 0;i < types.size();i++) {

            //  if (types.get(i).getType().toLowerCase().contains(query.toLowerCase())) {

            searchResults.getChildren().addAll(new Separator(), new Label("Species: " + types.get(i).getType()),
                    selectionButtons.get(numOfResults - numOfResultsStorage), new Label(""));

            final Type locator = types.get(i);

            selectionButtons.get(numOfResults - numOfResultsStorage).setOnAction(e -> {
                addNewTypePrompt(true, locator);
                searchResults.getChildren().clear();
                resultsArea.setVisible(false);
                searchBar.setText("");
            });
            numOfResultsStorage--;
        }
    }

    /**
     *
     * @param query the text that the user inputs to search for
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * This method searches through the environment
     */
    public void searchEnvironmentData(String query, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        int numOfResults = 0;
        int numOfResultsStorage;
        ArrayList<Land> lands = myDB.readLands(query);
        ArrayList<Aqua> aquas = myDB.readAquas(query, true);
        ArrayList<Button> selectionButtons = new ArrayList<Button>();
        Label results = new Label("No Results Found  :(");

        results.setTextFill(Color.web("#ff0000"));

        numOfResults = addSelectBtns(numOfResults, selectionButtons, lands.size());
        numOfResults = addSelectBtns(numOfResults, selectionButtons, aquas.size());

        pluralResultsOrNo(results, numOfResults, "s");

        searchResults.getChildren().addAll(results, new Label(""));

        numOfResultsStorage = numOfResults;

        numOfResultsStorage = searchLandLoop(lands, selectionButtons, numOfResults, numOfResultsStorage, searchResults, resultsArea, searchBar);

        searchAquaLoop(aquas, selectionButtons, numOfResults, numOfResultsStorage, searchResults, resultsArea, searchBar);


    }

    /**
     *
     * @param numOfResults the number of search results
     * @param selectionButtons an arraylist of select buttons for the search results
     * @param size this is the size/length of the arraylist
     * @return the number of results
     *
     * This method adds the appropriate amount of buttons for the search results
     */
    private int addSelectBtns(int numOfResults, ArrayList<Button> selectionButtons, int size) {
        for (int i = 0; i < size; i++) {
            numOfResults++;
            selectionButtons.add(new Button("Select"));
        }
        return numOfResults;
    }

    /**
     *
     * @param results the results label
     * @param numOfResults the number of results
     * @param s a string containing the letter s
     *
     * This method determines if the search result label is plural or singular
     */
    private void pluralResultsOrNo(Label results, int numOfResults, String s) {
        if (numOfResults != 0) {

            if (numOfResults == 1) {
                s = "";
            }

            results.setText(numOfResults + " result" + s + " found:");
            results.setTextFill(Color.web("#008000"));

        }
    }

    /**
     *
     * @param lands an array list of all of the lands that poped up in the search
     * @param selectionButtons an arraylist of all of the select buttons
     * @param numOfResults the number of results to the search
     * @param numOfResultsStorage the number of results left to be added to the results displayed
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     * @return num of results that will be stored
     *
     * This method adds selection buttons for the land search results
     */
    private int searchLandLoop(ArrayList<Land> lands, ArrayList<Button> selectionButtons, int numOfResults, int numOfResultsStorage, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        for (int i = 0;i < lands.size();i++) {

            searchResults.getChildren().addAll(new Separator(), new Label("Name: " + lands.get(i).getTitle() + "   |   Biome: " + lands.get(i).getBiome()), selectionButtons.get(numOfResults - numOfResultsStorage), new Label(""));

            final Land landLocator = lands.get(i);

            selectionButtons.get(numOfResults - numOfResultsStorage).setOnAction(e -> {
                addNewEnvironmentPrompt(true, landLocator, null);
                searchResults.getChildren().clear();
                resultsArea.setVisible(false);
                searchBar.setText("");
            });
            numOfResultsStorage--;
        }
        return numOfResultsStorage;
    }


    /**
     *
     * @param aquas an array list of all of the lands that popped up in the search
     * @param selectionButtons an arraylist of all of the select buttons
     * @param numOfResults the number of results to the search
     * @param numOfResultsStorage the number of results left to be added to the results displayed
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     * @return num of results that will be stored
     *
     * This method adds selection buttons for the aquas search results
     */
    private void searchAquaLoop(ArrayList<Aqua> aquas, ArrayList<Button> selectionButtons, int numOfResults, int numOfResultsStorage, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        for (int i = 0;i < aquas.size();i++) {

            searchResults.getChildren().addAll(new Separator(), new Label("Name: " + aquas.get(i).getTitle() + "   |   Biome: Aquatic"), selectionButtons.get(numOfResults - numOfResultsStorage), new Label(""));

            final Aqua aquaLocator = aquas.get(i);

            selectionButtons.get(numOfResults - numOfResultsStorage).setOnAction(e -> {
                addNewEnvironmentPrompt(true, null, aquaLocator);
                searchResults.getChildren().clear();
                resultsArea.setVisible(false);
                searchBar.setText("");
            });
            numOfResultsStorage--;
        }
    }
    /**
     *
     * @param query the text that the user inputs to search for
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     *
     * This method searches through the food
     */
    public void searchFoodData(String query, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {

        int numOfResults = 0;
        int numOfResultsStorage;
        ArrayList<Food> foods = myDB.readFoods(query);
        ArrayList<Button> selectionButtons = new ArrayList<Button>();
        Label results = new Label("No Results Found  :(");

        results.setTextFill(Color.web("#ff0000"));
        String s = "s";

        for (int i = 0;i < foods.size();i++) {

            numOfResults++;
            selectionButtons.add(new Button("Select"));
        }

        if (numOfResults != 0) {

            if (numOfResults == 1)
                s = "";

            results.setText(numOfResults + " result" + s + " found:");
            results.setTextFill(Color.web("#008000"));
        }

        searchResults.getChildren().addAll(results, new Label(""));

        numOfResultsStorage = numOfResults;

        searchFoodLoop(foods, selectionButtons, numOfResults, numOfResultsStorage, searchResults, resultsArea, searchBar);

    }
    /**
     *
     * @param foods an array list of all of the foods that popped up in the search
     * @param selectionButtons an arraylist of all of the select buttons
     * @param numOfResults the number of results to the search
     * @param numOfResultsStorage the number of results left to be added to the results displayed
     * @param searchResults The VBox in which the search results are put
     * @param resultsArea the scroll pane that the search results are displayed in
     * @param searchBar the text field where the user inputs search character
     * @return num of results that will be stored
     *
     * This method adds selection buttons for the food search results
     */
    private void searchFoodLoop(ArrayList<Food> foods, ArrayList<Button> selectionButtons, int numOfResults, int numOfResultsStorage, VBox searchResults, ScrollPane resultsArea, TextField searchBar) {
        for (int i = 0; i < foods.size(); i++) {

            searchResults.getChildren().addAll(new Separator(), new Label("Food: " + foods.get(i).getName()), selectionButtons.get(numOfResults - numOfResultsStorage), new Label(""));

            final Food locator = foods.get(i);

            selectionButtons.get(numOfResults - numOfResultsStorage).setOnAction(e -> {
                addNewFoodPrompt(true, locator);
                searchResults.getChildren().clear();
                resultsArea.setVisible(false);
                searchBar.setText("");
            });
            numOfResultsStorage--;
        }
    }

    /**
     *
     * @param mode this is a boolean that shows if the prompt window is for editing or a new animal
     * @param locator The animal that acts as a place holder for the animal that is being added or edited
     *
     * This methods is the general methods that sets up the add or edit animal window
     */

    public void addNewAnimalPrompt(boolean mode, Animal locator) {

        int numOfElements = 8;
        AtomicInteger wrapper = new AtomicInteger(0);

        Stage addNewElementPromptStage = new Stage();
        HashMap<String, String> mySpecialNeeds = new HashMap<>();

        ArrayList<String> sexes = myDB.readSexOptions();
        ArrayList<Type> speciesInitialize = myDB.readTypes("");

        VBox mainRoot = new VBox(10);
        GridPane addRoot = new GridPane();
        HBox[] inputFormat = new HBox[numOfElements];
        Label[] elementTitles = new Label[numOfElements];
        Label addNewElementPromptMainTitle = new Label(" Add New Animal");

        TextField name = new TextField();
        TextField ID = new TextField();
        TextField speciesSearch = new TextField();
        TextField year = new TextField();
        TextField location = new TextField();
        TextField specialNeeds = new TextField();
        TextArea notesArea = new TextArea();

        ComboBox day = new ComboBox();
        ComboBox month = new ComboBox();
        ComboBox sexOptions = new ComboBox();
        ComboBox speciesOptions = new ComboBox();

        Button specialNeedsExtra = new Button((char) 183 + " " + (char) 183 + " " + (char) 183);

        elementTitles[0] = new Label(" Name:");
        elementTitles[1] = new Label(" ID:");
        elementTitles[2] = new Label(" Species:");
        elementTitles[3] = new Label(" Date of Birth:");
        elementTitles[4] = new Label(" Sex:");
        elementTitles[5] = new Label(" Location:");
        elementTitles[6] = new Label(" Special Needs:");
        elementTitles[7] = new Label(" Notes:");


        setGeneralAnimalFormat(name, ID, location, specialNeeds, 480, addNewElementPromptMainTitle, mainRoot, addRoot);

        for (int i = 0; i < numOfElements; i++) {
            inputFormat[i] = new HBox(5);
        }

        inputFormat[0].getChildren().add(name);
        inputFormat[1].getChildren().add(ID);

        setSpeciesAnimalFormat(inputFormat, speciesSearch, speciesOptions, speciesInitialize, wrapper);
        setDBAnimalFormat(inputFormat, day, month, year);
        setSexAnimalFormat(inputFormat, sexOptions, sexes);

        inputFormat[5].getChildren().add(location);

        setSpecialNeedsAnimalFormat(inputFormat, specialNeedsExtra, specialNeeds, mySpecialNeeds);

        notesArea.setWrapText(true);
        inputFormat[7].getChildren().add(notesArea);

        animalTxtBoxFilters(name, ID, speciesSearch, year, location, notesArea);

        addRoot.setValignment(elementTitles[7], VPos.TOP);


        for (int i = 0; i < numOfElements; i++) {

            addRoot.add(inputFormat[i], 1, i);
            addRoot.add(elementTitles[i], 0, i);

        }

        initializerButton(mode, addNewElementPromptMainTitle, addRoot, numOfElements, mainRoot, wrapper, name, ID, year, location, mySpecialNeeds, notesArea, locator, addNewElementPromptStage, speciesOptions, sexOptions, month, day, specialNeeds);
    }

    /**
     *
     * @param name name textField
     * @param ID ID textField
     * @param speciesSearch speciesSearch textField that holds the text to search for the species of the animal
     * @param year year textField
     * @param location location textField
     * @param notesArea notes text area
     *
     * This method adds listeners to the animal text boxes and areas for length and content
     */
    private void animalTxtBoxFilters(TextField name, TextField ID, TextField speciesSearch, TextField year, TextField location, TextArea notesArea) {
        name.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(name, oldVal, newVal, 50);
        });
        ID.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(ID, oldVal, newVal, 20);
        });
        speciesSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(speciesSearch, oldVal, newVal, 50);
        });
        year.textProperty().addListener((obs, oldVal, newVal) -> {
            intFilter(year, oldVal, newVal, 4);
        });
        location.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(location, oldVal, newVal, 50);
        });
        notesArea.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextAreaToLength(notesArea, oldVal, newVal, 250);
        });

    }

    /**
     *
     * @param mode
     * @param addNewElementPromptMainTitle
     * @param addRoot
     * @param numOfElements
     * @param mainRoot
     * @param wrapper
     * @param name
     * @param ID
     * @param year
     * @param location
     * @param mySpecialNeeds
     * @param notesArea
     * @param locator
     * @param addNewElementPromptStage
     * @param speciesOptions
     * @param sexOptions
     * @param month
     * @param day
     *
     * This method deals with the add animal button for animal
     */
    private void initializerButton(boolean mode, Label addNewElementPromptMainTitle, GridPane addRoot, int numOfElements, VBox mainRoot, AtomicInteger wrapper, TextField name, TextField ID, TextField year, TextField location, HashMap<String, String> mySpecialNeeds, TextArea notesArea, Animal locator, Stage addNewElementPromptStage, ComboBox speciesOptions, ComboBox sexOptions, ComboBox month, ComboBox day, TextField specialNeeds) {
        Button initializerAddButton = new Button("Add Animal");

        addRoot.add(new Label(""), 1, numOfElements);
        addRoot.add(initializerAddButton, 1, numOfElements + 1);

        Scene scene = new Scene(mainRoot, 750, 780);
        scene.getStylesheets().add("./style.css");
        addNewElementPromptStage.setScene(scene);
        addNewElementPromptStage.show();

        initializerAddButton.setOnAction(e -> {
            insertAnimal(locator, addNewElementPromptStage, mode, /**/ name, ID, year, location, mySpecialNeeds,/**/ notesArea,  wrapper.get(), (String) sexOptions.getValue(), (String) month.getValue(), (String) day.getValue());
        });

        if (mode) {

            Button delete = new Button("Delete");
            addRoot.add(delete, 1, numOfElements + 2);
            addNewElementPromptMainTitle.setText(" Animal Editor");
            initializerAddButton.setText("Save Changes");


            initializeEditor(name, ID, year, location, mySpecialNeeds, notesArea, locator, delete, addNewElementPromptStage, speciesOptions, sexOptions, month, day, specialNeeds); //add initialization method
        }

    }


    private void setSpecialNeedsAnimalFormat(HBox[] inputFormat, Button specialNeedsExtra, TextField specialNeeds, HashMap<String, String> mySpecialNeeds) {
        specialNeedsExtra.setId("extra");
        specialNeeds.setId("display");
        specialNeeds.setEditable(false);


        specialNeedsExtra.setOnAction(e -> {
            specialNeedsPopUp(specialNeeds, mySpecialNeeds);
        });


        inputFormat[6].getChildren().addAll(specialNeeds, specialNeedsExtra);
    }

    private void setSexAnimalFormat(HBox[] inputFormat, ComboBox sexOptions, ArrayList<String> sexes) {
        for (int i = 0, n = sexes.size(); i < n; i++) {
            sexOptions.getItems().add(sexes.get(i));
        }

        inputFormat[4].getChildren().add(sexOptions);
    }

    private void setSpeciesAnimalFormat(HBox[] inputFormat, TextField speciesSearch, ComboBox speciesOptions, ArrayList<Type> speciesInitialize, AtomicInteger wrapper) {

        speciesOptions.getItems().clear();
        for (int k = 0, n = speciesInitialize.size(); k < n; k++) {
            speciesOptions.getItems().add(speciesInitialize.get(k).getType());
        }

        setSpeciesSearchListener(speciesOptions, speciesSearch, wrapper);

        setSpeciesOptionsListener(speciesOptions, speciesSearch, wrapper);


        inputFormat[2].getChildren().addAll(speciesSearch, speciesOptions);
    }

    private void setSpeciesSearchListener(ComboBox speciesOptions, TextField speciesSearch, AtomicInteger wrapper) {
        speciesSearch.textProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal.equals(speciesOptions.getValue()) || newVal.equals("")) {
                speciesOptions.getItems().clear();

                ArrayList<Type> species = myDB.readTypes(newVal);
                for (int j = 0, n = species.size(); j < n; j++) {
                    speciesOptions.getItems().add(species.get(j).getType());
                }

                speciesSearch.setStyle("-fx-control-inner-background: white;");

            }
        });
    }

    private void setSpeciesOptionsListener(ComboBox speciesOptions, TextField speciesSearch, AtomicInteger wrapper) {
        speciesOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newVal) -> {
            if (newVal != null) {
                speciesSearch.setStyle("-fx-control-inner-background: #CFF0CC;");
                speciesSearch.setText(String.valueOf(newVal));
                speciesOptions.getItems().removeIf(n -> (!n.equals(speciesSearch.getText())));

                ArrayList<Type> allSpecies = myDB.readTypes("");
                for (int m = 0, n = allSpecies.size(); m < n; m++) {
                    if (allSpecies.get(m).getType().equals(speciesSearch.getText())) {
                        wrapper.set(allSpecies.get(m).getID());
                    }
                }
            }
        });
    }



    private void setDBAnimalFormat(HBox[] inputFormat, ComboBox day, ComboBox month, TextField year) {

        month.setValue("Month");
        day.setValue("Day");
        year.setPromptText("Year");


        //Label DoBLabel = new Label(" Date of Birth:");

        //DoBFormat.getChildren().addAll(month, day, year);
        month.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");


        for (int i = 1; i <= 31; i++) {
            day.getItems().add(String.valueOf(i));
        }

        inputFormat[3].getChildren().addAll(month, day, year);
    }

    private void setGeneralAnimalFormat(TextField name, TextField ID, TextField location, TextField specialNeeds, int width, Label addNewElementPromptMainTitle, VBox mainRoot, GridPane addRoot) {

        name.setPrefWidth(width);
        ID.setPrefWidth(width);
        location.setPrefWidth(width);
        specialNeeds.setPrefWidth(width);


        addNewElementPromptMainTitle.setStyle("-fx-font: 35 arial;");
        mainRoot.getChildren().addAll(addNewElementPromptMainTitle, new Separator(), addRoot);


        addRoot.setHgap(20);
        addRoot.setVgap(20);
    }


    public void initializeEditor(TextField name, TextField ID, TextField year, TextField location, HashMap<String, String> mySpecialNeeds, TextArea notesArea, Animal locator, Button delete, Stage addNewElementPromptStage, ComboBox speciesOptions, ComboBox sexOptions, ComboBox month, ComboBox day, TextField specialNeeds) {

        name.setText(locator.getName());
        ID.setText(locator.getUserID());
        location.setText(String.valueOf(locator.getLocation()));
        notesArea.setText(locator.getNotes());
        String needsText = "";


        ArrayList<String> needsOptions = myDB.getTextElements("SELECT needOption FROM SpecialNeeds WHERE animalID=?", "needOption", locator.getID());
        ArrayList<String> needsDescriptions = myDB.getTextElements("SELECT description FROM SpecialNeeds WHERE animalID=?", "description", locator.getID());

        for (int i = 0, n = needsOptions.size(); i < n; i++) {
            mySpecialNeeds.put(needsOptions.get(i), needsDescriptions.get(i));

            if (!(needsDescriptions.get(i).equals("")))
                needsText = needsText + needsOptions.get(i) + ", ";

        }

        needsText = needsText.substring(0, needsText.length() - 2);
        specialNeeds.setText(needsText);


        String DoB = locator.getAge();
        String monthNum = DoB.substring(0, 2);
        String dayNum = DoB.substring(2, 4);
        String yearNum = DoB.substring(4);

        monthNumToStr(monthNum, month);

        if (dayNum.charAt(0) == '0')
            day.setValue(Character.toString(dayNum.charAt(1)));
        else
            day.setValue(dayNum);

        year.setText(yearNum);

        animalEditorDel(locator, speciesOptions, sexOptions, delete, addNewElementPromptStage);

    }

    private void animalEditorDel(Animal locator, ComboBox speciesOptions, ComboBox sexOptions, Button delete, Stage addNewElementPromptStage) {
        int typeID = myDB.getLinkedElement("SELECT TypeID FROM AnimalType WHERE AnimalID=?", "TypeID", locator.getID());
        String type = myDB.getTextElement("SELECT Type FROM Type WHERE TypeID=?", "Type", typeID);
        speciesOptions.setValue(type);

        sexOptions.setValue(locator.getSex());

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                myDB.deleteAnimal(locator.getID());
                addNewElementPromptStage.close();
                createAlert("Element successfully deleted", "Element successfully deleted", "The selected element has been removed from the database"); //maybe add an "are you sure message" but idk
            }
        });
    }

    private void monthNumToStr(String monthNum, ComboBox month) {
        switch (monthNum) {

            case "01": month.setValue("January");
                break;

            case "02": month.setValue("February");;
                break;

            case "03": month.setValue("March");;
                break;

            case "04": month.setValue("April");;
                break;

            case "05": month.setValue("May");;
                break;

            case "06": month.setValue("June");;
                break;

            case "07": month.setValue("July");;
                break;

            case "08": month.setValue("August");;
                break;

            case "09": month.setValue("September");;
                break;

            case "10": month.setValue("October");;
                break;

            case "11": month.setValue("November");;
                break;

            case "12": month.setValue("December");;
                break;

        }
    }

    /**
     * adds and animal to the animal table
     */
    public void insertAnimal(Animal locator, Stage addNewElementPromptStage, boolean mode, TextField name, TextField ID, TextField year, TextField location, HashMap<String, String> specialNeeds, TextArea notesArea, int typeIDVal, String sexVal, String monthVal, String dayVal) { //method may be useless

        int animalIDVal = getRandomInt(0); //prolly change later


        String userIDVal = ID.getText();
        String nameVal = name.getText();
        String locationVal = location.getText();
        String notesVal = notesArea.getText();

        String ageVal = monthStrToNum(monthVal);
        ageVal = setAgeVal(dayVal, year, ageVal);

        responseToAnimalEditAttempt(mode, animalIDVal, locator, userIDVal, nameVal, ageVal, sexVal, locationVal, notesVal, typeIDVal, addNewElementPromptStage, specialNeeds);
    }

    private String setAgeVal(String dayVal, TextField year, String ageVal) {
        if (!dayVal.equals("Day")) {
            if (Integer.parseInt(dayVal) < 10) {
                ageVal = ageVal + "0" + dayVal;
            } else {
                ageVal = ageVal + dayVal;
            }
        }

        return (ageVal + year.getText());
    }

    private void responseToAnimalEditAttempt(boolean mode, int animalIDVal, Animal locator, String userIDVal, String nameVal, String ageVal, String sexVal, String locationVal, String notesVal, int typeIDVal, Stage addNewElementPromptStage, HashMap<String, String> specialNeeds) {
        if (mode) {

            myDB.editAnimal(locator.getID(), userIDVal, nameVal, ageVal, sexVal, locationVal, notesVal, typeIDVal, specialNeeds);
            addNewElementPromptStage.close();
            createAlert("Attribute successfully updated", "Attribute successfully updated", "The updated version of this element is now in the database");


        } else {
            myDB.insertAnimal(animalIDVal, userIDVal, typeIDVal, nameVal, ageVal, sexVal, locationVal, notesVal, specialNeeds);
            addNewElementPromptStage.close();
            createAlert("Element successfully added", "Element successfully added", "The new element has been added to the database and can be accessed");
        }
    }


    private String monthStrToNum(String monthVal) {
        String ageVal= "";
        switch (monthVal) {

            case "January": ageVal = "01";
                break;

            case "February": ageVal = "02";
                break;

            case "March": ageVal = "03";
                break;

            case "April": ageVal = "04";
                break;

            case "May": ageVal = "05";
                break;

            case "June": ageVal = "06";
                break;

            case "July": ageVal = "07";
                break;

            case "August": ageVal = "08";
                break;

            case "September": ageVal = "09";
                break;

            case "October": ageVal = "10";
                break;

            case "November": ageVal = "11";
                break;

            case "December": ageVal = "12";
                break;

        }

        return ageVal;
    }


    public void specialNeedsPopUp(TextField specialNeeds, HashMap<String, String> mySpecialNeeds) {

        ArrayList<String> elements = myDB.readSpecialNeedsOptions();

        int numOfElements = elements.size();

        Stage specialNeedsAdd = new Stage();

        VBox mainRoot = new VBox(10);

        ScrollPane needsScroll = new ScrollPane();
        GridPane elems = new GridPane();

        elems.setHgap(20);
        elems.setVgap(20);

        Label[] specialNeedsLabels = new Label[numOfElements];
        TextArea[] specialNeedsFields = new TextArea[numOfElements];

        Button insertNeeds = new Button("Save");

        Label specialNeedsAddTitle = new Label(" Special Needs Options");
        needsScroll.setContent(elems);
        mainRoot.getChildren().addAll(specialNeedsAddTitle, needsScroll, insertNeeds);

        addSpecialNeedsLoop(specialNeedsFields, specialNeedsLabels, elements, elems, numOfElements);


        for (int k = 0; k < numOfElements; k++) {
            specialNeedsFields[k].setText(mySpecialNeeds.get(elements.get(k)));
        }

        saveSpecialNeeds(numOfElements, specialNeedsFields, mySpecialNeeds, elements, elems, mainRoot, specialNeedsAdd, insertNeeds, specialNeeds);

    }

    private void addSpecialNeedsLoop(TextArea[] specialNeedsFields, Label[] specialNeedsLabels, ArrayList<String> elements, GridPane elems, int numOfElements) {
        for (int i = 0; i < numOfElements; i++) {
            specialNeedsLabels[i] = new Label(elements.get(i) + ":");
            specialNeedsFields[i] = new TextArea();
            specialNeedsFields[i].setPrefSize(300, 30);
            specialNeedsFields[i].setWrapText(true);

            elems.add(specialNeedsLabels[i], 0, i + 1);
            elems.add(specialNeedsFields[i], 1, i + 1);
        }

    }


    private void saveSpecialNeeds(int numOfElements, TextArea[] specialNeedsFields, HashMap<String, String> mySpecialNeeds, ArrayList<String> elements, GridPane elems, VBox mainRoot, Stage specialNeedsAdd, Button insertNeeds, TextField specialNeeds) {
        Label blank = new Label("");
        blank.setStyle("-fx-font: 0 arial;");
        elems.add(blank, 0, numOfElements + 1);
        mainRoot.setPadding(new Insets(2,2,2,2));


        Scene scene = new Scene(mainRoot, 440, 300);
        scene.getStylesheets().add("./style.css");
        specialNeedsAdd.setScene(scene);
        specialNeedsAdd.show();

        insertNeeds.setOnAction(e -> {

            String specialNeedsText = "";
            String finText; //we might not actually need (reset orig txt)

            for (int j = 0; j < numOfElements; j++) {

                mySpecialNeeds.put(elements.get(j), specialNeedsFields[j].getText());

                try {

                    if (!(specialNeedsFields[j].getText().equals("")))
                        specialNeedsText = specialNeedsText + elements.get(j) + ", ";

                } catch (NullPointerException f) {
                    //code caught if null pointer exception present and the loop continues
                }
            }
            finText = setFinTextSpecialNeeds(specialNeedsText);

            specialNeeds.setText(finText);
            specialNeedsAdd.close();

        });
    }


    public void addNewTypePrompt(boolean mode, Type locator) {

        int numOfElements = 6;

        Stage addNewElementPromptStage = new Stage();
        HashMap<Integer, Food> myFoods = new HashMap<>();
        ArrayList<Aqua> aquaInitialize = new ArrayList<Aqua>();
        ArrayList<Land> landInitialize = new ArrayList<Land>();
        AtomicInteger wrapper = new AtomicInteger(0);
        AtomicInteger environ = new AtomicInteger(0);


        VBox mainRoot = new VBox(10);
        GridPane addRoot = new GridPane();
        HBox[] inputFormat = new HBox[numOfElements];
        Label[] elementTitles = new Label[numOfElements];
        Label addNewElementPromptMainTitle = new Label(" Add New Species");
        ComboBox activeOptions = new ComboBox();
        ComboBox environOptions = new ComboBox();

        TextField species = new TextField();
        TextField food = new TextField();
        TextField environSearch = new TextField();
        TextField community = new TextField();
        TextField activity = new TextField();
        TextArea notesArea = new TextArea();

        setGeneralTypeFormat(species, food, community, activity, 480, addNewElementPromptMainTitle, mainRoot, addRoot, inputFormat, numOfElements);

        inputFormat[0].getChildren().add(species);

        setFoodTypeFormat(food, inputFormat, myFoods);

        inputFormat[2].getChildren().add(community);

        setActiveTypeFormat(inputFormat, activeOptions);


        aquaInitialize = myDB.readAquas("", false);
        landInitialize = myDB.readLands("");
        setEnvironTypeFormat(inputFormat, environSearch, environOptions, wrapper, aquaInitialize, landInitialize, environ);

        notesArea.setWrapText(true);
        inputFormat[5].getChildren().add(notesArea);

        typeTxtBoxFilters(species, food, community, notesArea);

        addFormatTypePromptLabels(inputFormat, elementTitles, numOfElements, addRoot, addNewElementPromptStage, locator, addNewElementPromptStage, mode, species, myFoods, community, notesArea, (String) activeOptions.getValue());

        setTypeEditorBtns(food, activeOptions, numOfElements, mainRoot, addNewElementPromptMainTitle, addRoot, addNewElementPromptStage, locator, mode, species, myFoods, community, notesArea, (String) activeOptions.getValue(), mode, wrapper, environ, environSearch);

    }


    private void setEnvironTypeFormat(HBox[] inputFormat, TextField environSearch, ComboBox environOptions, AtomicInteger wrapper, ArrayList<Aqua> aquaInitialize, ArrayList<Land> landInitialize, AtomicInteger environ) {

        environOptions.getItems().clear();
        for (int k = 0, n = landInitialize.size(); k < n; k++) {
            environOptions.getItems().add(landInitialize.get(k).getTitle());
        }

        for (int l = 0, n = aquaInitialize.size(); l < n; l++) {
            environOptions.getItems().add(aquaInitialize.get(l).getTitle());
        }

        setEnvironSearchListener(environOptions, environSearch);

        setEnvironOptionsListener(environOptions, environSearch, wrapper, environ);

        inputFormat[4].getChildren().addAll(environSearch, environOptions);

    }


    private void setEnvironOptionsListener(ComboBox environOptions, TextField environSearch, AtomicInteger wrapper, AtomicInteger environ) {
        environOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newVal) -> {
            if (newVal != null) {
                environSearch.setStyle("-fx-control-inner-background: #CFF0CC;");
                environSearch.setText(String.valueOf(newVal));
                environOptions.getItems().removeIf(n -> (!n.equals(environSearch.getText())));

                ArrayList<Aqua> allAquas = myDB.readAquas("", false);
                ArrayList<Land> allLands = myDB.readLands("");

                for (int m = 0, n = allLands.size(); m < n; m++) {
                    if (allLands.get(m).getTitle().equals(environSearch.getText())) {
                        wrapper.set(allLands.get(m).getID());
                        environ.set(1);
                    }
                }

                for (int p = 0, n = allAquas.size(); p < n; p++) {
                    if (allAquas.get(p).getTitle().equals(environSearch.getText())) {
                        wrapper.set(allAquas.get(p).getID());
                        environ.set(2);
                    }
                }
            }
        });
    }


    private void setEnvironSearchListener(ComboBox environOptions, TextField environSearch) {
        environSearch.textProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal.equals(environOptions.getValue()) || newVal.equals("")) {
                environOptions.getItems().clear();

                ArrayList<Aqua> aquas = myDB.readAquas(newVal, false);
                ArrayList<Land> lands = myDB.readLands(newVal);

                for (int j = 0, n = lands.size(); j < n; j++) {
                    environOptions.getItems().add(lands.get(j).getTitle());
                }

                for (int j = 0, n = aquas.size(); j < n; j++) {
                    environOptions.getItems().add(aquas.get(j).getTitle());
                }


                environSearch.setStyle("-fx-control-inner-background: white;");

            }
        });
    }





    /**
     *
     * @param species
     * @param food
     * @param community
     * @param notesArea
     *
     * This method adds listeners to the type text boxes and areas for length and content
     */
    private void typeTxtBoxFilters(TextField species, TextField food, TextField community, TextArea notesArea) {
        species.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(species, oldVal, newVal, 50);
        });
        food.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(food, oldVal, newVal, 50);
        });
        community.textProperty().addListener((obs, oldVal, newVal) -> {
            intFilter(community, oldVal, newVal, 5);
        });
        notesArea.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextAreaToLength(notesArea, oldVal, newVal, 250);
        });
    }

    private void setTypeEditorBtns(TextField food, ComboBox activeOptions, int numOfElements, VBox mainRoot, Label addNewElementPromptMainTitle, GridPane addRoot, Stage addNewElementPromptStage, Type locator, boolean mode, TextField species, HashMap<Integer,Food> myFoods, TextField community, TextArea notesArea, String value, boolean mode1, AtomicInteger wrapper, AtomicInteger environ, TextField environSearch) {
        Button initializerAddButton = new Button("Add Species");

        addRoot.add(new Label(""), 1, numOfElements);
        addRoot.add(initializerAddButton, 1, numOfElements + 1);

        Scene scene = new Scene(mainRoot, 750, 780);
        scene.getStylesheets().add("./style.css");
        addNewElementPromptStage.setScene(scene);
        addNewElementPromptStage.show();

        initializerAddButton.setOnAction(e -> {
            insertType(locator, addNewElementPromptStage, mode, species, myFoods, community, notesArea, (String) activeOptions.getValue(), wrapper, environ);
        });

        if (mode) {

            Button delete = new Button("Delete");
            addRoot.add(delete, 1, numOfElements + 2);
            addNewElementPromptMainTitle.setText(" Species Editor");
            initializerAddButton.setText("Save Changes");


            typeEditor(species, food, community, notesArea, locator, delete, addNewElementPromptStage, activeOptions, myFoods, wrapper, environ, environSearch);

        }
    }

    private void setActiveTypeFormat(HBox[] inputFormat, ComboBox activeOptions) {
        ArrayList<String> activities = myDB.readActiveOptions();
        for (int i = 0, n = activities.size(); i < n; i++) {
            activeOptions.getItems().add(activities.get(i));
        }


        inputFormat[3].getChildren().add(activeOptions);
    }

    private void addFormatTypePromptLabels(HBox[] inputFormat, Label[] elementTitles, int numOfElements, GridPane addRoot, Stage addNewElementPromptStage, Type locator, Stage addNewElementPromptStage1, boolean mode, TextField species, HashMap<Integer,Food> myFoods, TextField community, TextArea notesArea, String value) {
        elementTitles[0] = new Label(" Species:");
        elementTitles[1] = new Label(" Food:");
        elementTitles[2] = new Label(" Community Size:");
        elementTitles[3] = new Label(" Sleep Activity:");
        elementTitles[4] = new Label(" Environment:");
        elementTitles[5] = new Label(" Notes:");
        addRoot.setValignment(elementTitles[5], VPos.TOP);


        for (int i = 0; i < numOfElements; i++) {

            addRoot.add(inputFormat[i], 1, i);
            addRoot.add(elementTitles[i], 0, i);

        }
    }

    private void setGeneralTypeFormat(TextField species, TextField food, TextField community, TextField activity, int width, Label addNewElementPromptMainTitle, VBox mainRoot, GridPane addRoot, HBox[] inputFormat, int numOfElements) {
        species.setPrefWidth(width);
        food.setPrefWidth(width);
        community.setPrefWidth(width);
        activity.setPrefWidth(width);


        addNewElementPromptMainTitle.setStyle("-fx-font: 35 arial;");
        mainRoot.getChildren().addAll(addNewElementPromptMainTitle, new Separator(), addRoot);


        addRoot.setHgap(20);
        addRoot.setVgap(20);


        for (int i = 0; i < numOfElements; i++) {
            inputFormat[i] = new HBox(5);
        }
    }

    private void setFoodTypeFormat(TextField food, HBox[] inputFormat,  HashMap<Integer, Food>myFoods) {
        Button foodExtra = new Button((char) 183 + " " + (char) 183 + " " + (char) 183);
        foodExtra.setId("extra");
        food.setId("display");
        food.setEditable(false);

        foodExtra.setOnAction(e -> {
            addFoodInSpecies(food, myFoods);
        });


        inputFormat[1].getChildren().addAll(food, foodExtra);
    }

    public void typeEditor(TextField species, TextField food, TextField community, TextArea notesArea, Type locator, Button delete, Stage addNewElementPromptStage, ComboBox activeOptions, HashMap<Integer, Food> myFoods, AtomicInteger wrapper, AtomicInteger environ, TextField environSearch) {

        species.setText(locator.getType());
        community.setText(String.valueOf(locator.getCommunity()));
        notesArea.setText(locator.getNotes());
        activeOptions.setValue(locator.getActive());
        ArrayList<Integer> tempStore = new ArrayList<Integer>();
        String foodText = "";
        String finText = "";

        tempStore = myDB.getLinkedElements("SELECT FoodID FROM TypeFood WHERE TypeID=?", "FoodID", locator.getID());

        for (int i = 0, n = tempStore.size(); i < n; i++) {
            myFoods.put(i, myDB.getFoodElement("SELECT * FROM Food WHERE FoodID=?", tempStore.get(i)));
            foodText = foodText + myFoods.get(i).getName() + ", ";

        }

        if (tempStore.size() > 0){
            finText = foodText.substring(0, foodText.length() - 2);
        }

        food.setText(finText);

        wrapper.set(myDB.getLinkedElement("SELECT EnvironID FROM TypeEnvironment WHERE TypeID=?", "EnvironID", locator.getID()));
        environ.set(myDB.getLinkedElement("SELECT EnvironType FROM TypeEnvironment WHERE TypeID=?", "EnvironType", locator.getID()));


        if (environ.intValue() == 1) {
            environSearch.setText(myDB.getTextElement("SELECT Title FROM Land WHERE LandID=?", "Title", wrapper.intValue()));
            environSearch.setStyle("-fx-control-inner-background: #CFF0CC;");
        } else if (environ.intValue() == 2) {
            environSearch.setText(myDB.getTextElement("SELECT Title FROM Aqua WHERE AquaID=?", "Title", wrapper.intValue()));
            environSearch.setStyle("-fx-control-inner-background: #CFF0CC;");
        }



        delTypeAction(delete, locator, addNewElementPromptStage);
    }

    private void delTypeAction(Button delete, Type locator, Stage addNewElementPromptStage) {
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                myDB.deleteType(locator.getID());
                addNewElementPromptStage.close();
                createAlert("Element Successfully Deleted", "Element Successfully Deleted", "The selected element has been removed from the database."); //maybe add an "are you sure message" but idk... also bad alert

            }
        });
    }

    public void insertType(Type locator, Stage addNewElementPromptStage, boolean mode, TextField species, HashMap<Integer, Food> myFoods, TextField community, TextArea notesArea, String activeVal, AtomicInteger wrapper, AtomicInteger environ) {

        Integer typeIDVal = getRandomInt(2);
        String typeVal = species.getText();
        Integer communityVal = 0;
        String notesVal = notesArea.getText();
        ArrayList<Integer> foodIDs = new ArrayList<Integer>();


        Integer environID = wrapper.intValue();
        Integer environClass = environ.intValue();


        communityVal = setCommunityVal(communityVal, community);


        for (int i = 0, n = myFoods.size(); i < n; i++) {

            try {
                foodIDs.add(myFoods.get(i).getFoodID());
            } catch (NullPointerException j) {
                //code caught if null pointer exception present and the loop continues
            }
        }


        if (mode) {

            myDB.editType(locator.getID(), foodIDs, typeVal, communityVal, activeVal, notesVal, environID, environClass);
            addNewElementPromptStage.close();
            createAlert("Attributes Successfully Updated", "Attributes Successfully Updated", "The new element is now in the database and can be accessed");

        } else {
            myDB.insertType(typeIDVal, foodIDs, typeVal, communityVal, activeVal, notesVal, environID, environClass);
            addNewElementPromptStage.close();
            createAlert("Element successfully added", "Element successfully added", "The new element is now in the database and can be accessed"); //bad alert, but whatever

        }
    }

    private Integer setCommunityVal(Integer communityVal, TextField community) {

        if (community.getText().length() > 0) {
            communityVal = Integer.parseInt(community.getText());
        }
        return communityVal;
    }


    public void addFoodInSpecies(TextField food, HashMap<Integer, Food> myFoods) {

        HashMap<Integer, Food> oldStorage = new HashMap<>();

        Stage foodSpecies = new Stage();
        VBox mainRoot = new VBox();


        GridPane fieldRoot = new GridPane();
        HBox inputField = new HBox(5);


        Button add = new Button("+");
        HashMap<Integer, Label> foodNames = new HashMap<>();
        HashMap<Integer, Button> remove = new HashMap<>();
        Label empty = new Label("No foods currently associated with this species");

        ScrollPane foodDisplay = new ScrollPane();
        VBox displayContainers = new VBox(2);


        Label foodPopUpTitle = new Label(" Food Selector:");
        Label find = new Label(" Find Foods:");

        TextField search = new TextField();

        Button insertFood = new Button("Save");

        addFoodSpeciesGeneralFormat(oldStorage, myFoods, empty, foodDisplay, fieldRoot, displayContainers, foodPopUpTitle, mainRoot, find);

        ComboBox foodOptions = new ComboBox();

        ArrayList<Food> foodInitialize = myDB.readFoods("");

        setFoodOptionComboBox(foodOptions, foodInitialize, search, myFoods);


        add.setOnAction(e -> {
            setAddFoodBtnAction(empty, foodOptions, foodInitialize, myFoods, displayContainers, remove, foodNames, foodDisplay, search);
        });


        Scene scene = new Scene(mainRoot, 450, 265);
        formatInputFieldFoodNSceneInSpecies(foodDisplay, insertFood, inputField, search, foodOptions, add, fieldRoot, scene, foodSpecies);


        if (myFoods.size() == 0) {

            displayContainers.getChildren().add(empty);

        } else {

            setDisplayFoodInSpecies(myFoods, foodNames, remove, foodDisplay, foodOptions, displayContainers, empty);
        }


        insertFoodActionFoodInSpecies(myFoods, insertFood, food, foodSpecies);

        setFoodSpeciesCloseRequest(foodSpecies, myFoods, oldStorage);


    } //add error check for empty entry

    private void setFoodSpeciesCloseRequest(Stage foodSpecies, HashMap<Integer,Food> myFoods, HashMap<Integer,Food> oldStorage) {
        foodSpecies.setOnCloseRequest(e -> {

            myFoods.clear();

            for (int z = 0, n = oldStorage.size(); z < n; z++) {

                myFoods.put(z, oldStorage.get(z));
            }


        });
    }


    private void insertFoodActionFoodInSpecies(HashMap<Integer,Food> myFoods, Button insertFood, TextField food, Stage foodSpecies) {
        insertFood.setOnAction(e -> {

            String foodText = "";

            for (int i = 0, n = myFoods.size(); i < n; i++) {

                try {

                    foodText = foodText + myFoods.get(i).getName() + ", ";

                } catch (NullPointerException j) {
                    alert("Error: Inserting food");
                }
            }

            String finText = setFinTextFood(myFoods.size(), foodText);

            food.setText(finText);
            foodSpecies.close();

        });
    }


    private String setFinTextFood(int size, String foodText) {

        if (size > 0){
            foodText = foodText.substring(0, foodText.length() - 2);
        }
        return foodText;
    }

    private String setFinTextSpecialNeeds(String text) {

        if (text.length() > 0){
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    private void setDisplayFoodInSpecies(HashMap<Integer,Food> myFoods, HashMap<Integer, Label> foodNames, HashMap<Integer, Button> remove, ScrollPane foodDisplay, ComboBox foodOptions, VBox displayContainers, Label empty) {

        for (int i = 0;i < myFoods.size();i++) {

            foodNames.put(i, new Label(myFoods.get(i).getName()));
            remove.put(i, new Button("x"));
            remove.get(i).setStyle("-fx-color: #ff0000;-fx-font: 10 arial;");
            remove.get(i).setPrefWidth(20);

            foodOptions.getItems().remove(myFoods.get(i).getName());

            GridPane test = new GridPane();
            setGPaneforAddFoodBtn(foodDisplay, displayContainers, foodNames, i, remove, test);


            final int locVal = i;
            remove.get(i).setOnAction(e -> {
                displayContainers.getChildren().remove(test);
                myFoods.remove(locVal);
                foodOptions.getItems().add(foodNames.get(locVal).getText());


                if (myFoods.size() == 0) {
                    displayContainers.getChildren().add(empty);
                }

            });


        }
    }

    private void formatInputFieldFoodNSceneInSpecies(ScrollPane foodDisplay, Button insertFood, HBox inputField, TextField search, ComboBox foodOptions, Button add, GridPane fieldRoot, Scene scene, Stage foodSpecies) {
        inputField.getChildren().addAll(search, foodOptions, add);
        fieldRoot.add(inputField, 1, 0);
        fieldRoot.add(foodDisplay, 1, 1);
        fieldRoot.add(insertFood, 1, 2);


        scene.getStylesheets().add("./style.css");
        foodSpecies.setScene(scene);
        foodSpecies.show();
    }

    private void setAddFoodBtnAction(Label empty, ComboBox foodOptions, ArrayList<Food> foodInitialize, HashMap<Integer,Food> myFoods, VBox displayContainers, HashMap<Integer, Button> remove, HashMap<Integer, Label> foodNames, ScrollPane foodDisplay, TextField search) {
        int location = 0;
        GridPane test = new GridPane();

        if (foodOptions.getValue() != null) {

            if (myFoods.size() == 0) {
                displayContainers.getChildren().remove(empty);
            }


            for (int i = 0, n = foodInitialize.size(); i < n; i++) {

                if (myFoods.get(i) == null) {
                    location = i;
                    i = n;
                }
            }


            foodNames.put(location, new Label((String) foodOptions.getValue()));
            remove.put(location, new Button("x"));
            remove.get(location).setStyle("-fx-color: #ff0000;-fx-font: 10 arial;");
            remove.get(location).setPrefWidth(20);

            setGPaneforAddFoodBtn(foodDisplay, displayContainers, foodNames, location, remove, test);


            for (int k = 0, n = foodInitialize.size(); k < n; k++) {

                if (foodOptions.getValue().equals(foodInitialize.get(k).getName())) {
                    myFoods.put(location, foodInitialize.get(k));
                    k = n;
                }
            }

            setRemoveActionAddFoodBtn(test, empty, remove, search, location, displayContainers, myFoods, foodOptions, foodNames);
        }

    }

    private void setRemoveActionAddFoodBtn(GridPane test, Label empty,  HashMap<Integer, Button> remove, TextField search, int location, VBox displayContainers, HashMap<Integer,Food> myFoods, ComboBox foodOptions, HashMap<Integer, Label> foodNames) {

        foodOptions.getItems().remove(foodOptions.getValue());
        search.setText("");

        final int locVal = location;
        remove.get(locVal).setOnAction(f -> {
            displayContainers.getChildren().remove(test);


            myFoods.remove(locVal);
            foodOptions.getItems().add(foodNames.get(locVal).getText());

            if (myFoods.size() == 0)
                displayContainers.getChildren().add(empty);


        });

    }

    private void setGPaneforAddFoodBtn(ScrollPane foodDisplay, VBox displayContainers, HashMap<Integer, Label> foodNames, int location, HashMap<Integer, Button> remove, GridPane test) {
        test.add(foodNames.get(location), 0, 0);
        test.add(remove.get(location), 1, 0);
        test.setGridLinesVisible(true);
        test.setMinWidth(foodDisplay.getWidth() - 17);
        test.setMaxWidth(foodDisplay.getWidth() - 17);



        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMaxWidth(foodDisplay.getWidth() - 37);
        col1.setMinWidth(foodDisplay.getWidth() - 37);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMaxWidth(20);
        col2.setMinWidth(20);
        test.getColumnConstraints().addAll(col1, col2);

        displayContainers.getChildren().add(0, test);
    }

    private void setFoodOptionComboBox(ComboBox foodOptions, ArrayList<Food> foodInitialize , TextField search, HashMap<Integer,Food> myFoods) {
        AtomicInteger wrapper = new AtomicInteger(0);


        foodOptions.getItems().clear();
        for (int k = 0, n = foodInitialize.size(); k < n; k++) {
            foodOptions.getItems().add(foodInitialize.get(k).getName());
        }

        addFoodOptionComboBoxSearchListener(search, foodOptions, myFoods);


        foodOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newVal) -> {

            if (newVal != null) {
                search.setStyle("-fx-control-inner-background: #CFF0CC;");
                search.setText(String.valueOf(newVal));
                foodOptions.getItems().removeIf(n -> (!n.equals(search.getText())));

                ArrayList<Food> allFoods = myDB.readFoods("");
                for (int m = 0, n = allFoods.size(); m < n; m++) {
                    if (allFoods.get(m).getName().equals(search.getText())) {
                        wrapper.set(allFoods.get(m).getFoodID());
                    }
                }
            }
        });
    }

    private void addFoodOptionComboBoxSearchListener(TextField search, ComboBox foodOptions, HashMap<Integer,Food> myFoods) {
        search.textProperty().addListener((obs, oldVal, newVal) -> {

            if (!newVal.equals(foodOptions.getValue()) || newVal.equals("")) {
                foodOptions.getItems().clear();

                ArrayList<Food> foods = myDB.readFoods(newVal);


                for (int j = 0, n = foods.size(); j < n; j++) {
                    foodOptions.getItems().add(foods.get(j).getName());
                }

                for (int k = 0, n = myFoods.size(); k < n; k++) {

                    try {
                        foodOptions.getItems().remove(myFoods.get(k).getName());

                    } catch (NullPointerException e) {
                        alert("Error: with food combo box");
                    }
                }


                search.setStyle("-fx-control-inner-background: white;");

            }
        });
    }

    private void addFoodSpeciesGeneralFormat(HashMap<Integer,Food> oldStorage, HashMap<Integer,Food> myFoods, Label empty, ScrollPane foodDisplay, GridPane fieldRoot, VBox displayContainers, Label foodPopUpTitle, VBox mainRoot, Label find) {
        for (int y = 0, n = myFoods.size(); y < n; y++) {
            oldStorage.put(y, myFoods.get(y));
        }

        empty.setStyle("-fx-font: 12 arial;");
        empty.setTextFill(Color.web("#ff0000"));

        foodDisplay.setContent(displayContainers);
        foodDisplay.setPrefSize(250, 110);

        foodDisplay.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        foodPopUpTitle.setStyle("-fx-font: 20 arial;");
        mainRoot.getChildren().addAll(foodPopUpTitle, new Separator(), new Label(""), fieldRoot);

        fieldRoot.setHgap(20);
        fieldRoot.setVgap(20);
        fieldRoot.add(find, 0, 0);
    }


    public void addNewEnvironmentPrompt(boolean mode, Land landLocator, Aqua aquaLocator) {

        int numOfElements = 9;

        Stage addNewElementPromptStage = new Stage();

        VBox mainRoot = new VBox(10);
        GridPane addRoot = new GridPane();
        HBox[] inputFormat = new HBox[numOfElements];
        Label[] elementTitles = new Label[numOfElements];
        Label addNewElementPromptMainTitle = new Label(" Add New Environment");

        TextField title = new TextField();
        TextField minTemp = new TextField();
        TextField maxTemp = new TextField();

        TextField biomeOrSalinity = new TextField();
        TextField humidityOrPH = new TextField();
        TextField minAreaOrVolume = new TextField();
        TextField minHeightOrOxygen = new TextField();

        TextArea notesArea = new TextArea();

        ComboBox environmentTypeSelector = new ComboBox();

        setGeneralEnvironmentFormat(480, numOfElements, inputFormat, addNewElementPromptMainTitle, mainRoot, addRoot, title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, humidityOrPH, minAreaOrVolume, minHeightOrOxygen);

        setEnvironLandAquaOptions(environmentTypeSelector, elementTitles);

        environTxtBoxFilters(elementTitles, title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea);

        addChildrenToInputFormatEnvironment(inputFormat, environmentTypeSelector, title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea);

        addElementTitlesEnvironment(elementTitles, addRoot);

        for (int i = 0; i < numOfElements; i++) {

            addRoot.add(inputFormat[i], 1, i);
            addRoot.add(elementTitles[i], 0, i);

        }
        Button initializerAddButton = new Button("Add Environment");
        initializerAddButtonEnvironmentFormat(mainRoot, initializerAddButton, addRoot, numOfElements, addNewElementPromptStage, landLocator, aquaLocator, addNewElementPromptStage, mode, title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea, environmentTypeSelector);


        if (mode) {

            Button delete = new Button("Delete");
            addRoot.add(delete, 1, numOfElements + 2);
            addNewElementPromptMainTitle.setText(" Environment Editor");
            initializerAddButton.setText("Save Changes");

            environmentEditor(title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea, landLocator, aquaLocator, delete, addNewElementPromptStage, environmentTypeSelector);
        }
    }

    private void initializerAddButtonEnvironmentFormat(VBox mainRoot, Button initializerAddButton, GridPane addRoot, int numOfElements, Stage addNewElementPromptStage, Land landLocator, Aqua aquaLocator, Stage addNewElementPromptStage1, boolean mode, TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea, ComboBox environmentTypeSelector) {

        addRoot.add(new Label(""), 1, numOfElements);
        addRoot.add(initializerAddButton, 1, numOfElements + 1);

        Scene scene = new Scene(mainRoot, 865, 800);
        scene.getStylesheets().add("./style.css");
        addNewElementPromptStage.setScene(scene);
        addNewElementPromptStage.show();

        initializerAddButton.setOnAction(e -> {
            insertEnvironment(landLocator, aquaLocator, addNewElementPromptStage, mode, title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea, environmentTypeSelector);
        });
    }

    private void addElementTitlesEnvironment(Label[] elementTitles, GridPane addRoot) {
        elementTitles[0] = new Label(" Environment Type:");
        elementTitles[1] = new Label(" Environment Title/Name/Identifier:");
        elementTitles[2] = new Label(" Maximum Temperature (Celsius):");
        elementTitles[3] = new Label(" Minimum Temperature (Celsius):");
        elementTitles[4] = new Label(" Biome:");
        elementTitles[5] = new Label(" Humidity (%):");
        elementTitles[6] = new Label(" Minimum Area (Square Meters):");
        elementTitles[7] = new Label(" Minimum Height (Meters):");
        elementTitles[8] = new Label(" Notes:");
        addRoot.setValignment(elementTitles[8], VPos.TOP);
    }

    private void addChildrenToInputFormatEnvironment(HBox[] inputFormat, ComboBox environmentTypeSelector, TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea) {
        inputFormat[0].getChildren().add(environmentTypeSelector);
        inputFormat[1].getChildren().add(title);
        inputFormat[2].getChildren().add(minTemp);
        inputFormat[3].getChildren().add(maxTemp);
        inputFormat[4].getChildren().add(biomeOrSalinity);
        inputFormat[5].getChildren().add(humidityOrPH);
        inputFormat[6].getChildren().add(minAreaOrVolume);
        inputFormat[7].getChildren().add(minHeightOrOxygen);
        inputFormat[8].getChildren().add(notesArea);
    }

    private void setEnvironLandAquaOptions(ComboBox environmentTypeSelector, Label[] elementTitles) {

        environmentTypeSelector.getItems().addAll("Land", "Aquatic");
        environmentTypeSelector.getSelectionModel().select("Land");

        environmentTypeSelector.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    if (newValue.equals("Land")) {
                        elementTitles[4].setText(" Biome:");
                        elementTitles[5].setText(" Humidity (%):");
                        elementTitles[6].setText(" Minimum Area (Square Meters):");
                        elementTitles[7].setText(" Minimum Height (Meters):");

                    } else {
                        elementTitles[4].setText(" Salinity (%):");
                        elementTitles[5].setText(" pH:");
                        elementTitles[6].setText(" Volume (Cubic Meters):");
                        elementTitles[7].setText(" Oxygen (%):");

                    }
                }
        );
    }

    /**
     *
     * @param elementTitles
     * @param title
     * @param minTemp
     * @param maxTemp
     * @param biomeOrSalinity
     * @param humidityOrPH
     * @param minAreaOrVolume
     * @param minHeightOrOxygen
     * @param notesArea
     *
     * This method adds listeners to the environment text boxes and areas for length and content
     */
    private void environTxtBoxFilters(Label[] elementTitles, TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea) {
        title.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(title, oldVal, newVal, 6);
        });

        minTemp.textProperty().addListener((obs, oldVal, newVal) -> {
            doubleFilter(minTemp, oldVal, newVal, 6);
        });

        maxTemp.textProperty().addListener((obs, oldVal, newVal) -> {
            doubleFilter(maxTemp, oldVal, newVal, 6);
        });

        biomeOrSalinity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (elementTitles [4].getText().equals(" Salinity (%):")){
                posDblFilter(biomeOrSalinity, oldVal, newVal, 6);
            } else{
                trimTextFldToLength(biomeOrSalinity, oldVal, newVal, 50);
            }
        });

        humidityOrPH.textProperty().addListener((obs, oldVal, newVal) -> {
            posDblFilter(humidityOrPH, oldVal, newVal, 6);
        });

        minAreaOrVolume.textProperty().addListener((obs, oldVal, newVal) -> {
            posDblFilter(minAreaOrVolume, oldVal, newVal, 8);
        });

        minHeightOrOxygen.textProperty().addListener((obs, oldVal, newVal) -> {
            posDblFilter(minHeightOrOxygen, oldVal, newVal, 6);
        });

        notesArea.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextAreaToLength(notesArea, oldVal, newVal, 250);
        });
    }

    private void setGeneralEnvironmentFormat(int width, int numOfElements, HBox[] inputFormat, Label addNewElementPromptMainTitle, VBox mainRoot, GridPane addRoot, TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField humidityOrPH1, TextField minAreaOrVolume, TextField minHeightOrOxygen) {
        title.setPrefWidth(width);
        minTemp.setPrefWidth(width);
        maxTemp.setPrefWidth(width);
        biomeOrSalinity.setPrefWidth(width);
        humidityOrPH.setPrefWidth(width);
        minAreaOrVolume.setPrefWidth(width);
        minHeightOrOxygen.setPrefWidth(width);

        addNewElementPromptMainTitle.setStyle("-fx-font: 35 arial;");
        mainRoot.getChildren().addAll(addNewElementPromptMainTitle, new Separator(), addRoot);

        addRoot.setHgap(20);
        addRoot.setVgap(20);


        for (int i = 0; i < numOfElements; i++) {
            inputFormat[i] = new HBox(5);
        }
    }

    public void environmentEditor(TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea, Land landLocator, Aqua aquaLocator, Button delete, Stage addNewElementPromptStage, ComboBox environmentTypeSelector) {

        if (landLocator != null) {
            landEnvironmentEditor(title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea, landLocator, environmentTypeSelector);


        } else {
            aquaEnvironmentEditor(title, minTemp, maxTemp, biomeOrSalinity, humidityOrPH, minAreaOrVolume, minHeightOrOxygen, notesArea, aquaLocator, environmentTypeSelector);


        }

        delete.setOnAction(e -> {
            if (landLocator != null) myDB.deleteLand(landLocator.getID());
            else myDB.deleteAqua(aquaLocator.getID());

            addNewElementPromptStage.close();
            createAlert("Element successfully deleted", "Element successfully deletd", "The selected element has been removed from the database"); //maybe add an "are you sure message" but idk
        });
    }

    private void aquaEnvironmentEditor(TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea, Aqua aquaLocator, ComboBox environmentTypeSelector) {
        title.setText(aquaLocator.getTitle());
        maxTemp.setText(String.valueOf(aquaLocator.getTempMax()));
        minTemp.setText(String.valueOf(aquaLocator.getTempMin()));
        biomeOrSalinity.setText(String.valueOf(aquaLocator.getSalinity()));
        humidityOrPH.setText(String.valueOf(aquaLocator.getPH()));
        minAreaOrVolume.setText(String.valueOf(aquaLocator.getVolume()));
        minHeightOrOxygen.setText(String.valueOf(aquaLocator.getOxygen()));
        notesArea.setText(aquaLocator.getNotes());

        environmentTypeSelector.getSelectionModel().select("Aquatic");
        environmentTypeSelector.setDisable(true);
    }

    private void landEnvironmentEditor(TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea, Land landLocator, ComboBox environmentTypeSelector) {
        title.setText(landLocator.getTitle());
        maxTemp.setText(String.valueOf(landLocator.getTempMax()));
        minTemp.setText(String.valueOf(landLocator.getTempMin()));
        biomeOrSalinity.setText(landLocator.getBiome());
        humidityOrPH.setText(String.valueOf(landLocator.getHumidity()));
        minAreaOrVolume.setText(String.valueOf(landLocator.getMinArea()));
        minHeightOrOxygen.setText(String.valueOf(landLocator.getMinHeight()));
        notesArea.setText(landLocator.getNotes());

        environmentTypeSelector.getSelectionModel().select("Land");
        environmentTypeSelector.setDisable(true);
    }

    public void insertEnvironment(Land landLocator, Aqua aquaLocator, Stage addNewElementPromptStage, boolean mode, TextField title, TextField minTemp, TextField maxTemp, TextField biomeOrSalinity, TextField humidityOrPH, TextField minAreaOrVolume, TextField minHeightOrOxygen, TextArea notesArea, ComboBox environmentTypeSelector) {

        Integer environmentIDVal;

        String biomeVal = "";
        Double humidityVal = new Double(0);
        Double minAreaVal = new Double(0);
        Double minHeightVal = new Double(0);
        Double salinityVal = new Double(0);
        Double pHVal = new Double(0);
        Double volumeVal = new Double(0);
        Double oxygenVal = new Double(0);
        Double tempMaxVal = new Double(0);
        Double tempMinVal = new Double(0);
        new Double(0);
        new Double(0);
        new Double(0);

        String titleVal = title.getText();

        tempMaxVal = getDouble(tempMaxVal, maxTemp.getText());
        tempMinVal = getDouble(tempMinVal, minTemp.getText());
        String notesVal = notesArea.getText();

        if (environmentTypeSelector.getValue().equals("Land")) {
            environmentIDVal = getRandomInt(3); //prolly change later
            biomeVal = biomeOrSalinity.getText();
            humidityVal = getDouble(humidityVal, humidityOrPH.getText());
            minAreaVal = getDouble(minAreaVal, minAreaOrVolume.getText());
            minHeightVal = getDouble(minHeightVal, minHeightOrOxygen.getText());

        } else {
            environmentIDVal = getRandomInt(4); //prolly change later
            salinityVal = getDouble(salinityVal, biomeOrSalinity.getText());
            pHVal = getDouble(pHVal, humidityOrPH.getText());
            volumeVal = getDouble(volumeVal, minAreaOrVolume.getText());
            oxygenVal = getDouble(oxygenVal, minHeightOrOxygen.getText());

        }

        Integer[] foodIDs = {0};

        if (mode) {

            // *** changing environment already existing from land -> aqua and vice versa doesn't work.  Fix.  Later tho


            if (environmentTypeSelector.getValue().equals("Land"))
                myDB.editLand(landLocator.getID(), titleVal, biomeVal, tempMaxVal, tempMinVal, humidityVal, minHeightVal, minAreaVal, notesVal);
            else
                myDB.editAqua(aquaLocator.getID(), titleVal, salinityVal, pHVal, tempMaxVal, tempMinVal, volumeVal, oxygenVal, notesVal);

            addNewElementPromptStage.close();
            createAlert("Attributes Successfully Updated", "Attributes Successfully Updated", "The updated version of this element is now in the database");
        } else {

            if (environmentTypeSelector.getValue().equals("Land"))
                myDB.insertLand(environmentIDVal, titleVal, biomeVal, tempMaxVal, tempMinVal, humidityVal, minHeightVal, minAreaVal, notesVal);
            else
                myDB.insertAqua(environmentIDVal, titleVal, salinityVal, pHVal, tempMaxVal, tempMinVal, volumeVal, oxygenVal, notesVal);

            addNewElementPromptStage.close();
            createAlert("Element successfully added", "Element successfully added", "The new element is now in the database and can be accessed"); //bad alert, but whatever
        }
    }

    private Double getDouble(Double num, String string) {
        if (string.length() > 0) {
            num = Double.parseDouble(string);
        }
        return num;
    }

    public void addNewFoodPrompt(boolean mode, Food locator) {

        int numOfElements = 4;

        Stage addNewElementPromptStage = new Stage();


        VBox mainRoot = new VBox(10);
        GridPane addRoot = new GridPane();
        HBox[] inputFormat = new HBox[numOfElements];
        Label[] elementTitles = new Label[numOfElements];
        Label addNewElementPromptMainTitle = new Label(" Add New Food");

        TextField name = new TextField();
        TextField price = new TextField();
        TextField stock = new TextField();

        TextArea notesArea = new TextArea();

        setGeneralFoodFormat(name, price, stock, addNewElementPromptMainTitle, mainRoot, addRoot, 480);

        for (int i = 0; i < numOfElements; i++) {
            inputFormat[i] = new HBox(5);
        }


        inputFormat[0].getChildren().add(name);
        inputFormat[1].getChildren().add(price);

        ComboBox inStock = new ComboBox();
        inStock.getItems().addAll("Food in Stock", "None in Stock");

        inputFormat[2].getChildren().add(inStock);
        inputFormat[3].getChildren().add(notesArea);

        foodTxtBoxFilters(name, price, notesArea);

        elementTitles[0] = new Label(" Name:");
        elementTitles[1] = new Label(" Price ($ / kg):");
        elementTitles[2] = new Label(" Stock:");
        elementTitles[3] = new Label(" Notes:");
        addRoot.setValignment(elementTitles[3], VPos.TOP);


        for (int i = 0; i < numOfElements; i++) {

            addRoot.add(inputFormat[i], 1, i);
            addRoot.add(elementTitles[i], 0, i);

        }


        Button initializerAddButton = new Button("Add Food");

        addRoot.add(new Label(""), 1, numOfElements);
        addRoot.add(initializerAddButton, 1, numOfElements + 1);

        Scene scene = new Scene(mainRoot, 750, 780);

        foodBtns(scene, mode, addRoot, numOfElements, addNewElementPromptMainTitle, initializerAddButton, name, price, notesArea, locator, addNewElementPromptStage, inStock);

    }

    /**
     *
     * @param name
     * @param price
     * @param notesArea
     *
     * This method adds listeners for length and content to the text boxes and areas of food
     */
    private void foodTxtBoxFilters(TextField name, TextField price, TextArea notesArea) {
        name.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextFldToLength(name, oldVal, newVal, 50);
        });
        price.textProperty().addListener((obs, oldVal, newVal) -> {
            doubleFilter(price, oldVal, newVal, 8);
        });
        notesArea.textProperty().addListener((obs, oldVal, newVal) -> {
            trimTextAreaToLength(notesArea, oldVal, newVal, 250);
        });
    }

    private void setGeneralFoodFormat(TextField name, TextField price, TextField stock, Label addNewElementPromptMainTitle, VBox mainRoot, GridPane addRoot, int width) {

        name.setPrefWidth(width);
        price.setPrefWidth(width);
        stock.setPrefWidth(width);


        addNewElementPromptMainTitle.setStyle("-fx-font: 35 arial;");
        mainRoot.getChildren().addAll(addNewElementPromptMainTitle, new Separator(), addRoot);

        addRoot.setHgap(20);
        addRoot.setVgap(20);
    }

    private void foodBtns(Scene scene, boolean mode, GridPane addRoot, int numOfElements, Label addNewElementPromptMainTitle, Button initializerAddButton, TextField name, TextField price, TextArea notesArea, Food locator, Stage addNewElementPromptStage, ComboBox inStock) {
        scene.getStylesheets().add("./style.css");
        addNewElementPromptStage.setScene(scene);
        addNewElementPromptStage.show();

        initializerAddButton.setOnAction(e -> {
            insertFood(locator, addNewElementPromptStage, mode, name, price, notesArea, inStock);
        });
        if (mode) {

            Button delete = new Button("Delete");
            addRoot.add(delete, 1, numOfElements + 2);
            addNewElementPromptMainTitle.setText(" Food Editor");
            initializerAddButton.setText("Save Changes");


            foodEditor(name, price, notesArea, locator, delete, addNewElementPromptStage, inStock);
        }
    }

    public void foodEditor(TextField name, TextField price, TextArea notesArea, Food locator, Button delete, Stage addNewElementPromptStage, ComboBox inStock) {

        name.setText(locator.getName());
        price.setText(String.valueOf(locator.getPrice()));
        notesArea.setText(locator.getNotes());

        if (locator.getStock())
            inStock.getSelectionModel().select("Food in Stock");
        else
            inStock.getSelectionModel().select("None in Stock");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                myDB.deleteFood(locator.getFoodID());
                addNewElementPromptStage.close();
                createAlert("Element successfully deleted", "Element successfully deleted", "The selected element has been removed from the database"); //maybe add an "are you sure message" but idk

            }
        });

    }

    public void insertFood(Food locator, Stage addNewElementPromptStage, boolean mode, TextField name, TextField price, TextArea notesArea, ComboBox inStock) { //method may be useless

        Integer foodIDVal = getRandomInt(1); //prolly change later
        boolean stockVal = false;
        String nameVal = name.getText();
        Double priceVal = new Double(0);
        priceVal = getDouble(priceVal, price.getText());

        if(inStock.getValue() != null) {

            if (inStock.getValue().equals("None in Stock"))
                stockVal = false;
            else
                stockVal = true;
        }

        String notesVal = notesArea.getText();


        if (mode) {

            myDB.editFood(locator.getFoodID(), nameVal, priceVal, stockVal, notesVal);
            addNewElementPromptStage.close();
            createAlert("Attributes Successfully Updated", "Attributes Successfully Updated", "The updated version of this element is now in the database");
        } else {
            myDB.insertFood(foodIDVal, nameVal, priceVal, stockVal, notesVal);
            addNewElementPromptStage.close();
            createAlert("Element Successfully Added", "Element Successfully Added", "The new element is now in the database and can be accessed"); //bad alert, but whatever
        }
    }

    public void intFilter(TextField txtFld, String oldVal, String newVal, int maxLen) {
        if (newVal.length() > 0) {
            txtFld.setText(txtFld.getText().trim());
            if (txtFld.getText().length() > maxLen) {
                txtFld.setText(oldVal);
                newVal = oldVal;
            }
            if (!newVal.matches("\\d*")) {
                txtFld.setText(newVal.replaceAll("[^\\d]", ""));
            }
        }
    }

    public void doubleFilter(TextField txtFld, String oldVal, String newVal, int maxLen) {
        if (newVal.length() > 0) {
            txtFld.setText(txtFld.getText().trim());
            if (txtFld.getText().length() > maxLen) {
                txtFld.setText(oldVal);
                newVal = oldVal;
            }
            if (!newVal.matches("(\\-?\\d*\\.?\\d+)")) {
                txtFld.setText(newVal.replaceAll("[^\\-?\\d*\\.?\\d+]", ""));
            }
        }

    }

    public void posDblFilter(TextField txtFld, String oldVal, String newVal, int maxLen) {
        txtFld.setText( txtFld.getText().trim());
        if (newVal.length() > 0) {
            if (txtFld.getText().length() > maxLen) {
                if(oldVal.length() > maxLen){
                    Platform.runLater(() -> {
                        txtFld.clear();
                    });
                } else {
                    txtFld.setText(oldVal);
                    newVal = oldVal;

                }
            }

            if (!oldVal.matches("(\\d*\\.?\\d+)") && oldVal.length() > 0) {
                Platform.runLater(() -> {
                    txtFld.clear();
                });
            } else {
                if (!newVal.matches("(\\d*\\.?\\d+)")) {
                    txtFld.setText(newVal.replaceAll("[^\\d*\\.?\\d+]", ""));
                }
            }
        }
    }

    public void trimTextAreaToLength(TextArea txtArea, String oldVal, String newVal, int maxLen) {

        if (newVal.length() > 0) {
            if (txtArea.getText().length() > maxLen) {
                if(oldVal.length() > maxLen){
                    Platform.runLater(() -> {
                        txtArea.clear();
                    });
                } else {
                    txtArea.setText(oldVal);
                    newVal = oldVal;

                }
            }
        }
    }

    public void trimTextFldToLength(TextField txtFld, String oldVal, String newVal, int maxLen) {

        if (newVal.length() > 0) {
            if (txtFld.getText().length() > maxLen) {
                if(oldVal.length() > maxLen){
                    Platform.runLater(() -> {
                        txtFld.clear();
                    });
                } else {
                    txtFld.setText(oldVal);
                    newVal = oldVal;

                }
            }
        }
    }

    public void createAlert(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public Integer getRandomInt(int i) {//Ella - change later and maybe send this method the bounds when Its being done
        Random rand = new Random();
        Integer randomInt = rand.nextInt(9000000) + 1000000;

        while(myDB.readIDs(randomInt + "", i)){
            randomInt = rand.nextInt(9000000) + 1000000;
        }

        return randomInt;
    }

    /**
     * @param what String is the string of that the warining is about
     *
     * This method pops up an error alert and tells the user that there has been an error
     */
    private void alert(String what) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(what);
    }

}








