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

package Main;

import Controllers.MainPaneController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * A simple tabbed text editor.
 * This is the application entry point. The application follow a MVC logic.
 * Initially, a scene is set up on MainPane.fxml. See MainPaneController.
 * @author jguitana
 */
public class SimpleTextEditor extends Application {
    private MainPaneController mainPaneController;
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/MainPane.fxml"));
        Parent root = fxmlLoader.load();
        mainPaneController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        
        String appName = "Simple Text Editor 1.0";
        stage.setOnCloseRequest(onCloseEvent());
        stage.setTitle(appName);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/Styling/iconsmall.png"));
        stage.show();
    }
    
    // EVENT HANDLERS
    
    /**
     * 
     * @return an event handler used to override normal closing of the application
     * so that we can prompt save file tabs.
     */
    private EventHandler<WindowEvent> onCloseEvent() {
        return (WindowEvent event) -> {
            try {
                closeAllTabs();
            } catch (IOException ex) {
                Logger.getLogger(SimpleTextEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            event.consume();
        };
    }
    
    /**
     * This helper method is called by onCloseEvent() event handler.
     * @throws IOException 
     */
    private void closeAllTabs() throws IOException {
        mainPaneController.getFileTabPaneController().closeTabsAndExit();
    }
    
    // MAIN
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
