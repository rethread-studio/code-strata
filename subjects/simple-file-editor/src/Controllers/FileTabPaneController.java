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

import Models.FileTab;
import Models.FileTabPane;
import Models.SaveDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller of FileTabPane.fxml and FileTabPane.
 * This class is included under MainPane.fxml and has most application logic alongside
 * FileTabController. A FileTabPane is a view with a TabPane within an AnchorPane.
 * This controller manages FileTab. See FileTab.
 */
public class FileTabPaneController implements Initializable {
    private FileTabPane fileTabPane;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileTabPane = new FileTabPane(this);
        // Select the text area of the tab when a tab is selected.
        tabPane.getSelectionModel().selectedItemProperty().addListener(selectTextAreaOnTabSelection());
    }
    
    /**
     * Prompts the user to open a file.
     * @throws IOException 
     */
    public void openFile() throws IOException {
        File file = fileTabPane.getFileChooser().showOpenDialog(getWindow());
        if (file != null) {
            openUniqueFile(file);      
        }
    }
    
     /**
     * Closes the file if the tab saved state is true. Otherwise
     * it will prompot the user with a dialog.
     * @param tab to be removed.
     * @throws java.io.IOException
     */
    public void closeFile(Tab tab) throws IOException {
        if (tab != null) {
            FileTab fileTab = getFileTab(tab);
            if (fileTab != null) {
                // If the FileTab saved state is true, close the file.
                if (fileTab.getSavedState()) {
                    tabPane.getTabs().remove(tab);
                    fileTabPane.removeFileTab(fileTab);
                }
                // Else prompt the user if the file should be saved.
                else {
                    promptSaveDialog(tab);
                }
            }
        }
    }
    
    /**
     * Will close the tab without checking for the saved state.
     * @param tab to be removed.
     */
    public void forceCloseFile(Tab tab) {
        if (tab != null) {
            FileTab fileTab = getFileTab(tab);
            if (fileTab != null) {
                tabPane.getTabs().remove(tab);
                fileTabPane.removeFileTab(fileTab);
            }
        }
    }
    
    /**
     * Closes the file if the selected file tab saved state is true. Otherwise
     * it will prompot the user with a dialog.
     * @throws java.io.IOException
     */
    public void closeSelectedFile() throws IOException {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if(tab != null) {
            closeFile(tab);
        }
    }
    
    /**
     * Will save the contents of the Tab TextArea to the associated File
     * if it is valid.
     * @param tab which text is to be saved in the file.
     * @throws java.io.IOException
     */
    public void saveFile(Tab tab) throws IOException {
        if (tab != null) {
            FileTabController controller = getFileTabController(tab);
            if (controller != null) {
                controller.saveToFile();
            }
        }
    }
    
     /**
     * Will prompt the user to save the contents of the tab to the textarea.
     * @param tab which text is to be saved in the file.
     * @throws java.io.IOException
     */
    public void saveAsFile(Tab tab) throws IOException {
        if (tab != null) {
            FileTabController controller = getFileTabController(tab);
            if (controller != null) {
                controller.promptSaveToFile();
            }
        }
    }
    
    /**
     * Will save the contents of the selected tab TextArea to the associated File
     * if it is valid.
     * @throws java.io.IOException
     */
    public void saveSelectedFile() throws IOException {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab != null) {
            saveFile(tab);
        }
    }
    
    /**
     * Will prompt the user to save the contents of the selected tab TextArea 
     * to the chosen file.
     * @throws java.io.IOException
     */
    public void saveAsSelectedFile() throws IOException {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab != null) {
            saveAsFile(tab);
        }
    }
    
    /**
     * Will try to close all tabs. If the save state of any tab is false
     * the user will be prompted for a save.
     * @throws java.io.IOException
     */
    public void closeTabsAndExit() throws IOException {
        for (Tab tab : tabPane.getTabs()) {
            Boolean isTabSaved = getFileTabController(tab).getFileTab().getSavedState();
            if (!isTabSaved) {
                promptSaveDialog(tab);
                return;
            }
        }
            Platform.exit();
    }
    
    /**
     * 
     * @return the file tab controller.
     */
    public FileTabController getCurrentFileTabController() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab != null) {
            return getFileTabController(tab);
        }
        return null;
    }
    
    /*
    * @return the FileTabPane associated with this controller.
    */
    public FileTabPane getFileTabPane() {
        return fileTabPane;
    }
    
    /**
     * 
     * @return the window associated with the tab pane.
     */
    public Window getWindow() {
        return tabPane.getScene().getWindow();
    }
        
    // FXML ACTIONS
    
    /**
     * Adds a new empty file tab.
     * @throws java.io.IOException
     */
    @FXML public void newFile() throws IOException {
        newFile(new File("new file"));
    }
    
    // EVENT HANDLERS 
    
    /**
     * 
     * @return a change listener which will select the corresponding text area
     * of the selected tab. This means a user can switch tabs without losing
     * the text area selection.
     */
   private ChangeListener<Tab> selectTextAreaOnTabSelection() {
        return (new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue != null) {
                    Platform.runLater(() -> {
                        Tab tab = newValue;
                        FileTabController controller = getFileTabController(tab);
                        if (controller != null) {
                            controller.getTextArea().requestFocus();
                        }
                    });
                }   
            }
        });
    }
    
   // PRIVATE METHODS
   
    /**
     * Adds a new empty file tab and selects it.
     * @throws java.io.IOException
     */
    private void newFile(File file) throws IOException {
        // Loads a new tab from FXML.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/FileTab.fxml"));
        Tab tab = fxmlLoader.load();
        // Loads the controller.
        FileTabController fileTabController = fxmlLoader.getController();
        // Creates a new FileTab that is set on the controller.
        FileTab fileTab = new FileTab(file);
        fileTabController.setFileTab(fileTab);
        // Sets this on the FileTab controller.
        fileTabController.setFileTabPaneController(this);
        // Updates the view and the model.
        tabPane.getTabs().add(tab);
        fileTabPane.addFileTab(fileTab, fileTabController);
        // Selects the new tab.
        tabPane.getSelectionModel().select(tab);
    }
    
        
    /**
     * Opens a file if it is not already open.
     * @param file to be open.
     * @throws IOException 
     */
    private void openUniqueFile(File file) throws IOException {
        FileTabController fileTabController = getFileTabController(file);
        if (fileTabController == null) {
            newFile(file);
        }
    }
    
    /**
     * Prompts the user with a save dialog. This action is associated with
     * closing the file when the saved state is false.
     */
    private void promptSaveDialog(Tab tab) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/SaveDialog.fxml"));
        Parent node = fxmlLoader.load();
        SaveDialogController controller = fxmlLoader.getController();
        // Set the scene.
        Scene scene = new Scene(node);
        // Set the stage.
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Styling/iconsmall.png"));
        stage.show();
        // Set the SaveDialog model.
        SaveDialog saveDialog = new SaveDialog(stage, tab, this);
        controller.setSaveDialog(saveDialog);
    }
    
    /**
     * 
     * @param tab associated with the FileTab.
     * @return the FileTab associated with tab.
     */
    private FileTab getFileTab(Tab tab) {
        Iterator<FileTabController> iter = fileTabPane.getFileTabControllerMap().values().iterator();
        for (Iterator<FileTabController> i = iter; iter.hasNext();) {
            FileTabController controller = i.next();
            if (controller.getTab().equals(tab)) {
                return controller.getFileTab();
            }
        }
        return null;
    }
    
    /**
     * 
     * @param tab associated with the controller.
     * @return the FileTabController associated with tab.
     */
    private FileTabController getFileTabController(Tab tab) {
        Iterator<FileTabController> iter = fileTabPane.getFileTabControllerMap().values().iterator();
        for (Iterator<FileTabController> i = iter; iter.hasNext();) {
            FileTabController controller = i.next();
            if (controller.getTab().equals(tab)) {
                return controller;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param file associated with the controller.
     * @return the FileTabController associated with file.
     */
    private FileTabController getFileTabController(File file) {
        Iterator<FileTabController> iter = fileTabPane.getFileTabControllerMap().values().iterator();
        for (Iterator<FileTabController> i = iter; iter.hasNext();) {
            FileTabController controller = i.next();
            if (controller.getFileTab().getFile().equals(file)) {
                return controller;
            }
        }
        return null;
    }
 
    // FXML VARIABLES: DO NOT CHANGE
    @FXML private TabPane tabPane;
    // END OF FXML VARIABLES
}
