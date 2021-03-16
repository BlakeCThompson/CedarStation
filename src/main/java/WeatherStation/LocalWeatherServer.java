package WeatherStation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalWeatherServer {
    String host = "localhost";
    int port = 10430;



    public LocalWeatherServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
        System.out.println("Server started");

        Socket client = new Socket(host, port);
        System.out.println("connecting to server");

        Socket connection = serverSocket.accept();
        System.out.println("Connection established.");

        DataOutputStream clientOut = new DataOutputStream(client.getOutputStream());
        DataInputStream clientIn = new DataInputStream(client.getInputStream());
        DataOutputStream serverOut = new DataOutputStream(connection.getOutputStream());
        DataInputStream serverIn = new DataInputStream(connection.getInputStream());
        System.out.println("Communication-ready");

        String hello = "Hello World";
        byte[] networkHello = hello.getBytes();
        clientOut.writeInt(networkHello.length);
        clientOut.write(networkHello);
        clientOut.flush();
        System.out.println("Message sent to server");

        int length = serverIn.readInt();
        if(length > 0) {
            byte[] messageIn = new byte[length];
            serverIn.readFully(messageIn, 0 , messageIn.length);
            System.out.println("message received by server from client: "+new String(messageIn));
        }

    }
}
