package org.example.recipevault;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * RecipePDFWriter is a utility class responsible for filling out an existing PDF template with recipe details.
 * It uses Apache PDFBox to load the template, fill in the form fields, and save the new recipe PDF.
 */
public class RecipePDFWriter extends Main {

    // Constants for maximum ingredients and directions
    private static final int MAX_INGREDIENTS = 15;
    private static final int MAX_DIRECTIONS = 25;

    /**
     * Fills out the recipe details in an existing PDF template and saves the filled PDF.
     *
     * @param recipeName    The name of the recipe.
     * @param category      The category of the recipe.
     * @param author        The author of the recipe.
     * @param prepTime      The preparation time of the recipe.
     * @param cookTime      The cooking time of the recipe.
     * @param totalTime     The total time required for the recipe.
     * @param servings      The number of servings the recipe provides.
     * @param theme         The theme of the recipe.
     * @param ingredients   A list of ingredients required for the recipe.
     * @param instructions A list of cooking instructions (directions).
     * @param notes         Any additional notes related to the recipe.
     */
    public static void saveRecipeToPDF(String recipeName, String category, String author, String prepTime,
                                       String cookTime, String totalTime, String servings, String theme,
                                       List<String> ingredients, List<String> instructions, String notes) {

        // Validation for required fields
        if (recipeName.isEmpty() || category.isEmpty() || author.isEmpty() || prepTime.isEmpty() ||
                cookTime.isEmpty() || totalTime.isEmpty() || servings.isEmpty()) {
            showErrorDialog("All fields except Notes must be filled.");
            return;
        }

        // Validation for maximum number of ingredients
        if (ingredients.size() > MAX_INGREDIENTS) {
            showErrorDialog("The number of ingredients cannot exceed " + MAX_INGREDIENTS + ".");
            return;
        }

        // Validation for maximum number of directions
        if (instructions.size() > MAX_DIRECTIONS) {
            showErrorDialog("The number of directions cannot exceed " + MAX_DIRECTIONS + ".");
            return;
        }

        // Select template file based on theme
        String templateFileName;
        switch (theme.toLowerCase()) {
            case "summer":
                templateFileName = "Summer_Template.pdf";
                break;
            case "winter":
                templateFileName = "Winter_Template.pdf";
                break;
            case "spring":
                templateFileName = "Spring_Template.pdf";
                break;
            case "fall":
                templateFileName = "Fall_Template.pdf";
                break;
            default:
                showErrorDialog("Invalid theme. Please choose from Summer, Winter, Spring, or Fall.");
                return;
        }

        InputStream inputStream = RecipePDFWriter.class.getResourceAsStream("/template/" + templateFileName);

        File templateFile = new File("G:/UMGC/Computer Science Capstone/intellij(java)/RecipeVault/src/main/resources/template/" + templateFileName);
        if (!templateFile.exists()) {
            showErrorDialog("Template file not found: " + templateFileName);
            return;
        }

        if (inputStream == null) {
            showErrorDialog("Template file not found in resources: " + templateFileName);
            return;
        }


        try (PDDocument document = PDDocument.load(inputStream)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                // Fill in the form fields
                fillFormField(acroForm, "Recipe", recipeName);
                fillFormField(acroForm, "Author", author);
                fillFormField(acroForm, "Prep Time", prepTime);
                fillFormField(acroForm, "Cook Time", cookTime);
                fillFormField(acroForm, "Total Time", totalTime);
                fillFormField(acroForm, "Serves", servings);
                fillFormField(acroForm, "Meal Type", category);


                // Fill in ingredients
                for (int i = 0; i < ingredients.size(); i++) {
                    String ingredientField = "Ingredient" + (i + 1);
                    fillFormField(acroForm, ingredientField, ingredients.get(i));
                }

                // Fill in directions (steps)
                for (int i = 0; i < instructions.size(); i++) {
                    String directionField = "Direction" + (i + 1);
                    fillFormField(acroForm, directionField, instructions.get(i));
                }

                // Fill in notes
                fillFormField(acroForm, "Notes", notes);

                // Save the filled-out PDF
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                saveFileChooser.setTitle("Save Recipe");
                saveFileChooser.setInitialFileName(recipeName + "_filled.pdf");
                File saveFile = saveFileChooser.showSaveDialog(new Stage());



                if (saveFile != null) {
                    document.save(saveFile);
                    showInfoDialog("Recipe saved successfully!");
                }
            } else {
                showErrorDialog("No form fields found in the PDF template.");
            }
        } catch (IOException e) {
            showErrorDialog("Error loading or saving the PDF template.");
        }

    }

    /**
     * Fills a specific form field in the PDF.
     *
     * @param acroForm   The AcroForm object containing the form fields.
     * @param fieldName  The name of the form field to fill.
     * @param fieldValue The value to set in the form field.
     */
    private static void fillFormField(PDAcroForm acroForm, String fieldName, String fieldValue) {
        try {
            PDField field = acroForm.getField(fieldName);
            if (field != null) {
                field.setValue(fieldValue); // Set the field's value
            }
        } catch (IOException e) {
            showErrorDialog("Error filling form field: " + fieldName);
        }
    }

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The message to be displayed in the error dialog.
     */
    private static void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an information dialog with the specified message.
     *
     * @param message The message to be displayed in the information dialog.
     */
    private static void showInfoDialog(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
