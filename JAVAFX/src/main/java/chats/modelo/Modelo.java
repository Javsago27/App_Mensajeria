package chats.modelo;

import java.util.ArrayList;

public class Modelo {

    private static Modelo m;
    private ArrayList<Usuario> lUsuariosConectados;

    private Modelo(){
        lUsuariosConectados = new ArrayList<>();
    }

    public static Modelo getInstancia(){
        if(m==null){
            m = new Modelo();
        }
        return m;
    }

    public ArrayList<Usuario> getlUsuariosConectados() {
        return lUsuariosConectados;
    }
    public void setlUsuariosConectados(ArrayList<Usuario> lUsuariosConectados) {
        this.lUsuariosConectados = lUsuariosConectados;
    }
}
