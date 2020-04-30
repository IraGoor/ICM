package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppServer extends Application {

    public static EchoServer echoserver;

    public static void main(String[] args) {
        System.out.println("    <------- Server ------->");
        launch(args);
    }

    /**
     *
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/server/views/serverGUI.fxml"));
            root = loader.load();
            Scene s1 = new Scene(root);
            primaryStage.setScene(s1);
            primaryStage.show();
            Image image = new Image("/client/views/img/serverlogo.png");
            primaryStage.getIcons().add(image);
            primaryStage.setTitle("Server-ICM");
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("App Server : Start ERR");
        }
    }

}
