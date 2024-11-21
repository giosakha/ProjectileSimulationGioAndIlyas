/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projectilesimulationfile;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author giorg
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        SimulationController controller = new SimulationController(mainPane);
        Scene scene = new Scene(mainPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Projectile Motion Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
