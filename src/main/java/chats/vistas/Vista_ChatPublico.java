package chats.vistas;

import java.io.IOException;
import chats.modelo.ChatUDP;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import chats.controladores.Controlador_ChatPublico;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Vista_ChatPublico extends Vista {
    private Controlador_ChatPublico controlador;
    private ChatUDP chatUDP;
    protected final String ficheroInterfaz = "chatPublico.fxml";
    private Scene escena;

    @FXML
    private AnchorPane root;
    @FXML
    private TextField cuadroMensaje;
    @FXML
    private AnchorPane vistaMensajes;

    public void initialize() {
        root.setOnKeyPressed(this::detectarTeclas);
    }

    public Vista_ChatPublico(Controlador_ChatPublico controlador) throws IOException {
        super();
        this.controlador = controlador;
        escena = new Scene(cargarInterfaz(ficheroInterfaz, this));

        // Iniciar el chat UDP y definir qué hacer cuando llegue un mensaje
        chatUDP = new ChatUDP(this::mostrarMensajeRecibido);
    }

    private void detectarTeclas(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                enviarMensaje();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void enviarMensaje() throws IOException {
        if (!cuadroMensaje.getText().isEmpty()) {
            String mensaje = cuadroMensaje.getText();
            
            // Mostrar mensaje en la interfaz
            mostrarMensaje("Tú: " + mensaje, "lightgreen");

            // Enviar mensaje por UDP
            chatUDP.enviarMensaje(mensaje);

            // Limpiar cuadro de texto
            cuadroMensaje.setText("");
        }
    }

    private void mostrarMensaje(String mensaje, String color) {
        Label label = new Label(mensaje);
        label.setStyle("-fx-padding: 10; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: black; -fx-background-color: " + color + ";");
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(350);

        double yPosition = vistaMensajes.getChildren().size() * 40.0 + 20.0;
        AnchorPane.setTopAnchor(label, yPosition);
        AnchorPane.setRightAnchor(label, 10.0);

        vistaMensajes.getChildren().add(label);
    }

    private void mostrarMensajeRecibido(String mensaje) {
        // Ejecutar en el hilo de la interfaz gráfica
        Platform.runLater(() -> mostrarMensaje(mensaje, "lightblue"));
    }

    public Scene getEscena() {
        return escena;
    }
}
