package org.example.recipevault;

/**
 * Recipe Vault Application
 *
 * This JavaFX application provides a user interface to enter recipe information.
 * Users can:
 *  - Input recipe metadata (name, category, author, time, servings, theme)
 *  - Add and remove up to 15 ingredients
 *  - Add and remove up to 25 cooking instruction steps
 *  - Enter notes or tips for the recipe
 *  - Reset the form to clear all inputs
 *  - Receive validation alerts via pop-up dialogs
 *
 * The application window opens centered horizontally and maximized vertically.
 * Lists for ingredients and instructions use ObservableLists linked to ListView components.
 *
 * Developed using JavaFX UI components like BorderPane, GridPane, ListView, TextField, and TextArea.
 */

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//Main JavaFX application class for Recipe Vault
public class Main extends Application {
    // Recipe attributes
    private TextField recipeNameField;
    private Label recipeNameCount;
    private int recipeNameLimit = 18;
    private TextField recipeCategoryField;
    private Label recipeCategoryCount;
    private int recipeCategoryLimit = 15;
    private TextField prepTimeField;
    private Label prepTimeCount;
    private int prepTimeLimit = 15;
    private TextField cookTimeField;
    private Label cookTimeCount;
    private int cookTimeLimit = 15;
    private TextField totalTimeField;
    private Label totalTimeCount;
    private int totalTimeLimit = 15;
    private TextField servingsField;
    private Label servingsCount;
    private int servingsLimit = 15;
    private TextField recipeAuthorField;
    private Label recipeAuthorCount;
    private int recipeAuthorLimit = 27;
    private ComboBox<String> themeComboBox;

    // Ingredients
    private TextField ingredientField;
    private Label ingredientCount;
    private int ingredientLimit = 38;
    private ListView<String> ingredientListView;
    private ObservableList<String> ingredientsList = FXCollections.observableArrayList();

    // Instructions
    private TextField instructionField;
    private Label instructionCount;
    private int instructionLimit = 77;
    private ListView<String> instructionListView;
    private ObservableList<String> instructionsList = FXCollections.observableArrayList();

    // Notes
    private TextArea notesArea;
    private Label notesCount;
    private int notesLimit = 700;

    // Status label
    private Label statusLabel;

    // Color constants for styling
    private final String CREAM_BACKGROUND = "#FFF8E1";
    private final String BROWN_BUTTON = "#8B4513";
    private final String BROWN_LIGHT = "#D2B48C";
    private final String BROWN_DARK = "#654321";
    private final String BUTTON_TEXT = "white";

