package chats.modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChatUDP {
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

                    System.out.println("\n📩 Mensaje recibido desde " + ipRemitente + " → " + mensaje);
                    System.out.print("> ");
                }
            } catch (Exception e) {
                System.err.println("❌ Error en la recepción: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Hilo para enviar mensajes por Broadcast
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);  // Habilita el envío por broadcast
            InetAddress direccionBroadcast = InetAddress.getByName(BROADCAST_IP);
            System.out.println("✏ Escribe un mensaje para enviar (o 'salir' para terminar):");

            while (true) {
                System.out.print("> ");
                String mensaje = scanner.nextLine();
                if (mensaje.equalsIgnoreCase("salir")) break;

                byte[] buffer = mensaje.getBytes();
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionBroadcast, PUERTO);

                socket.send(paquete);
                System.out.println("📤 Mensaje enviado por broadcast → " + mensaje);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en el envío: " + e.getMessage());
            e.printStackTrace();
        }
    }
}