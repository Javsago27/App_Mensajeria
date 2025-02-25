package chats.vistas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public abstract class Vista {

    protected Parent cargarInterfaz(String ficheroInterfaz, Vista vista) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Vista.class.getResource(ficheroInterfaz));
        fxmlLoader.setController(vista);
        return fxmlLoader.load();
    }

    public void mostrar(){}
}
