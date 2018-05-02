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

package Models;

import Controllers.FileTabPaneController;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

/**
 * A model used to store data associated with SaveDialog.fxml and SaveDialogController.
 */
public class SaveDialog {
    private final Stage stage;
    private final Tab tab;
    private final FileTabPaneController fileTabPaneController;
    
    /**
     * Constructs a new save dialog.
     * @param stage to be associated with the save dialog.
     * @param tab to be associated with the save dialog.
     * @param fileTabPaneController to be associated with the save dialog.
     */
    public SaveDialog(Stage stage, Tab tab, FileTabPaneController fileTabPaneController) {
        this.fileTabPaneController = fileTabPaneController;
        this.stage = stage;
        this.tab = tab;
    }

    /**
     * 
     * @return the FileTabPaneController.
     */
    public FileTabPaneController getFileTabPaneController() {
        return fileTabPaneController;
    }
    
    /**
     * 
     * @return the stage for the save dialog.
     */
    public Stage getStage() { 
        return stage; 
    }
    
    /**
     * 
     * @return the tab which called the save dialog.
     */
    public Tab getTab() {
        return tab;
    }
}
