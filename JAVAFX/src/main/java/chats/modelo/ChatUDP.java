package chats.modelo;

import chats.controladores.ControladorPrincipal;
import chats.controladores.Controlador_ChatPublico;
import chats.vistas.Vista_ChatPublico;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatUDP extends Application {
    private static final int PUERTO = 5000;  // Mismo puerto para enviar y recibir
    private static String BROADCAST_IP;

    static {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            String[] ipParts = localIp.split("\\.");
            BROADCAST_IP = ipParts[0] + "." + ipParts[1] + "." + ipParts[2] + ".255";
        } catch (UnknownHostException e) {
            BROADCAST_IP = "255.255.255.255";
        }
    }

    public static void main(String[] args) {
        new Thread(() -> Application.launch(ChatUDP.class)).start();

        Scanner scanner = new Scanner(System.in);

        // Hilo para recibir mensajes
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
                socket.setBroadcast(true);  // Habilita la recepción de mensajes de broadcast
                byte[] buffer = new byte[1024];
                System.out.println("📡 Escuchando en el puerto " + PUERTO + " (Broadcast)...");

                while (true) {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);

                    String mensaje = new String(paquete.getData(), 0, paquete.getLength());
                    String ipRemitente = paquete.getAddress().getHostAddress();

                    Platform.runLater(() -> Controlador_ChatPublico.pintarMensajeRecibido(mensaje));

                    System.out.println("\n📩 Mensaje recibido desde " + ipRemitente + " → " + mensaje);
                    System.out.print("> ");
                }
            } catch (Exception e) {
                System.err.println("❌ Error en la recepción: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Hilo para enviar mensajes por Broadcast
    }

    public static void enviarMensaje(String mensaje){
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);  // Habilita el envío por broadcast
            InetAddress direccionBroadcast = InetAddress.getByName(BROADCAST_IP);
            byte[] buffer = mensaje.getBytes();
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionBroadcast, PUERTO);
            socket.send(paquete);
            System.out.println("📤 Mensaje enviado por broadcast → " + mensaje);
        } catch (Exception e) {
            System.err.println("❌ Error en el envío: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        new ControladorPrincipal(stage);
    }
}