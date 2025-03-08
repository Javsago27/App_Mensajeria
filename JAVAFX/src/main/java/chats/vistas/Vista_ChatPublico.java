package chats.vistas;

import java.io.IOException;

import chats.App;
import chats.modelo.Modelo;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import chats.controladores.Controlador_ChatPublico;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Vista_ChatPublico extends Vista{

    private Controlador_ChatPublico controlador;
    protected final String ficheroInterfaz = "chatPublico.fxml";
    private Scene escena;
    private String usuario;

    @FXML
    private AnchorPane root; // Nodo raíz del archivo FXML
    @FXML
    private TextField cuadroMensaje;
    @FXML
    private AnchorPane vistaMensajes;

    public void initialize() {
        // Escucha los eventos de teclado cuando el nodo raíz tiene el foco
        root.setOnKeyPressed(this::detectarTeclas);
    }

    public Vista_ChatPublico(Controlador_ChatPublico controlador) throws IOException{
        super();
        this.controlador = controlador;
        escena = new Scene(cargarInterfaz(ficheroInterfaz, this));
    }

    private void detectarTeclas(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {enviarMensaje();} catch (IOException e) {throw new RuntimeException(e);}
        }
    }

    public void pintarMensajeRecibido(String mensaje, String usuarioRecibido){
        Label usuarioMensaje = new Label();
        Label texto = new Label();
        //textField.setDisable(true);
        usuarioMensaje.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: #DCDCDC; -fx-opacity: 1; -fx-control-inner-background: lightgreen;");
        texto.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: red; -fx-opacity: 1; -fx-control-inner-background: lightgreen;");

        usuarioMensaje.setText(usuarioRecibido);
        texto.setText(mensaje);
        usuarioMensaje.setAlignment(Pos.CENTER);
        texto.setAlignment(Pos.CENTER);

        usuarioMensaje.setMaxWidth(70);
        texto.setMaxWidth(350);

        // Posicionar el TextField dinámicamente
        double yPosition = vistaMensajes.getChildren().size() * 40.0 + 20.0; // Espaciado entre TextFields
        AnchorPane.setTopAnchor(usuarioMensaje, yPosition);
        AnchorPane.setTopAnchor(texto, yPosition);
        AnchorPane.setLeftAnchor(usuarioMensaje, 10.0);
        AnchorPane.setLeftAnchor(texto, 10.0);

        // Añadir el TextField al AnchorPane
        vistaMensajes.getChildren().add(usuarioMensaje);
        vistaMensajes.getChildren().add(texto);
    }

    @FXML
    private void enviarMensaje() throws IOException {
        if(!cuadroMensaje.getText().isEmpty()){
            Label label = new Label();
            //textField.setDisable(true);
            label.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: lightgreen; -fx-opacity: 1; -fx-control-inner-background: lightgreen;");
            label.setText(cuadroMensaje.getText());
            label.setAlignment(Pos.CENTER);

            label.setMaxWidth(350);

            // Posicionar el TextField dinámicamente
            double yPosition = vistaMensajes.getChildren().size() * 40.0 + 20.0; // Espaciado entre TextFields
            AnchorPane.setTopAnchor(label, yPosition);
            AnchorPane.setRightAnchor(label, 10.0);

            App.enviarMensaje(cuadroMensaje.getText(), usuario);

            // Añadir el TextField al AnchorPane
            vistaMensajes.getChildren().add(label);

            cuadroMensaje.setText("");
        }
    }

    public void establecerUsuario(String nombreUsuario) {
        this.usuario = nombreUsuario;
    }

    public void pintarUsuariosConectados(){
        Modelo m = Modelo.getInstancia();
        for(int i=0;i<m.getlUsuariosConectados().size();i++){
            Label label = new Label();
            label.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1;");
            label.setText(m.getlUsuariosConectados().get(i).getNombre());
            label.setAlignment(Pos.CENTER);

            label.setMaxWidth(200);

            double yPosition = vistaMensajes.getChildren().size() * 0.0; // Espaciado entre Labels es 0
            AnchorPane.setTopAnchor(label, yPosition);
            AnchorPane.setRightAnchor(label, 10.0);

            vistaMensajes.getChildren().add(label);
        }
    }

    public Scene getEscena() {
        return escena;
    }
}
