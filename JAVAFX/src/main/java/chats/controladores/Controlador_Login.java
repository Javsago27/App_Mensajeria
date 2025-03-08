package chats.controladores;

import chats.vistas.Vista_ChatPublico;
import chats.vistas.Vista_Login;
import javafx.scene.Scene;

import java.io.IOException;

public class Controlador_Login extends Controlador{

    private static Vista_Login vista;
    private ControladorPrincipal controlador;

    public Controlador_Login(ControladorPrincipal controlador) throws IOException{
        this.controlador = controlador;
        vista = new Vista_Login(this);
    }

    public void iniciarSesion(String nombreUsuario){
        controlador.iniciarSesion(nombreUsuario);
    }

    @Override
    public void mostrar() {
        vista.mostrar();
    }

    public Scene getEscena1() {
        return vista.getEscena();
    }
}