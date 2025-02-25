import java.net.*;
import java.util.Scanner;

public class App {
    private static final int PORT = 9876;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            InetAddress broadcastAddress = getBroadcastAddress();

            // Hilo para recibir mensajes
            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\nMensaje recibido: " + receivedMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            System.out.print("Introduce tu nombre: ");
            String nombre = scanner.nextLine();
            String mensaje = nombre + " se ha conectado";
            byte[] buffer = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, PORT);
            socket.send(packet);

            System.out.println("Mensaje de conexión enviado. Puedes enviar mensajes ahora...");

            while (true) {
                System.out.print("Introduce un mensaje (o 'exit' para salir): ");
                String customMessage = scanner.nextLine();
                if (customMessage.equalsIgnoreCase("exit")) {
                    break;
                }
                mensaje = nombre + ": " + customMessage;
                buffer = mensaje.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, PORT);
                socket.send(packet);
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("Cerrado");
        } finally {
            scanner.close();
        }
    }

    private static InetAddress getBroadcastAddress() throws SocketException, UnknownHostException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
            InetAddress broadcast = interfaceAddress.getBroadcast();
            if (broadcast != null) {
                return broadcast;
            }
        }
        throw new UnknownHostException("No se pudo determinar la dirección de broadcast");
    }
}
