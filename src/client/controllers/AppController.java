package client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h2>General Application Controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class AppController {

	
    protected Alert alert = new Alert(null);

    /**
     * 
     * <h3>Method Purpose:</h3> 
     * <p>Show an alert when needed, run in a separate thread as a background mechanism to improve runtime.</p> 
     * @param at the alert type(CONFIRMATION,ERROR,INFORMATION,NONE AND WARNING)  
     * @param title  the sting to shpw as the title of dialog box
     * @param content the string to show in the dialog content area. 
     * @param header the string to show in the dialog header area. 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.AlertType.html
     */
    protected void showAlert(AlertType at, String title, String content, String header) {
        Platform.runLater(() -> {
            alert.setAlertType(at);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.setHeaderText(header);
            alert.show();
        });
    }

    /**
     * <h3>Method Purpose:</h3> 
     * <p>Close old window, open new window with app main format in a new thread while main thread usually communicates with server.</p>  
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     * @param window  Current window
     * @param fxml  Full path to the window we switch to 
     */
    protected static void changeWindow(Window window, String fxml) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                window.hide();
                try {
                    Pane root = FXMLLoader.load(getClass().getResource(fxml));
                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.setResizable(false);
                    Image image = new Image("/client/views/img/logoicon.png");
                    primaryStage.getIcons().add(image);
                    primaryStage.setTitle("ICM");
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    } // changeWindow

    /**
     * <h3>Method Purpose:</h3> 
     * Get system current time
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     * @return : String the time and  date zone
     */
    protected String getToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String date_time;
        date_time = formatter.format(date);
        return date_time;
    }

    /**
     * <h3>Method Purpose:</h3> 
     * <p><ul>
     * <li>Load center page in app.</li>
     * <li>Load FXML page file as a child of main window.</li> 
     * <li>Loaded page resolution: 1000*710</li></ul></p>
     *@author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     * @param fxmlName : String (Without file type name)
     */
    protected void loadPage(String fxmlName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/client/views/" + fxmlName + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.instance.getBorderPane().setCenter(root);
    }



    /**
     * <h3>Method Purpose:</h3> 
     * <p>Load inner page inside main layout</p> 
     * 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     * 
     *
     * @param fxmlname
     * @param pageTitle
     */
    protected void loadPage(String fxmlname, String pageTitle) {
        mainController.instance.setTitle(pageTitle);
        loadPage(fxmlname);
    }

}
