# Recipe-Vault

# Overall Architecture

The Recipe Vault application adopts a Model-View-Controller (MVC) architectural pattern, fostering a clear separation of concerns between data management, user interface presentation, and application logic.

## Model (Implicit)

The `Main` class serves as the central repository for the application's state. Instance variables within `Main`, such as `recipeNameField`, `recipeCategoryField`, `ingredientsList` (an `ObservableList`), `instructionsList` (an `ObservableList`), and `notesArea`, collectively represent the data model for the currently active recipe being created or edited by the user. These in-memory structures directly reflect the information manipulated through the user interface.

## View

The visual aspect of the application is entirely constructed using the JavaFX Scene Graph within the `start(Stage primaryStage)` method of the `Main` class and its associated helper methods (`createHeader()`, `createContentGrid()`, `createButtons()`). Each UI component, including `Label`, `TextField`, `ListView`, `Button`, and `ComboBox`, forms a node within this hierarchical structure. Layout panes like `BorderPane`, `GridPane`, `HBox`, and `VBox` are employed to organize these components into a user-friendly and visually coherent interface.

Styling using CSS-like syntax (`-fx-`) is applied directly within the code to define the application's visual theme (cream background, brown accents, Georgia font).

## Controller

User interactions within the view trigger specific events. These events are handled by event handlers attached to the interactive UI components. Examples include:

- **Data Input:**  
  Typing within `TextFields` automatically updates their corresponding text properties. This change is often bound to other UI elements, such as the character count `Labels`, providing immediate visual feedback driven by changes in the underlying model.

- **List Manipulation:**  
  Clicking the "Add" buttons for ingredients and instructions triggers the `addIngredient()` and `addInstruction()` methods within the `Main` class. These methods act as controllers, modifying the `ingredientsList` and `instructionsList` (the model). Due to the observable nature of these lists, the associated `ListViews` (the view) are automatically refreshed to reflect these changes.

  Similarly, the "Remove Selected" buttons invoke `removeIngredient()` and `removeInstruction()`, again acting as controllers to update the model and subsequently the view.

- **Form Management:**  
  The "Reset" button calls the `resetForm()` method, which clears all input fields and the observable lists, effectively resetting the application's model and the displayed view.

- **Data Persistence:**  
  The "Save" button initiates the crucial data persistence process by calling the `saveRecipeToPDF()` method within the `Main` class. This method acts as an orchestrator, retrieving the recipe data from the current model and delegating the task of generating the PDF file to the `RecipePDFWriter` class.

- **Application Lifecycle:**  
  The "Close" button triggers the `stage.close()` method, directly controlling the application's termination.

---

The `RecipePDFWriter` class functions as a specialized service or controller dedicated to the specific task of converting the in-memory recipe data into a persistent PDF format. It operates based on user-selected themes and utilizes the Apache PDFBox library to interact with pre-designed PDF templates.

# Implementation Details

The Recipe Vault application is implemented using Java and the JavaFX framework for the user interface, with the Apache PDFBox library integrated for PDF generation.

- **JavaFX Foundation:**  
  The `Main` class extends `javafx.application.Application`, serving as the entry point for the JavaFX application. The `start(Stage primaryStage)` method is the core of the UI initialization, responsible for creating the main application window (`Stage`), setting its properties (title, initial dimensions), and constructing the entire scene graph that defines the user interface.

- **Modular UI Construction:**  
  The UI is built in a modular fashion using helper methods (`createHeader()`, `createContentGrid()`, `createButtons()`) within the `Main` class. These methods encapsulate the instantiation, configuration, and layout of individual JavaFX UI components. Layout panes are strategically used to arrange these components into a user-friendly and responsive design. Visual styling is applied directly to the components using inline CSS-like syntax.

- **Reactive Data Handling:**  
  JavaFX's powerful property binding mechanism is employed to create dynamic relationships between UI elements and the underlying data model. For instance, the `textProperty()` of `TextFields` is bound to the `textProperty()` of the character count `Labels`, ensuring real-time updates as the user types. The `ObservableLists` for ingredients and instructions are fundamental for managing dynamic collections of data that are directly reflected in the `ListViews`. Any changes to these observable lists automatically trigger updates in the corresponding UI elements. Event listeners (`setOnAction`, `setOnKeyPressed`) are attached to interactive UI components to handle user-initiated actions.

- **User Input Management:**  
  Input from `TextFields` and the `TextArea` is directly accessed through their respective text properties. The `TextFormatter` class is utilized to enforce character limits on text-based input fields, ensuring data integrity and preventing overly long entries. The `setOnKeyPressed` event handler added to the ingredient and instruction input fields allows users to add new items by simply pressing the Enter key, streamlining the input process.

