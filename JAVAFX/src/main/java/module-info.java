module chats.vistas {
    requires javafx.controls;
    requires javafx.fxml;

    opens chats.vistas to javafx.fxml;
    opens chats.modelo to javafx.graphics;
    exports chats;
}
