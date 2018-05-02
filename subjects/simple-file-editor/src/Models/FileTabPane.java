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
import Controllers.FileTabController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.FileChooser;

/**
 * A model used to store data associated with the view FileTabPane.fxml
 * and FileTabPaneController.
 */
public class FileTabPane {
    private final Map<FileTab, FileTabController> fileTabControllerMap;
    private final FileChooser fileChooser;

    /**
    * Constructs a new FileTabPane.
    * @param controller of the file tab pane.
    */
    public FileTabPane(FileTabPaneController controller) {
        this.fileTabControllerMap = new HashMap<>();
        this.fileChooser = new FileChooser();
        // Set extension filter on the file chooser.
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    }
    
    /**
     * 
     * @return a FileChooser with txt extensions.
     */
    public FileChooser getFileChooser() {
        return fileChooser;
    }
    
    /**
     * Adds a file tab to the hash map.
     * @param fileTab to be added to the map.
     * @param fileTabController to be added to the map.
     */
    public void addFileTab(FileTab fileTab, FileTabController fileTabController) {
        fileTabControllerMap.put(fileTab, fileTabController);
    }
    
    /**
     * Removes a file tab from the hash map.
     * @param fileTab to be removed from the map.
     */
    public void removeFileTab(FileTab fileTab) {
        fileTabControllerMap.remove(fileTab);
    }
    
    /**
     *
     * @return the file tab controller map used to associate file tabs with
     * their respective controller.
     */
    public Map<FileTab, FileTabController> getFileTabControllerMap() {
        return Collections.unmodifiableMap(fileTabControllerMap);
    }
}