- **The Role of RecipePDFWriter (Apache PDFBox Integration):**  
  The `RecipePDFWriter` class encapsulates the logic for saving recipe data to a PDF file using pre-designed templates and the Apache PDFBox library.

  - The `saveRecipeToPDF()` method within `RecipePDFWriter` receives all the recipe details as parameters.

  - Based on the user-selected theme, it determines the appropriate PDF template file name (e.g., `"Summer_Template.pdf"`) and attempts to load it as an `InputStream` from the application's resources using `RecipePDFWriter.class.getResourceAsStream()`.

  - It then utilizes the Apache PDFBox library's core classes (`PDDocument`, `PDAcroForm`, `PDField`) to:

    - Load the PDF template from the InputStream.

    - Access the interactive form (`PDAcroForm`) within the loaded PDF document.

    - Retrieve specific form fields within the template by their defined names (e.g., `"Recipe"`, `"Author"`, `"Ingredient1"`, `"Direction1"`, `"Notes"`).

    - Set the values of these retrieved form fields using the recipe data passed to the method. For ingredients and instructions, it iterates through the respective lists and populates sequentially named form fields (e.g., `"Ingredient1"`, `"Ingredient2"`, and `"Direction1"`, `"Direction2"`).

  - To allow the user to choose the save location and filename, a `FileChooser` is presented.

  - Finally, the modified `PDDocument` (now containing the filled recipe data) is saved to the user-specified file.

- **Resource Handling:**  
  The application employs `getResourceAsStream()` to correctly access PDF template files that are bundled within the application's resources. This ensures that the application can locate and utilize these templates regardless of how it is deployed (e.g., as a standalone JAR file). The use of try-with-resources blocks when handling `PDDocument` and `InputStream` objects guarantees that these resources are properly closed after use, preventing potential resource leaks.

- **User Feedback via Alert Dialogs:**  
  The `showMessageDialog()`, `showErrorDialog()`, and `showInfoDialog()` methods within the `Main` class provide a consistent mechanism for displaying feedback to the user in the form of standard JavaFX `Alert` dialogs. These dialogs are used to communicate validation errors, informational messages (e.g., successful save), and other relevant application states.

---

# How it Works (End-to-End User Workflow)

1. **Application Launch:**  
   The user initiates the Recipe Vault application, leading to the execution of the `main()` method in the `Main` class, which in turn launches the JavaFX runtime and displays the primary application window defined in the `start()` method.

2. **Recipe Data Input:**  
   The user interacts with the various UI controls to input recipe details. This includes typing text into fields for metadata, ingredients, instructions, and notes, as well as selecting a theme from the `ComboBox`. Real-time feedback, such as character counters and updates to the ingredient and instruction `ListViews`, keeps the user informed of their input.

3. **Initiating Save:**  
   Once the user has entered all the desired recipe information, they click the "Save" button.

4. **Data Validation:**  
   Upon clicking "Save," the `validateFields()` method in the `Main` class is executed. This method checks for completeness in required fields and adherence to list size limits for ingredients and instructions. If any validation rules are violated, an error dialog is displayed to the user, and the saving process is interrupted.

5. **PDF Generation Orchestration:**  
   If the data validation is successful, the `saveRecipeToPDF()` method within the `Main` class is called. This method then delegates the actual PDF generation to the `RecipePDFWriter` class, passing the collected recipe data as arguments.

6. **Template Loading and Data Merging:**  
   The `RecipePDFWriter`'s `saveRecipeToPDF()` method determines the appropriate PDF template based on the selected theme and loads it from the application's resources. It then accesses the interactive form fields within the template using Apache PDFBox and populates these fields with the provided recipe data.

7. **Save File Selection:**  
   A `FileChooser` dialog is presented to the user, allowing them to specify the desired save location and filename for the generated PDF recipe file.

8. **PDF Saving:**  
   Once the user confirms the save location, the `RecipePDFWriter` saves the filled PDF document to the chosen file using Apache PDFBox.

9. **User Confirmation:**  
   A success message is displayed to the user via an information dialog, confirming that the recipe has been saved successfully.

10. **Optional Actions:**  
    The user can choose to "Reset" the form to clear all entered data and begin entering a new recipe or "Close" the application.

---

In summary, the Recipe Vault application provides a user-friendly JavaFX interface for recipe input. Upon the user's request to save, it leverages the `RecipePDFWriter` class and the Apache PDFBox library to seamlessly merge the entered recipe data with pre-designed, theme-specific PDF templates, resulting in a well-structured and visually appealing digital record of their culinary creations. The clear separation of UI logic (in `Main`) and PDF generation logic (in `RecipePDFWriter`) promotes modularity and maintainability within the project.

