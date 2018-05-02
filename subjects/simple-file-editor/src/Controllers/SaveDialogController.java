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

import Models.SaveDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

/**
 * FXML Controller class of SaveDialog.fxml and SaveDialog.
 */
public class SaveDialogController implements Initializable {
    private SaveDialog saveDialog;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    // FXML ACTIONS
    
    /**
     * Saves the prompted text area to the file.
     * @param event to be handled.
     * @throws IOException 
     */
    @FXML private void saveFileByDialog(ActionEvent event) throws IOException {
        Tab tab = saveDialog.getTab();
        if (tab != null) {
            saveDialog.getFileTabPaneController().saveFile(tab);
            saveDialog.getStage().close();
        }
        
    }

    /**
     * Closes the prompted file tab.
     * @param event to be handled.
     * @throws IOException
     * @throws Exception 
     */
    @FXML
    private void closeFileByDialog(ActionEvent event) throws IOException, Exception {
        Tab tab = saveDialog.getTab();
        if (tab != null) {
            saveDialog.getFileTabPaneController().forceCloseFile(tab);
            saveDialog.getStage().close();
        }
    }

    /**
     * Closes the save dialog and won't do any action.
     * @param event to be handled.
     */
    @FXML private void closeSaveDialog(ActionEvent event) {
        saveDialog.getStage().close();
    }

    /**
     * Sets the save dialog model.
     * WARNING: This must be set for each new SaveDialog.fxml view to collect
     * the correct references for the button actions.
     * @param saveDialog 
     */
    public void setSaveDialog(SaveDialog saveDialog) {
        this.saveDialog = saveDialog;
    }

    /**
     * 
     * @return the save dialog model.
     */
    public SaveDialog getSaveDialog() {
        return saveDialog;
    }
}