    // Color object for dark brown text
    private final javafx.scene.paint.Color DARK_BROWN_COLOR = javafx.scene.paint.Color.web(BROWN_DARK);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Recipe Vault");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: " + CREAM_BACKGROUND + ";");

        VBox headerBox = createHeader();
        mainLayout.setTop(headerBox);

        // Note for character counters
        Label characterLimitExplanation = new Label("Numbers show current text length / maximum characters allowed");
        characterLimitExplanation.setStyle(
                "-fx-font-family: 'Georgia';" +
                        "-fx-font-style: italic;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 0 0 10 0;" +
                        "-fx-background-color: " + CREAM_BACKGROUND + ";"
        );
        // Set text color directly
        characterLimitExplanation.setTextFill(DARK_BROWN_COLOR);

        VBox contentWithNote = new VBox(10);
        contentWithNote.setStyle("-fx-background-color: " + CREAM_BACKGROUND + ";");
        contentWithNote.getChildren().add(characterLimitExplanation);

        GridPane contentGrid = createContentGrid();
        contentWithNote.getChildren().add(contentGrid);
        mainLayout.setCenter(contentWithNote);

        HBox buttonBox = createButtons(primaryStage);
        mainLayout.setBottom(buttonBox);

        Scene scene = new Scene(mainLayout, 800, javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());
        scene.getRoot().setStyle("-fx-font-family: 'Georgia';");

        primaryStage.centerOnScreen();
        primaryStage.setOnShown(e -> primaryStage.setX((javafx.stage.Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2));
        primaryStage.setY(0);
        primaryStage.setScene(scene);
        primaryStage.setHeight(javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Creates the top header section with title and slogan
    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        headerBox.setStyle("-fx-background-color: " + CREAM_BACKGROUND + ";");

        Label titleLabel = new Label("Recipe Vault");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        titleLabel.setTextFill(DARK_BROWN_COLOR);  // Set dark brown color

        Label sloganLabel = new Label("Preserve the past, cook for the future!");
        sloganLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 14));
        sloganLabel.setTextFill(DARK_BROWN_COLOR);  // Set dark brown color

        headerBox.getChildren().addAll(titleLabel, sloganLabel);
        return headerBox;
    }

    // Creates the main content area including form fields, ingredient and instruction lists
    private GridPane createContentGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: " + CREAM_BACKGROUND + ";");

        int row = 0;

        String labelStyle = "-fx-font-family: 'Georgia'; -fx-font-size: 14px;";

        String textFieldStyle = "-fx-font-family: 'Georgia'; -fx-font-size: 14px;";

        Label recipeNameLabel = new Label("Recipe Name:");
        recipeNameLabel.setStyle(labelStyle);
        recipeNameLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(recipeNameLabel, 0, row);

        recipeNameField = new TextField();
        recipeNameField.setStyle(textFieldStyle);

        recipeNameField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        HBox recipeNameBox = new HBox(10);

        recipeNameField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= recipeNameLimit) {
                return change;
            } else {
                return null;
            }
        }));

        recipeNameCount = new Label("0/" + recipeNameLimit);
        recipeNameCount.setStyle(labelStyle);
        recipeNameCount.setTextFill(DARK_BROWN_COLOR);
        recipeNameBox.getChildren().addAll(recipeNameField, recipeNameCount);
        grid.add(recipeNameBox, 1, row);

        // Use helper method to set up the character counter
        setupCharacterCounter(recipeNameCount, recipeNameField.textProperty(), recipeNameLimit);

        row++;
        Label categoryLabel = new Label("Cuisine:");
        categoryLabel.setStyle(labelStyle);
        categoryLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(categoryLabel, 0, row);

        recipeCategoryField = new TextField();
        recipeCategoryField.setStyle(textFieldStyle);
        recipeCategoryField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        recipeCategoryCount = new Label("0/" + recipeCategoryLimit);
        recipeCategoryCount.setStyle(labelStyle);
        recipeCategoryCount.setTextFill(DARK_BROWN_COLOR);

        // Use helper method for category count
        setupCharacterCounter(recipeCategoryCount, recipeCategoryField.textProperty(), recipeCategoryLimit);

        recipeCategoryField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= recipeCategoryLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(recipeCategoryField, 1, row);
        grid.add(recipeCategoryCount, 2, row);

        row++;
        Label authorLabel = new Label("Recipe Author:");
        authorLabel.setStyle(labelStyle);
        authorLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(authorLabel, 0, row);

        recipeAuthorCount = new Label("0/" + recipeAuthorLimit);
        recipeAuthorCount.setStyle(labelStyle);
        recipeAuthorCount.setTextFill(DARK_BROWN_COLOR);

        recipeAuthorField = new TextField();
        recipeAuthorField.setStyle(textFieldStyle);
        recipeAuthorField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for author count
        setupCharacterCounter(recipeAuthorCount, recipeAuthorField.textProperty(), recipeAuthorLimit);

        recipeAuthorField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= recipeAuthorLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(recipeAuthorField, 1, row);
        grid.add(recipeAuthorCount, 2, row);

        row++;
        Label prepTimeLabel = new Label("Prep Time:");
        prepTimeLabel.setStyle(labelStyle);
        prepTimeLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(prepTimeLabel, 0, row);

        prepTimeCount = new Label("0/" + prepTimeLimit);
        prepTimeCount.setStyle(labelStyle);
        prepTimeCount.setTextFill(DARK_BROWN_COLOR);

        prepTimeField = new TextField();
        prepTimeField.setStyle(textFieldStyle);
        prepTimeField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for prep time count
        setupCharacterCounter(prepTimeCount, prepTimeField.textProperty(), prepTimeLimit);

        prepTimeField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= prepTimeLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(prepTimeField, 1, row);
        grid.add(prepTimeCount, 2, row);

        row++;
        Label cookTimeLabel = new Label("Cook Time:");
        cookTimeLabel.setStyle(labelStyle);
        cookTimeLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(cookTimeLabel, 0, row);

        cookTimeCount = new Label("0/" + cookTimeLimit);
        cookTimeCount.setStyle(labelStyle);
        cookTimeCount.setTextFill(DARK_BROWN_COLOR);

        cookTimeField = new TextField();
        cookTimeField.setStyle(textFieldStyle);
        cookTimeField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for cook time count
        setupCharacterCounter(cookTimeCount, cookTimeField.textProperty(), cookTimeLimit);

        cookTimeField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= cookTimeLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(cookTimeField, 1, row);
        grid.add(cookTimeCount, 2, row);

        row++;
        Label totalTimeLabel = new Label("Total Time:");
        totalTimeLabel.setStyle(labelStyle);
        totalTimeLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(totalTimeLabel, 0, row);

        totalTimeCount = new Label("0/" + totalTimeLimit);
        totalTimeCount.setStyle(labelStyle);
        totalTimeCount.setTextFill(DARK_BROWN_COLOR);

        totalTimeField = new TextField();
        totalTimeField.setStyle(textFieldStyle);
        totalTimeField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for total time count
        setupCharacterCounter(totalTimeCount, totalTimeField.textProperty(), totalTimeLimit);

        totalTimeField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= totalTimeLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(totalTimeField, 1, row);
        grid.add(totalTimeCount, 2, row);

        row++;
        Label servingsLabel = new Label("Servings:");
        servingsLabel.setStyle(labelStyle);
        servingsLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(servingsLabel, 0, row);

        servingsCount = new Label("0/" + servingsLimit);
        servingsCount.setStyle(labelStyle);
        servingsCount.setTextFill(DARK_BROWN_COLOR);

        servingsField = new TextField();
        servingsField.setStyle(textFieldStyle);
        servingsField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for servings count
        setupCharacterCounter(servingsCount, servingsField.textProperty(), servingsLimit);

        servingsField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= servingsLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(servingsField, 1, row);
        grid.add(servingsCount, 2, row);

        row++;
        Label themeLabel = new Label("Theme:");
        themeLabel.setStyle(labelStyle);
        themeLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(themeLabel, 0, row);

        themeComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Spring", "Summer", "Fall", "Winter"
        ));
        themeComboBox.setPromptText("Select theme");
        themeComboBox.setEditable(false);
        themeComboBox.setStyle(
                "-fx-font-family: 'Georgia';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: " + BROWN_BUTTON + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-mark-color: white;" +
                        "-fx-prompt-text-fill: white;"
        );

        // Forces text to be white for selected items
        themeComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select theme");
                } else {
                    setText(item);
                }
                setTextFill(javafx.scene.paint.Color.WHITE);
            }
        });

        // Makes popup list items dark brown
        themeComboBox.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
                setTextFill(DARK_BROWN_COLOR);
            }
        });

        grid.add(themeComboBox, 1, row);

        row++;
        Separator separator1 = new Separator();
        grid.add(separator1, 0, row, 2, 1);

        row++;
        Label ingredientsHeaderLabel = new Label("Ingredients");
        ingredientsHeaderLabel.setStyle(labelStyle + "-fx-font-weight: bold; -fx-font-size: 16px;");
        ingredientsHeaderLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(ingredientsHeaderLabel, 0, row, 2, 1);

        row++;
        Label addIngredientLabel = new Label("Add Ingredient:");
        addIngredientLabel.setStyle(labelStyle);
        addIngredientLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(addIngredientLabel, 0, row);

        ingredientCount = new Label("0/" + ingredientLimit);
        ingredientCount.setStyle(labelStyle);
        ingredientCount.setTextFill(DARK_BROWN_COLOR);

        ingredientField = new TextField();
        ingredientField.setStyle(textFieldStyle);
        ingredientField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        // Use helper method for ingredient count
        setupCharacterCounter(ingredientCount, ingredientField.textProperty(), ingredientLimit);

        ingredientField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= ingredientLimit) {
                return change;
            } else {
                return null;
            }
        }));

        Button addIngredientButton = new Button("Add");
        addIngredientButton.setStyle("-fx-background-color: " + BROWN_BUTTON + "; -fx-text-fill: " + BUTTON_TEXT + "; -fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        addIngredientButton.setOnAction(e -> addIngredient());
        HBox ingredientBox = new HBox(10, ingredientField, addIngredientButton);
        grid.add(ingredientBox, 1, row);
        grid.add(ingredientCount, 2, row);

        // Add support for pressing Enter in ingredientField
        ingredientField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String ingredient = ingredientField.getText();
                if ((!ingredient.isEmpty() && (ingredientsList.size() < 15))) {
                    ingredientsList.add(ingredient);
                    ingredientField.clear();
                }
                else if (ingredientsList.size() >= 15) {
                    showMessageDialog("You can only add up to 15 ingredients.");
                }
            }
        });

        row++;
        Label ingredientListLabel = new Label("Ingredient List:");
        ingredientListLabel.setStyle(labelStyle);
        ingredientListLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(ingredientListLabel, 0, row);

        ingredientListView = new ListView<>(ingredientsList);
        ingredientListView.setPrefHeight(400);
        ingredientListView.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 14px;");

        grid.add(ingredientListView, 1, row);

        row++;
        Button removeIngredientButton = new Button("Remove Selected Ingredient");
        removeIngredientButton.setStyle("-fx-background-color: " + BROWN_BUTTON + "; -fx-text-fill: " + BUTTON_TEXT + "; -fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        removeIngredientButton.setOnAction(e -> removeIngredient());
        grid.add(removeIngredientButton, 1, row);

        row++;
        Separator separator2 = new Separator();
        grid.add(separator2, 0, row, 2, 1);

        row++;
        Label instructionsHeaderLabel = new Label("Cooking Instructions");
        instructionsHeaderLabel.setStyle(labelStyle + "-fx-font-weight: bold; -fx-font-size: 16px;");
        instructionsHeaderLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(instructionsHeaderLabel, 0, row, 2, 1);

        row++;
        Label addInstructionLabel = new Label("Add Instruction:");
        addInstructionLabel.setStyle(labelStyle);
        addInstructionLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(addInstructionLabel, 0, row);

        instructionField = new TextField();
        instructionField.setStyle(textFieldStyle);
        instructionField.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        instructionCount = new Label("0/" + instructionLimit);
        instructionCount.setStyle(labelStyle);
        instructionCount.setTextFill(DARK_BROWN_COLOR);

        // Use helper method for instruction count
        setupCharacterCounter(instructionCount, instructionField.textProperty(), instructionLimit);

        instructionField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= instructionLimit) {
                return change;
            } else {
                return null;
            }
        }));

        instructionField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 100) instructionField.setText(oldVal);
        });

        Button addInstructionButton = new Button("Add");
        addInstructionButton.setStyle("-fx-background-color: " + BROWN_BUTTON + "; -fx-text-fill: " + BUTTON_TEXT + "; -fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        addInstructionButton.setOnAction(e -> {
            addInstruction();
            statusLabel = new Label("");
            GridPane.setRowIndex(statusLabel, GridPane.getRowIndex(instructionListView) - 1);
        });

        // Add support for pressing Enter in instructionField
        instructionField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String step = instructionField.getText();
                if (!step.isEmpty() && (instructionsList.size() < 25)) {
                    instructionsList.add(step);
                    instructionField.clear();
                }
                else if(instructionsList.size() >= 25) {
                    showMessageDialog("You can only add up to 25 steps.");
                }
            }
        });

        HBox instructionBox = new HBox(10, instructionField, addInstructionButton);
        grid.add(instructionBox, 1, row);
        grid.add(instructionCount, 2, row);

        row++;
        Label instructionListLabel = new Label("Instruction List:");
        instructionListLabel.setStyle(labelStyle);
        instructionListLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(instructionListLabel, 0, row);

        instructionListView = new ListView<>(instructionsList);
        instructionListView.setPrefHeight(400);
        instructionListView.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        grid.add(instructionListView, 1, row);

        row++;
        Button removeInstructionButton = new Button("Remove Selected Instruction");
        removeInstructionButton.setStyle("-fx-background-color: " + BROWN_BUTTON + "; -fx-text-fill: " + BUTTON_TEXT + "; -fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        removeInstructionButton.setOnAction(e -> removeInstruction());
        grid.add(removeInstructionButton, 1, row);

        row++;
        Separator separator3 = new Separator();
        grid.add(separator3, 0, row, 2, 1);

        row++;
        Label notesHeaderLabel = new Label("Notes/Tips");
        notesHeaderLabel.setStyle(labelStyle + "-fx-font-weight: bold; -fx-font-size: 16px;");
        notesHeaderLabel.setTextFill(DARK_BROWN_COLOR);
        grid.add(notesHeaderLabel, 0, row, 2, 1);

        row++;
        notesArea = new TextArea();
        notesArea.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        notesArea.setStyle("-fx-text-fill: " + BROWN_DARK + ";");

        notesCount = new Label("0/" + notesLimit);
        notesCount.setStyle(labelStyle);
        notesCount.setTextFill(DARK_BROWN_COLOR);
        notesArea.setPrefHeight(400);

        // Use helper method for notes count
        setupCharacterCounter(notesCount, notesArea.textProperty(), notesLimit);

        notesArea.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= notesLimit) {
                return change;
            } else {
                return null;
            }
        }));

        grid.add(notesArea, 1, row);
        grid.add(notesCount, 2, row);

        return grid;
    }

    // Creates the bottom row of buttons (Save, Reset, Close)
    private HBox createButtons(Stage stage) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPadding(new Insets(20, 0, 0, 0));
        box.setStyle("-fx-background-color: " + CREAM_BACKGROUND + ";");

        String buttonStyle = "-fx-background-color: " + BROWN_BUTTON + "; " +
                "-fx-text-fill: " + BUTTON_TEXT + "; " +
                "-fx-font-family: 'Georgia'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 5 15 5 15;";

        Button save = new Button("Save");
        save.setStyle(buttonStyle);

        //Save user input to pdf if all fields are field or selected besides notes
        save.setOnAction(e -> {
            if(validateFields())
            {
                saveRecipeToPDF();
            }
        });

        Button reset = new Button("Reset");
        reset.setStyle(buttonStyle);
        reset.setOnAction(e -> resetForm());

        Button close = new Button("Close");
        close.setStyle(buttonStyle);
        close.setOnAction(e -> stage.close());

        box.getChildren().addAll(save, reset, close);
        return box;
    }

    // Validates if all required fields are filled
    private boolean validateFields() {
        List<String> emptyFields = new ArrayList<>();
        if (recipeNameField.getText().trim().isEmpty()) {
            emptyFields.add("Recipe Name");
        }
        if (recipeCategoryField.getText().trim().isEmpty()) {
            emptyFields.add("Category/Cuisine");
        }
        if (recipeAuthorField.getText().trim().isEmpty()) {
            emptyFields.add("Recipe Author");
        }
        if (prepTimeField.getText().trim().isEmpty()) {
            emptyFields.add("Prep Time");
        }
        if (cookTimeField.getText().trim().isEmpty()) {
            emptyFields.add("Cook Time");
        }
        if (totalTimeField.getText().trim().isEmpty()) {
            emptyFields.add("Total Time");
        }
        if (servingsField.getText().trim().isEmpty()) {
            emptyFields.add("Servings");
        }
        if (themeComboBox.getValue() == null) {
            emptyFields.add("Theme");
        }
        if (ingredientsList.isEmpty()) {
            emptyFields.add("Ingredients List");
        }
        if (instructionsList.isEmpty()) {
            emptyFields.add("Cooking Instructions");
        }
        if (!emptyFields.isEmpty()) {
            showMessageDialog("The following fields cannot be empty: " + String.join(", ", emptyFields));
            return false;
        }

        return true;
    }

    // Adds an ingredient to the list if valid and under limit
    private void addIngredient() {
        String ingredient = ingredientField.getText().trim();
        if (ingredientsList.size() >= 15) {
            showMessageDialog("You can only add up to 15 ingredients.");
            return;
        }
        if (!ingredient.isEmpty() && !ingredientsList.contains(ingredient)) {
            ingredientsList.add(ingredient);
            ingredientField.clear();
        } else {
            showMessageDialog("Ingredient already in the list or empty");
        }
        statusLabel = new Label("Ingredient already in the list or empty");
        statusLabel.setStyle("-fx-font-family: 'Georgia';");
        statusLabel.setTextFill(DARK_BROWN_COLOR);
    }

    // Removes the selected ingredient from the list
    private void removeIngredient() {
        String selected = ingredientListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ingredientsList.remove(selected);
            showMessageDialog("Ingredient '" + selected + "' removed");
        } else {
            showMessageDialog("Select an ingredient to remove");
        }
    }

    // Adds an instruction step to the list if valid and under limit
    private void addInstruction() {
        String step = instructionField.getText().trim();
        if (instructionsList.size() >= 25) {
            showMessageDialog("You can only add up to 25 instruction steps.");
            return;
        }
        if (!step.isEmpty() && !instructionsList.contains(step)) {
            instructionsList.add(step);
            instructionField.clear();
        } else {
            showMessageDialog("Instruction already in the list or empty");
        }
        statusLabel = new Label("Instruction already in the list or empty");
        statusLabel.setStyle("-fx-font-family: 'Georgia';");
        statusLabel.setTextFill(DARK_BROWN_COLOR);
    }

    // Removes the selected instruction from the list
    private void removeInstruction() {
        String selected = instructionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            instructionsList.remove(selected);
            showMessageDialog("Instruction removed");
        } else {
            showMessageDialog("Select an instruction to remove");
        }
    }

    // Shows a popup dialog with a given message
    private void showMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recipe Vault");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply styling to the dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CREAM_BACKGROUND + "; -fx-font-family: 'Georgia';");

        // Style the content text with dark brown
        Label contentLabel = (Label) dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setTextFill(DARK_BROWN_COLOR);
        }

        // Find all buttons in the dialog and style them
        dialogPane.lookupAll(".button").forEach(node -> {
            ((Button) node).setStyle("-fx-background-color: " + BROWN_BUTTON + "; -fx-text-fill: " + BUTTON_TEXT + "; -fx-font-family: 'Georgia'; -fx-font-size: 14px;");
        });

        alert.showAndWait();
    }

    /**
     * Helper method to set up a character counter with proper styling
     * @param countLabel The label that will display the character count
     * @param textProperty The text property to bind to for counting
     * @param limit The character limit
     */
    private void setupCharacterCounter(Label countLabel, javafx.beans.property.StringProperty textProperty, int limit) {
        // Initial styling
        countLabel.setStyle(
                "-fx-background-color: " + BROWN_LIGHT + ";" +
                        "-fx-padding: 2 8 2 8;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-family: 'Georgia';" +
                        "-fx-font-size: 11px;"
        );

        // Set text color directly
        countLabel.setTextFill(DARK_BROWN_COLOR);

        // Bind text content
        countLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            int length = textProperty.getValue().length();
            return length + "/" + limit;
        }, textProperty));

    }

    private void saveRecipeToPDF() {
        String name = recipeNameField.getText();
        String category = recipeCategoryField.getText();
        String author = recipeAuthorField.getText();
        String prepTime = prepTimeField.getText();
        String cookTime = cookTimeField.getText();
        String totalTime = totalTimeField.getText();
        String servings = servingsField.getText();
        String theme = themeComboBox.getValue();
        List<String> ingredients = new ArrayList<>(ingredientsList);
        List<String> instructions = new ArrayList<>(instructionsList);
        String notes = notesArea.getText();

        RecipePDFWriter.saveRecipeToPDF(name, category, author, prepTime, cookTime, totalTime, servings, theme, ingredients, instructions, notes);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Resets all input fields and clears ingredient and instruction lists
    private void resetForm() {
        recipeNameField.clear();
        recipeCategoryField.clear();
        prepTimeField.clear();
        cookTimeField.clear();
        totalTimeField.clear();
        servingsField.clear();
        recipeAuthorField.clear();
        themeComboBox.getEditor().clear();
        themeComboBox.setValue(null);

        ingredientField.clear();
        ingredientsList.clear();

        instructionField.clear();
        instructionsList.clear();

        notesArea.clear();
    }
}