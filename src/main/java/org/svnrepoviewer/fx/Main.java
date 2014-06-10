package org.svnrepoviewer.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    private static int port;

    @Override
    public void start(Stage stage) {
        stage.setTitle("SVNRepoViewer");
        Scene scene = new Scene(new Browser(port), Browser.PREF_WIDTH, Browser.PREF_HEIGHT, Color.web("#666970"));
        stage.setScene(scene);
        stage.getIcons().add(new Image("java.png"));
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            throw new RuntimeException("missing port");
        }
        port = Integer.parseInt(args[0]);
        launch(args);
    }
}
