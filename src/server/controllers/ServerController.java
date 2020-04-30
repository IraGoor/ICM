package server.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import server.AppServer;
import server.EchoServer;
import server.ICM_Scheduler;
import server.mysqlConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  GUI Controller - <code>serverGUI.fxml</code>
 *  let the server user ability to control the server and database connections, see logs, connected users etc.
 */
public class ServerController implements Initializable {

    public static ServerController instance;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textBox;

    @FXML
    private RadioButton radio_mysqlWorkbench;

    @FXML
    private RadioButton radio_remoteSQL;

    @FXML
    private Button onBtn;

    @FXML
    private Button offBtn;

    @FXML
    private Circle server_light;

    @FXML
    private Circle db_light;

    @FXML
    private TextField host_field;

    @FXML
    private TextField scheme_field;

    @FXML
    private TextField password_field;

    @FXML
    private TextField username_field;

    @FXML
    private TextField port_field;

    @FXML
    private ToggleGroup radio_group;

    @FXML
    private TextField server_port_field;

    @FXML
    public ListView<String> usersList;

    @FXML
    private Button btnRunTimersManually;

    @FXML
    void runTimersManually(ActionEvent event) {
        //EchoServer.NotifyDelayedStages();
        EchoServer.NotifyUncompletedStagesDayBeforeDeadline();
    }
    private DBDetails MySQLWorkbench = new DBDetails("localhost", "icm?serverTimezone=IST", "root", "Aa123456", "5555");
    private DBDetails RemoteSQL = new DBDetails("remotemysql.com", "yRBHdnFuc9", "yRBHdnFuc9", "QOMMWb8Jo6", "5555");

    private DBDetails currentDB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        btnRunTimersManually.setDisable(true);

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));
        printFormFields(RemoteSQL);

    }

    private void appendText(String str) {
        Platform.runLater(() -> textBox.appendText(str));
    }

    /**
     * fills in default database connection.
     * @param dbDetails
     */
    private void printFormFields(DBDetails dbDetails) {
        host_field.setText(dbDetails.getDB_HOST());
        scheme_field.setText(dbDetails.getDB_SCHEME());
        password_field.setText(dbDetails.getDB_PASSWORD());
        username_field.setText(dbDetails.getDB_USERNAME());
        port_field.setText(dbDetails.getDB_PORT());
    }


    private void setDBDetailsFromGUI() {
        DBDetails dbDetails = null;
        if (radio_remoteSQL.isSelected())
            dbDetails = RemoteSQL;
        else if (radio_mysqlWorkbench.isSelected())
            dbDetails = MySQLWorkbench;
        dbDetails.setDB_HOST(host_field.getText());
        dbDetails.setDB_PASSWORD(password_field.getText());
        dbDetails.setDB_PORT(port_field.getText());
        dbDetails.setDB_SCHEME(scheme_field.getText());
        dbDetails.setDB_USERNAME(username_field.getText());
    }

    public void startDBService() {
        if (currentDB != null)
            mysqlConnection.openConnection(currentDB);

    }

    /**
     * Use OCSF in order to create a new server connection, and a new Database connection.
     */
    @FXML
    public void startServer() {
        // EchoServer.mainServer(args);
        setDBDetailsFromGUI();
        int port = 0; // Port to listen on

        try {
            port = Integer.parseInt(server_port_field.getText()); // Get port from command line
        } catch (Throwable t) {
            port = EchoServer.DEFAULT_PORT; // Set port to 5555
        }


        EchoServer sv = new EchoServer(port);
        AppServer.echoserver = sv;
        EchoServer.portNumber = Integer.parseInt(server_port_field.getText());
        if (radio_remoteSQL.isSelected())
            currentDB = RemoteSQL;
        else if (radio_mysqlWorkbench.isSelected())
            currentDB = MySQLWorkbench;

        // create new Database connection.
        startDBService();

        // create new server connection
        if (!AppServer.echoserver.isListening()) {
            try {
                AppServer.echoserver.listen(); // Start listening for
                // connections
                if (AppServer.echoserver.isListening())
                    server_light.setFill(Paint.valueOf("#1ffb1b"));
                else
                    server_light.setFill(Paint.valueOf("#ff1717"));

            } catch (IOException e) {
                server_light.setFill(Paint.valueOf("#ff1717"));
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Failed to Start Server");
                alert.setHeaderText("error");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Server already Connected");
            alert.setHeaderText("Server already Connected"); // ADD MORE
            // INFORAMTION
            alert.show();
        }

        // update Server GUI component
        if (AppServer.echoserver.getDBStatus())
            db_light.setFill(Paint.valueOf("#1ffb1b"));
        else
            db_light.setFill(Paint.valueOf("#ff1717"));

        // call to ICM Scheduler.
        ICM_Scheduler.scheduler();
        btnRunTimersManually.setDisable(false);

    }

    /**
     * stop server listening.
     */
    @FXML
    void stopServer() {
        btnRunTimersManually.setDisable(true);

        // EchoServer.mainServer(args);

        if (AppServer.echoserver.isListening()) {
            AppServer.echoserver.stopListening();
            ; // Start listening for
            // connections
            if (!AppServer.echoserver.isListening())
                server_light.setFill(Paint.valueOf("#1ffb1b"));
            else
                server_light.setFill(Paint.valueOf("#ff1717"));

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Server already Disconnected");
            alert.setHeaderText("Server already Disconnected"); // ADD MORE
            // INFORAMTION
            alert.show();
        }

        if (AppServer.echoserver.getDBStatus())
            db_light.setFill(Paint.valueOf("#1ffb1b"));
        else
            db_light.setFill(Paint.valueOf("#ff1717"));
    }

    /**
     * call to <code>printFormFields</code> function according to the selected database connection.
     * @param e
     */
    @FXML
    void setFormFields(ActionEvent e) {
        DBDetails dbDetails = null;
        if (radio_remoteSQL.isSelected())
            dbDetails = RemoteSQL;
        else if (radio_mysqlWorkbench.isSelected())
            dbDetails = MySQLWorkbench;
        printFormFields(dbDetails);
    }

}
