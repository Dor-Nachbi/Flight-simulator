package application;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import org.omg.CORBA.Environment;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        MySimulatorModel m = new MySimulatorModel(); // Model
        ViewModel vm = new ViewModel(m); // View-Model
        m.addObserver(vm);
        FXMLLoader fxl = new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
        MainWindowController mwc = fxl.getController(); // View
        mwc.setViewModel(vm);
        vm.addObserver(mwc);

        primaryStage.setTitle("Simulator Controller");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                m.disconnect();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
