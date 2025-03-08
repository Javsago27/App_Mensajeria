package chats.vistas;

import chats.App;
import chats.controladores.Controlador_ChatPublico;
import chats.controladores.Controlador_Login;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Vista_Login extends Vista{

    private Controlador_Login controlador;
    protected final String ficheroInterfaz = "login.fxml";
    private Scene escena;

    @FXML
    private AnchorPane root; // Nodo raíz del archivo FXML
    @FXML
    private TextField nombreUsuario;

    public Vista_Login(Controlador_Login controlador) throws IOException{
        super();
        this.controlador = controlador;
        escena = new Scene(cargarInterfaz(ficheroInterfaz, this));
    }

    public void iniciarSesion(){
        if(!nombreUsuario.getText().isEmpty()){
            controlador.iniciarSesion(nombreUsuario.getText());
        }
    }

    public Scene getEscena() {
        return escena;
    }
    
}
