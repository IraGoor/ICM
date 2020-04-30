package server;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import server.controllers.ServerController;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Schedule Tasks and handle it.
 */
public class ICM_Scheduler {

    public static void scheduler() {

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 9); // timer2 runs everyday in 9:00 AM
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);


        Timer timer2 = new Timer(); // Instantiate Timer Object
        timer2.scheduleAtFixedRate(new TimerTask() {
                                       @Override
                                       public void run() {
                                           Platform.runLater(() -> {
                                               EchoServer.NotifyDelayedStages();
                                               EchoServer.NotifyUncompletedStagesDayBeforeDeadline();
                                           });
                                       }
                                   }
                , today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));


        System.out.println("ICM_Scheduler - Started");


    }


    static class checkConnectedUsers_onTimer extends TimerTask {
        @Override
        public synchronized void run() {
            System.out.println(EchoServer.connectedUsers);
            Platform.runLater(() -> {
                checkConnectedUsers();
            });
        }
    } // checkConnectedUsers class

    public static void checkConnectedUsers(){
        if (EchoServer.connectedUsers == null) return;
        try {
            EchoServer.connectedUsers.forEach((k, v) -> {
                if (!v.isAlive()) {
                    System.out.println("remove " + k);
                    if(EchoServer.connectedUsers.containsKey(k))
                        EchoServer.connectedUsers.remove(k);
                }
            });
            updateUserList();
        } catch (Exception e) {
            ServerController.instance.startServer();
        }
    }

    public static void updateUserList(){

        ServerController.instance.usersList.setItems(FXCollections.observableArrayList(EchoServer.connectedUsers.keySet()));
    }
}//ICM_Scheduler

