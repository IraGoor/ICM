package client;

import java.io.IOException;
//
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//

/**
 * Start the first screen of the app.
 *
 */
public class App extends Application {

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;
    public static Client client;
    public static User user;
    public static String server_ip = "";
    public static String server_port = "";
    public static boolean appInitialized = false;
    public static Stage stage;

    /**
     * main method
     */
    public static void main(String[] args) {
        try {
            launch(args);
        }
        catch (Exception e){
            System.out.println("Global Exception. check trace");
        }
    }

    public static boolean startClient() {
        String host = "";
        int port = 0; // The port number
        if (server_port == "")
            port = DEFAULT_PORT;
        else
            port = Integer.parseInt(server_port);

        host = server_ip;


        try {
            client = new Client(host, port);
            System.out.println("Client setup connection! " + host + ":" + port);
            return true;
        } catch (IOException exception) {
            System.out.println("Error: Can't setup connection!" + " Terminating client." + exception);
            System.exit(1);
            return false;
        }

    }

    /**
     *
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/views/clientConnection.fxml"));
            root = loader.load();
            Scene s1 = new Scene(root);
            primaryStage.setScene(s1);
            primaryStage.show();
            primaryStage.setResizable(false);
            Image image = new Image("/client/views/img/logoicon.png");
            primaryStage.getIcons().add(image);
            primaryStage.setTitle("ICM");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERR at App.Start");
        }
    }

    public static void ForceAuthorizeAllUsers() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                App.client.handleMessageFromClientUI(new Message(OperationType.ForceUpdateUsersPermissions, null));
            }
        });
    }

}
