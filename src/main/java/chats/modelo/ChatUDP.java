package chats.modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class ChatUDP {
    private static final int PUERTO = 9876;
    private static String BROADCAST_IP;
    private Consumer<String> onMessageReceived; // Callback para actualizar la interfaz

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

    public ChatUDP(Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
        iniciarRecepcion();
    }

    private void iniciarRecepcion() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];
    
                // Obtener la IP local para filtrar mensajes enviados por uno mismo
                String miIP = InetAddress.getLocalHost().getHostAddress();
    
                while (true) {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);
    
                    String mensaje = new String(paquete.getData(), 0, paquete.getLength());
                    String ipRemitente = paquete.getAddress().getHostAddress();
    
                    // 📌 Filtrar mensajes enviados por uno mismo
                    if (!ipRemitente.equals(miIP)) {
                        String mensajeFormateado = "📩 " + ipRemitente + " → " + mensaje;
    
                        // Enviar mensaje a la interfaz gráfica
                        if (onMessageReceived != null) {
                            onMessageReceived.accept(mensajeFormateado);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    

    public void enviarMensaje(String mensaje) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                InetAddress direccionBroadcast = InetAddress.getByName(BROADCAST_IP);

                byte[] buffer = mensaje.getBytes();
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionBroadcast, PUERTO);

                socket.send(paquete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
