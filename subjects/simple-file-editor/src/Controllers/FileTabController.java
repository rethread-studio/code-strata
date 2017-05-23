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
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 * FXML Controller class for FileTab.fxml and FileTab.
 * A FileTab is a Tab with an editable TextArea. A File can be opened and edited
 * within the TextArea of the Tab. It can also be saved.
 */
public class FileTabController implements Initializable {
    private FileTab fileTab;
    private FileTabPaneController fileTabPaneController;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Add a listener to the text area to set unsaved state on input.
        textArea.textProperty().addListener(savedStateListener());
        // Overrides the tab closing with our closeTabIfSaved() method so we 
        // can listen for saved state.
        tab.setOnCloseRequest(removeSelectedTabIfSavedListener());
    }

     /**
     * 
     * @return the file tab pane controller.
     */
    public FileTabPaneController getFileTabPaneController() {
        return fileTabPaneController;
    }

    /**
     * Sets the file tab pane window controller on this file tab controller.
     * @param fileTabPaneController 
     */
    public void setFileTabPaneController(FileTabPaneController fileTabPaneController) {
        this.fileTabPaneController = fileTabPaneController;
    }
    
    /**
     * Will save the contents of the TextArea to the associated File
     * if it is valid. If it is not valid, a prompt will ask for 
     * file selection.
     * @throws java.io.IOException
     */
    public void saveToFile() throws IOException {
        File file = fileTab.getFile();
        if (file != null && file.isFile()) {
            writeToFile();
            updateFileTab();
        }
        else {
            promptSaveToFile();
        }
    }
    
    /**
     * Prompts the user to select a file to which we can write the textarea.
     * @throws java.io.IOException
     */
    public void promptSaveToFile() throws IOException {
        FileChooser fileChooser = fileTabPaneController.getFileTabPane().getFileChooser();
        File file = fileChooser.showSaveDialog(fileTabPaneController.getWindow());
        if (file != null) {
            fileTab.setFile(file);
            writeToFile();
            updateFileTab();
        }
    }
    
    /**
     * 
     * @return this controller file tab.
     */
    public FileTab getFileTab() {
        return fileTab;
    }
    
    /**
     * @param fileTab to be set as the controller file tab.
     * @throws java.io.IOException
     */
    public void setFileTab(FileTab fileTab) throws IOException {
        this.fileTab = fileTab;
        updateFileTab();
    }
    
    /**
     * Sets the file associated with the FileTab and updates its name in the 
     * tab.
     * @param file to be opened on the tab.
     * @throws java.io.IOException
     */
    public void setFile(File file) throws IOException {
        fileTab.setFile(file);
        updateFileTab();
    }
    
    /**
     * Undo the last change to the text area.
     */
    public void undo() {
        ((TextInputControlBehavior)((BehaviorSkinBase)textArea.getSkin()).getBehavior()).callAction("Undo");
    }
    
     /**
     * Redo the last change to the text area.
     */
    public void redo() {
        ((TextInputControlBehavior)((BehaviorSkinBase)textArea.getSkin()).getBehavior()).callAction("Redo");
    }
    
     /**
     * Cut the current selection in the text area.
     */
    public void cut() {
        textArea.cut();
    }
    
    /**
     * Copy the current selection in the text area.
     */
    public void copy() {
        textArea.copy();
    }
    
     /**
     * Paste the currently cut/copied text to the text area.
     */
    public void paste() {
        textArea.paste();
    }
    
    /**
     * Select all the text in the text area.
     */
    public void selectAll() {
        textArea.selectAll();
    }
    
    /**
     * Prints the text area on the default printer.
     */
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(textArea);
            if (success) {
            job.endJob();
            }
        }
    }

    /**
     * 
     * @return the Tab associated with this controller.
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * 
     * @return the TextArea associated with this controller.
     */
    public TextArea getTextArea() {
        return textArea;
    }
    
    // PRIVATE METHODS
    
    /**
     * Updates the text area to match the file text and the
     * tab text to match the file name.
     */
    private void updateFileTab() throws IOException {
        File file = fileTab.getFile();
        if (file.isFile()) {
            tab.setText(file.getName());
            textArea.setText(getFileOutputAsString(file));
            fileTab.setSavedState(true);
        }
    }
    
    /**
     * Saves the current text in the text area to the file and sets 
     * the current saved state to true.
     */
    private void writeToFile() throws IOException {
        File file = fileTab.getFile();
        if (file != null) {
            try(FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                Writer writer = new BufferedWriter(osw)) {
                    writer.write(textArea.getText());
                    // Set saved state to true.
                    fileTab.setSavedState(true);
            }
        }
    }
    
    /**
     * 
     * @param file to be returned as a String.
     * @return the File output as a String.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private String getFileOutputAsString(File file) throws FileNotFoundException, IOException {
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);) {
                StringBuilder sb = new StringBuilder();
                while (bis.available() > 0) {
                    sb.append((char) bis.read());
                }
                return sb.toString();
        }
    }
 
    // EVENT METHODS
    
    /**
     * 
     * @return an event handler used to override the normal tab closing
     * so we can prompt the user to save the file if the state is unsaved.
     */
    private EventHandler<Event> removeSelectedTabIfSavedListener() {
        return (final Event event) -> {
            try {
                Tab tab1 = (Tab) event.getSource();
                event.consume();
                fileTabPaneController.closeFile(tab1);
            }catch (Exception ex) {
                Logger.getLogger(FileTab.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }
    
    /**
     * @return a saved state listener which sets a file tab saved state
     * to false.
     */
    private ChangeListener<String> savedStateListener() {
        return (new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fileTab.setSavedState(false);
            }
        });
    }
    
    //FXML VARIABLES: DO NOT CHANGE.
    @FXML private Tab tab;
    @FXML private TextArea textArea;
    // END OF FXML VARIABLES
}