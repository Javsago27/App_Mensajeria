package chats.controladores;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ControladorPrincipal extends Controlador {

    private Stage escenario;
    private Scene escena;
    private Controlador_ChatPublico controladorChatPublico;

    public ControladorPrincipal(Stage stage) throws IOException {
        this.escenario = stage;

        //Creación de controladores  vista
        controladorChatPublico = new Controlador_ChatPublico(this);

        //La primera escena se carga de forma especial
        escenario.setScene(controladorChatPublico.getEscena1());
        escenario.show();
    }

    public void irAVista1() {
        System.out.println("ControladorPrincipal 'irAVista1'");
        escenario.setScene(controladorChatPublico.getEscena1());
        controladorChatPublico.mostrar();
    }

    @Override
    public void mostrar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrar'");
    }
}