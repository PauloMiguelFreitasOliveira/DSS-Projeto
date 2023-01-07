package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String ip, int port) throws IOException{
        this.clientSocket = new Socket(ip, port);
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.in = new DataInputStream(clientSocket.getInputStream());
    }

    public String getNext() throws IOException{
        return in.readUTF();
    }

    public  void sendResponse(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}

