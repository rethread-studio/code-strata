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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class for ReplaceDialog.fxml.
 */
public class ReplaceDialogController implements Initializable {
    private Context context;

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
     * Replaces the selected text in the text area with the input of the 
     * text field.
     */
    @FXML public void replace() {
        TextArea textArea = getSelectedTextArea();
        if (textArea != null) {
            int anchor = textArea.getAnchor();
            int caret = textArea.getCaretPosition();
            if (anchor != caret) {
                replace(textArea, anchor, caret);
            }
        }
    }
    
    /**
     * Replaces all occurences of text equal to the text input on the text field 
     * in the text area.
     */
    @FXML public void replaceAll() {
        TextArea textArea = getSelectedTextArea();
        if (textArea != null) {
            String textToReplace = textArea.getSelectedText();
            if (!textToReplace.isEmpty()) {
                replaceAll(textArea, textToReplace);
            }
        }
    }
    
    // PRIVATE METHODS
    
    /**
     * A helper method which replaces the selected text in the text area with 
     * the input of the text field.
     * @param textArea in which to replace the text.
     * @param anchor of the selected text.
     * @param caret of the selected text.
     */
    private void replace(TextArea textArea, int  anchor, int caret) {
        String textToReplaceWith = replaceTextField.getText();     
        String firstHalf = textArea.getText(0, anchor);
        String lastHalf = textArea.getText(caret, textArea.getText().length());
        String replacedText = firstHalf + textToReplaceWith + lastHalf;
        updateTextArea(textArea, replacedText, textToReplaceWith);
    }
    
    /**
     * A helper method which replaced all occurences of text equal to the text 
     * input on the text field in the text area.
     * @param textArea in which to replace the text.
     * @param textToReplace in the text area.
     */
    private void replaceAll(TextArea textArea, String textToReplace) {
        String textToReplaceWith = replaceTextField.getText();
        String replacedText = textArea.getText().replaceAll(textToReplace, textToReplaceWith);
        updateTextArea(textArea, replacedText, textToReplaceWith);
    }
    
    /**
     * A helper method for replacing the text area with the replaced text.
     * @param textArea in which to replace the text.
     * @param replacedText which is the text with the replaced text.
     * @param textToReplaceWith which is the text to replace with in the text area.
     */
    private void updateTextArea(TextArea textArea, String replacedText, String textToReplaceWith) {    
        int anchor = replacedText.indexOf(textToReplaceWith);
        int caret = replacedText.indexOf(textToReplaceWith) + textToReplaceWith.length();
        textArea.setText(replacedText);
        textArea.selectRange(anchor, caret);
    }
    
    /**
     * A helper method to get the selected text area in the tab pane.
     * @return the currently selected text area.
     */
    private TextArea getSelectedTextArea() {
        FileTabPaneController fileTabPaneController = context.getFileTabPaneController();
        FileTabController fileTabController = fileTabPaneController.getCurrentFileTabController();
        if (fileTabController != null) {
            return fileTabController.getTextArea();
        }
        return null;
    }
    
    /**
     * Sets the context for the replace dialog.
     * WARNING: This must be set for each new ReplaceDialog.fxml view to collect
     * the correct references for the button actions.
     * @param context to be associated with the replace dialog.
     */
    public void setContext(Context context) {
        this.context = context;
    }
    
    /**
     * 
     * @return the application Context.
     */
    public Context getContext() {
        return context;
    }
    
    /**
     * 
     * @return the FileDialogController associated with the replace dialog.
     */
    public FindDialogController getFindDialogController() {
        return findDialogController;
    }

    // FXML VARIABLES: DO NOT CHANGE
    @FXML private FindDialogController findDialogController;
    @FXML private HBox replaceDialogHBox;
    @FXML private Button closeButton;
    @FXML private TextField replaceTextField;
    @FXML private Button replaceButton;
    @FXML private Button replaceAllButton;
    // END OF FXML VARIABLES
}
