package client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * 
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
/**
 * 
 * <h2>Connection to  Server Controller</h2>
 * <p>
 * Responsible of the connection feedback for the client to the sever and the database
 * </p>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */

public class clientConnectionDialog {
    @FXML
    private AnchorPane ap;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField ipAddress;

    @FXML
    private Button btnConnect;

    static Stage window;

    @FXML
    private TextField port;

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>let user input IP and port addresses</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event : mouse pressed on connect button
     */
    @FXML
    void setServerIP(MouseEvent event) {
        App.server_ip = ipAddress.getText();
        App.server_port = port.getText();

        boolean res = App.startClient();
        changeWindow((Stage) ((Node) event.getSource()).getScene().getWindow(), "/client/views/Login.fxml");

    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Change window to login window</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param window (Window) app window
     *@param fxml	(String) page path
     */
    public static void changeWindow(Window window, String fxml) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                window.hide();
                try {
                    Pane root = FXMLLoader.load(getClass().getResource(fxml));
                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @FXML
    void initialize() {
        assert ipAddress != null : "fx:id=\"ipAddress\" was not injected: check your FXML file 'clientConnection.fxml'.";
        assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'clientConnection.fxml'.";

    }
}
