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

import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A model used to store data associated with FileTab.fxml and FileTabController.
 * @author jguitana
 */
public class FileTab {
    private final ObjectProperty<File> file;
    private final SimpleBooleanProperty savedState;

    /**
     * Constructs a new FileTab.
     * @param file to be associated with the file tab.
     */
    public FileTab(File file) {
        this.savedState = new SimpleBooleanProperty(true);
        this.file = new SimpleObjectProperty<>(file);
    }

    /**
     * 
     * @return the saved state of this file tab. A saved state returns false
     * if any changes were made in the editor, and where not written to the
     * file.
     */
    public boolean getSavedState() {
        return savedState.get();
    }
    
    /**
     * Set the saved state of this file tab.
     * @param value 
     */
    public void setSavedState(boolean value) {
        savedState.set(value);
    }
    
    /**
     * 
     * @return the saved state property.
     */
    public SimpleBooleanProperty savedStateProperty() {
        return savedState;
    }
    
    /**
     * Set the file to be associated with this file tab.
     * @param value to be set as file.
     */
    public void setFile(File value) { 
        file.set(value); 
    }
    
    /**
     * 
     * @return the file property.
     */
    public ObjectProperty<File> fileProperty() { 
        return file; 
    }
    
    /**
     * 
     * @return the current file.
     */
    public File getFile() { 
        return file.get(); 
    }
}
