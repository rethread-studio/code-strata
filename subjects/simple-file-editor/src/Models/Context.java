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
import Controllers.MainPaneController;

/**
 * This class sets a global variable context for the main window and file tab pane
 * which can always be referenced while the application is running.
 * See MainPaneController or FileTabPaneController.
 */
public class Context {
    private final FileTabPaneController fileTabPaneController;
    private final MainPaneController mainPaneController;
    
    /**
     * Constructor for the class.
     * @param fileTabPaneController
     * @param mainPaneController 
     */
    public Context(FileTabPaneController fileTabPaneController, MainPaneController mainPaneController) {
        this.fileTabPaneController = fileTabPaneController;
        this.mainPaneController = mainPaneController;
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
     * @return the MainPaneController.
     */
    public MainPaneController getMainPaneController() {
        return mainPaneController;
    }
}
