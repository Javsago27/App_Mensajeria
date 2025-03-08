package chats;

import chats.controladores.ControladorPrincipal;
import chats.controladores.Controlador_ChatPublico;
import chats.modelo.Modelo;
import chats.modelo.Usuario;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class App extends Application {
    private static final int PUERTO = 12345;  // Mismo puerto para enviar y recibir
    private static String BROADCAST_IP = "192.168.7.255";

    /*static {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            String[] ipParts = localIp.split("\\.");
            BROADCAST_IP = ipParts[0] + "." + ipParts[1] + "." + ipParts[2] + ".255";
        } catch (UnknownHostException e) {
            BROADCAST_IP = "255.255.255.255";
        }
    }*/

    public static void main(String[] args) {
        new Thread(() -> Application.launch(App.class)).start();

        Scanner scanner = new Scanner(System.in);

        // Hilo para recibir mensajes
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
                socket.setBroadcast(true);  // Habilita la recepción de mensajes de broadcast
                byte[] buffer = new byte[1024];
                System.out.println("Escuchando en el puerto " + PUERTO + " (Broadcast)...");

                while (true) {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);

                    String mensaje = new String(paquete.getData(), 0, paquete.getLength());
                    String ipRemitente = paquete.getAddress().getHostAddress();

                    String []textoRecibido = mensaje.split("--");
                    InetAddress localHost = InetAddress.getLocalHost();
                    Modelo m = Modelo.getInstancia();
                    if(m.getlUsuariosConectados().isEmpty()){
                        boolean encontrado=false;
                        for(int i=0;i<m.getlUsuariosConectados().size();i++){
                            if(textoRecibido[0].equals(m.getlUsuariosConectados().get(i).getNombre())){
                                encontrado = true;
                            }
                        }
                        if(!encontrado){
                            m.getlUsuariosConectados().add(new Usuario(textoRecibido[0]));
                        }
                    }
                    String localIp = localHost.getHostAddress();
                    /*if(!textoRecibido[1].equals(localIp)){
                        Platform.runLater(() -> Controlador_ChatPublico.pintarMensajeRecibido(textoRecibido[0]));
                    }*/
                    Platform.runLater(() -> Controlador_ChatPublico.pintarMensajeRecibido(textoRecibido[1], textoRecibido[0]));

                    System.out.println("\nMensaje recibido desde " + ipRemitente + " → " + mensaje);
                    System.out.print("> ");
                }
            } catch (Exception e) {
                System.err.println("Error en la recepción: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Hilo para enviar mensajes por Broadcast
    }

    public static void enviarMensaje(String mensaje, String usuarioEnviado){
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);  // Habilita el envío por broadcast
            InetAddress direccionBroadcast = InetAddress.getByName(BROADCAST_IP);
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            String mensajeCompleto = usuarioEnviado+"--"+mensaje+"--"+localIp;
            byte[] buffer = mensajeCompleto.getBytes();
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionBroadcast, PUERTO);
            socket.send(paquete);
            System.out.println("Mensaje enviado por broadcast → " + mensaje);
        } catch (Exception e) {
            System.err.println("Error en el envío: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        new ControladorPrincipal(stage);
    }
}