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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class of FindDialog.fxml and FindDialog.
 */
public class FindDialogController implements Initializable {
    private Context context;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Listen for changes to the text field and automatically finds the
        // input text.
        textField.textProperty().addListener(findChangeListener());
    }
    
    /**
     * Sets a context for this class.
     * @param context to be associated with the find dialog.
     */
    public void setContext(Context context) {
        this.context = context;
    }
    
    /**
     * 
     * @return the Context for this class.
     */
    public Context getContext() {
        return context;
    }
    
    // FXML ACTIONS
        
    /**
     * Closes the find dialog.
     */
    @FXML public void close() {
        context.getMainPaneController().removeFindOrReplaceDialog();
    }
    
    /**
     * Calls find on any previous existing text in the text area
     * from the selected text.
     */
    @FXML public void previous() {
        TextArea textArea = getSelectedTextArea();
        if (textArea != null) {
            int caretPosition = textArea.getCaretPosition()  - textArea.getSelectedText().length() - 1;
            boolean findNext = false;
            find(textArea, caretPosition, findNext);
        }
    }
    
    /**
     * Calls find on any next existing text in the text area
     * from the selected text.
     */
    @FXML public void next() {
        TextArea textArea = getSelectedTextArea();
        if (textArea != null) {
            int caretPosition = textArea.getCaretPosition();
            boolean findNext = true;
            find(textArea, caretPosition, findNext);
        }
    }

    // EVENT HANDLERS
    
    /**
     * 
     * @return a listener which calls find on the find dialog text field change.
     */
    private ChangeListener<String> findChangeListener() {
        return (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            unsetNoTextFound();
            String textToFind = newValue;
            if (!textToFind.isEmpty()) {
                find();
            }
        };
    }
    
    // PRIVATE METHODS
    
    /**
     * Searches the text area for the current text at the find dialog text field.
     */
    private void find() {
        TextArea textArea = getSelectedTextArea();
        if (textArea != null) {
            int caretPosition = textArea.getCaretPosition() - textArea.getSelectedText().length();
            boolean findNext = true;
            find(textArea, caretPosition, findNext);
        }
    }
    
    /**
     * A helper method for find().
     * @param textArea
     * @param caretPosition
     * @param findNext 
     */
    private void find(TextArea textArea, int caretPosition, boolean findNext) {
        int firstOcurrence;
        if (findNext) { firstOcurrence = getFirstOcurrenceIndex(textArea, caretPosition); }
        else { firstOcurrence = getLastOcurrenceIndex(textArea, caretPosition); }
        if (firstOcurrence != -1) {
            unsetNoTextFound();
            updateTextArea(textArea, firstOcurrence);
        }
        else {
            noTextFound(textArea);
        }
    }
    
    /**
     * 
     * @param textArea to be searched.
     * @param caretPosition from where to search.
     * @return the index of the first occurence of the text field in the text area
     * searching backwards from the caret position.
     */
    private int getLastOcurrenceIndex(TextArea textArea, int caretPosition) {
        // If the caret is in the last position look backwards.
        if (caretPosition == textArea.getText().length()) {
            return findLastOccurence(textArea, caretPosition);
        }
        // If the caret is not in the last position look backwards and if unsucessfull forward.
        else {
            int firstOcurrence = findLastOccurence(textArea, caretPosition);
            // if it is still null find from the start.
            if (firstOcurrence == -1) { return findLastOccurence(textArea, textArea.getText().length()); }
            else { return firstOcurrence; }
        }
    }
    
    /**
     * 
     * @param textArea to be searched.
     * @param caretPosition from where to search.
     * @return the index of the first occurence of the text field in the text area
     * searching forward from the caret position.
     */
    private int getFirstOcurrenceIndex(TextArea textArea, int caretPosition) {
        // If the caret is in the first position look all the way.
        if (caretPosition == 0) {
            return findFirstOccurence(textArea, caretPosition);
        }
        // If the caret is not in the first position look forward and if unsucessful backwards.
        else {
            int firstOcurrence = findFirstOccurence(textArea, caretPosition);
            // if it is still null find from the start.
            if (firstOcurrence == -1) { return findFirstOccurence(textArea, 0); }
            else { return firstOcurrence; }
        }
    }
    
    /**
     * Sets up a message when no text is found.
     * @param textArea 
     */
    private void noTextFound(TextArea textArea) {
        textArea.selectRange(0, 0);
        messageLabel.setText("No Text Found.");
    }
    
    /**
     * Unsets the message.
     */
    private void unsetNoTextFound() {
        messageLabel.setText("");
    }

    /**
     * Update the text area when text if found.
     * @param textArea to be udpated.
     * @param textFound is the text found.
     */
    private void updateTextArea(TextArea textArea, int textFound) {
        int selectedTextLength = textField.getText().length();
        textArea.selectRange(textFound, textFound + selectedTextLength);
    }
    
    /**
     * A helper for getLastOccurenceIndex().
     * @param textArea of the occurence to be found.
     * @param startIndex of the text area in which to find.
     * @return the first occurence of the text to be find searching backwards.
     */
    private int findLastOccurence(TextArea textArea, int startIndex) {
        String textToFind = textField.getText();
        String textToLook = textArea.getText();
        return textToLook.lastIndexOf(textToFind, startIndex);
    }
    
    /**
     * A helper for getFirsttOccurenceIndex().
     * @param textArea of the occurence to be found.
     * @param startIndex of the text area in which to find.
     * @return the first occurence of the text to be find searching forward.
     */
    private int findFirstOccurence(TextArea textArea, int startIndex) {
        String textToFind = textField.getText();
        String textToLook = textArea.getText();
        return textToLook.indexOf(textToFind, startIndex);
    }
    
    /**
     * 
     * @return the currently selected text area of the tab pane.
     */
    private TextArea getSelectedTextArea() {
        FileTabPaneController fileTabPaneController = context.getFileTabPaneController();
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            return fileTabController.getTextArea();
        }
        return null;
    }
        
    // FXML VARIABLES: DO NOT CHANGE
    @FXML private HBox findDialogHBox;
    @FXML private TextField textField;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button closeButton;
    @FXML private Label messageLabel;
    // END OF FXML VARIABLES
}
