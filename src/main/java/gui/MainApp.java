package gui;

import gui.nav.DrillDownView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import orm.ORM;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        DrillDownView nav = new DrillDownView(AcademicNav.root());
        nav.onLeafSelected(row -> System.out.println("Selected: " + row.label()));

        Scene scene = new Scene(nav, 720, 480);
        stage.setTitle("TA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        // args (containing the model names) must always be forwared
        ORM.initializeInstance(args);
        launch();
    }
}
