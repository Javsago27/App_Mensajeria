package chats.vistas;

import java.io.IOException;

import chats.App;
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

    public void pintarMensajeRecibido(String mensaje){
        Label label = new Label();
        //textField.setDisable(true);
        label.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: red; -fx-opacity: 1; -fx-control-inner-background: lightgreen;");
        label.setText(mensaje);
        label.setAlignment(Pos.CENTER);

        label.setMaxWidth(350);

        // Posicionar el TextField dinámicamente
        double yPosition = vistaMensajes.getChildren().size() * 40.0 + 20.0; // Espaciado entre TextFields
        AnchorPane.setTopAnchor(label, yPosition);
        AnchorPane.setLeftAnchor(label, 10.0);

        // Añadir el TextField al AnchorPane
        vistaMensajes.getChildren().add(label);
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

            App.enviarMensaje(cuadroMensaje.getText());

            // Añadir el TextField al AnchorPane
            vistaMensajes.getChildren().add(label);

            cuadroMensaje.setText("");
        }
    }

    public Scene getEscena() {
        return escena;
    }
    
}
