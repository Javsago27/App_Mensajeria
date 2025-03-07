package chats.controladores;

import java.io.IOException;

import javafx.scene.Scene;
import chats.vistas.Vista_ChatPublico;

public class Controlador_ChatPublico extends Controlador{

    private Vista_ChatPublico vista;
    private ControladorPrincipal controlador;

    public Controlador_ChatPublico(ControladorPrincipal controlador) throws IOException{
        this.controlador = controlador;
        vista = new Vista_ChatPublico(this);
    }



    @Override
    public void mostrar() {
        vista.mostrar();
    }

    public Scene getEscena1() {
        return vista.getEscena();
    }
}