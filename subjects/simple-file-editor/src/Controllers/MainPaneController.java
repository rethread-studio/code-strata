/*
 * The MIT License
 *
 * Copyright 2015 João Guitana.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package Controllers;

import Models.Context;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller of MainPane.fxml.
 * This is the controller for the view MainPane.fxml. This is a VBox with
 * a MenuBar, and AnchorPane and an HBox.
 * The Anchor Pane includes FileTabPane.fxml. See FileTabPane.
 * The HBox holds a few labels.
 * This class implements the methods for the MenuBar, while the FileTabController
 * implements the application logic.
 */
public class MainPaneController implements Initializable {
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    /*
    * @return the FileTabPaneController which controls the file tabs.
    */
    public FileTabPaneController getFileTabPaneController() {
        return fileTabPaneController;
    }
    
    /**
     * Removes the find dialog or replace dialog prompt, which ever is open.
     */
    public void removeFindOrReplaceDialog() {
        Node replaceDialog = vBox.lookup("#replaceDialogHBox");
        Node findDialog = vBox.lookup("#findDialogHBox");
        if (replaceDialog != null) {
            vBox.getChildren().remove(replaceDialog);
        }
        else if (findDialog != null) {
            vBox.getChildren().remove(findDialog);
        }
    }

    // FXML ACTIONS
    
    /**
     * Adds a new empty file tab.
     * @throws java.io.IOException
     */
    @FXML public void newFile() throws IOException  {
        fileTabPaneController.newFile();
    }
      
    /**
     * Opens a prompt to select and open a file tab.
     * @throws IOException 
     */
    @FXML public void openFile() throws IOException  {
        fileTabPaneController.openFile();
    }
    
    /**
     * Saves the contents of the text area to the file.
     * @throws IOException 
     */
    @FXML public void saveFile() throws IOException {
        fileTabPaneController.saveSelectedFile();
    }
    
    /**
     * Save the contents of the text area to a file chosen in a prompt.
     * @throws IOException 
     */
    @FXML public void saveAsFile() throws IOException {
        fileTabPaneController.saveAsSelectedFile();
    }
        
    /**
     * Prints the file with the default printer.
     */
    @FXML public void printFile()  {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.print();
        }
    }
          
    /**
     * Overrides the normal program quit to prompt for file saving.
     * @throws IOException 
     */
    @FXML public void quitApp() throws IOException {
        fileTabPaneController.closeTabsAndExit();
    }
    
    /**
     * Undo the most recent text change in the text area.
     */
    @FXML public void undo() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.undo();
        }
    }
        
    /**
     * Redo the most recent text change in the text area.
     */
    @FXML public void redo() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.redo();
        }
    }
     
    /**
     * Cuts the chosen text within the text area.
     */
    @FXML public void cut() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.cut();
        }
    }
    
    /**
     * Copies the chosen text within the text area.
     */
    @FXML public void copy() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.copy();
        }
    }
    
    /**
     * Pastes the curretly copied/cut text to the text area.
     */
    @FXML public void paste() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.paste();
        }
    }

    /**
     * Selects all the text in the text area.
     */
    @FXML public void selectAll() {
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            fileTabController.selectAll();
        }
    }
    
    /**
     * Opens a prompt in which a user can input text to find in the text area.
     * Will exclusively open this or a replace dialog.
     * @throws java.io.IOException
     */
    @FXML public void find() throws IOException {
        Node replaceDialog = vBox.lookup("#replaceDialogHBox");
        Node findDialog = vBox.lookup("#findDialogHBox");
        if (replaceDialog != null) {
            removeFindOrReplaceDialog();
            addFindDialog();
        }
        else if (findDialog == null) {
            addFindDialog();
        }
    }
    
    /**
     * Opens a prompt to find text in the text area and then replace it.
     * @throws java.io.IOException
     */
    @FXML public void replace() throws IOException {
        Node replaceDialog = vBox.lookup("#replaceDialogHBox");
        Node findDialog = vBox.lookup("#findDialogHBox");
        if (findDialog != null) {
            removeFindOrReplaceDialog();
            addReplaceDialog();
        }
        else if (replaceDialog == null) {
            addReplaceDialog();
        }
    }
    
    /**
     * 
     * @throws java.io.IOException
     */
    @FXML public void help() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/HelpDialog.fxml"));
        Parent node = loader.load();
        // Set the scene.
        Scene scene = new Scene(node);
        // Set the stage.
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Styling/iconsmall.png"));
        stage.show();
    }
 
    // PRIVATE METHODS
    
    /**
     * Opens a prompt in which a user can input text to find in the text area.
     * @throws IOException 
     */
    private void addFindDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FindDialog.fxml"));
        Parent node = loader.load();
        FindDialogController controller = loader.getController();
        controller.setContext(new Context(fileTabPaneController, this));
        int index = 2;
        vBox.getChildren().add(index, node);
    }
 
    /**
     * Opens a prompt in which a user can input text to find in the text area and
     * replace it.
     * @throws IOException 
     */
    private void addReplaceDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReplaceDialog.fxml"));
        Parent node = loader.load();
        ReplaceDialogController controller = loader.getController();
        Context context = new Context(fileTabPaneController, this);
        controller.getFindDialogController().setContext(context);
        controller.setContext(context);
        int index = 2;
        vBox.getChildren().add(index, node);
    }

    // FXML VARIABLES: DO NOT CHANGE:
    @FXML private VBox vBox;
    @FXML private FileTabPaneController fileTabPaneController;
    // END OF FXML VARIABLES
}