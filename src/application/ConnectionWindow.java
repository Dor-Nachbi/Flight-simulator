package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

public class ConnectionWindow {
    ViewModel vm;
    Stage s;
    @FXML
    TextField varIP, varPort;

    public void onConnect() {
        vm.connect();
    }
    public void setViewModel(ViewModel vm,Stage stage) {
        this.vm = vm;
        this.s = stage;
        vm.ip.bind(varIP.textProperty());
        vm.port.bind(varPort.textProperty());
    }

    public void close(){
        s.close();
    }
}
