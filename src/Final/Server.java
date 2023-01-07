package Final;

import java.net.SocketException;
import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.HashMap;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Server {

    public static void main(String args[]) throws IOException{
        Socket clientSocket=null;
        ServerSocket serverSocket=null;
        System.out.println("Server Listening...");
        
        serverSocket = new ServerSocket(4445);

        RMFacade rmf = new RMFacade();

        while(true){
            try{
                clientSocket = serverSocket.accept();
                System.out.println("Connection Established");
                ClientThread svThread = new ClientThread(clientSocket,rmf);
                svThread.start();
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("Server Exiting...");
                serverSocket.close();
            }
        }

    }        
}


class ClientThread extends Thread{  
    DataInputStream is = null;
    DataOutputStream os = null;
    Socket s = null;
    RMFacade rmf = null;
    String usrEmail = null;
    
    public final String menuInicial = "200:Login\nRegisto";
    public final String loginRequest = "201:Email Password";
    public final String registerRequest = "201:Nome Email Password";
    public final String newFriendRequest = "201:Email";
    public final String joinCampeonatoRequest = "201:Codigo do lobby";
    public final String menuJogador = "200:Adicionar Amigo\nConfigurar Campeonato\nEntrar Campeonato\nListar amigos\nListar Campeonatos\nListar Circuitos\nListar Pilotos\nListar Carros";
    public final String configCampeonatoRequest = "201:Campeonato Carro Piloto NJogadores";

    public ClientThread(Socket s, RMFacade rmf){
        this.s = s;
        this.rmf = rmf;
    }

    public void run() {
        System.out.println("Client thread started!");
        try{
            is = new DataInputStream(s.getInputStream());
            os = new DataOutputStream(s.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in client thread!");
        }

        try {
            os.writeUTF(this.menuInicial);
            int option = Integer.parseInt(is.readUTF());

            HashMap<String,String> resp = null;

            switch (option) {
                case 1 -> {
                    os.writeUTF(this.loginRequest);
                    resp = parseResp(is.readUTF(), loginRequest);
                    usrEmail = rmf.login(resp.get("Email"), resp.get("Password"));
                    if (!usrEmail.equals("")) {
                        if (rmf.emailIsAdmin(usrEmail)) {
                            os.writeUTF(String.format("100:Bem vindo Administrador %s", rmf.getNomeUser(usrEmail)));
                        } else {
                            os.writeUTF(String.format("100:Bem vindo Jogador %s", rmf.getNomeUser(usrEmail)));
                        }
                    } else {
                        os.writeUTF("300:Login Falhado");

                    }
                }
                case 2 -> {
                    os.writeUTF(this.registerRequest);
                    resp = parseResp(is.readUTF(), registerRequest);
                    usrEmail = rmf.register(resp.get("Nome"), resp.get("Email"), resp.get("Password"));
                    if (!usrEmail.equals("")) {
                        os.writeUTF(String.format("100:Bem vindo Jogador %s", rmf.getNomeUser(usrEmail)));
                    } else {
                        os.writeUTF("300:Registo Falhado");
                    }
                }
                default -> os.writeUTF("200");
            }

            if(!usrEmail.equals("")){
                HashMap<String,String> map;
                String list = "abcd\nefgh";
                String rsp;
                while(rmf.isLoggedIn(usrEmail)){
                    os.writeUTF(menuJogador);
                    option = Integer.parseInt(is.readUTF());
                    switch (option) {
                        case 1 -> {
                            os.writeUTF(newFriendRequest);
                            map = parseResp(is.readUTF(), newFriendRequest);
                            rsp = rmf.addFriend(usrEmail, map.get("Email"));
                            os.writeUTF(rsp);
                        }
                        case 2 -> {
                            os.writeUTF(configCampeonatoRequest);
                            map = parseResp(is.readUTF(), configCampeonatoRequest);
                            rsp = rmf.configCampeonato(usrEmail, map.get("Campeonato"), map.get("Carro"), map.get("Piloto"), map.get("NJogadores"));
                            os.writeUTF(rsp);
                        }
                        case 3 -> {
                            os.writeUTF(joinCampeonatoRequest);
                            map = parseResp(is.readUTF(), joinCampeonatoRequest);
                            rsp = rmf.joinCampeonato(usrEmail, map.get("Codigo"));
                            os.writeUTF(rsp);
                        }
                        case 4 -> {
                            list = rmf.listAmigos(usrEmail);
                            os.writeUTF(list);
                        }
                        case 5 -> {
                            list = rmf.listCampeonatos();
                            os.writeUTF(list);
                        }
                        case 6 -> {
                            list = rmf.listCircuitos();
                            os.writeUTF(list);
                        }
                        case 7 -> {
                            list = rmf.listPilotos();
                            os.writeUTF(list);
                        }
                        case 8 -> {
                            list = rmf.listCarros();
                            os.writeUTF(list);
                        }
                        default -> {
                            rmf.logout(usrEmail);
                            os.writeUTF("500");
                        }
                    }
                }
            }
            os.writeUTF("500");
        } catch(SocketException e) {
            System.out.println("Client Exiting!");

        }catch(Exception e) {
            e.printStackTrace();
        }
        finally{    
            try{
                System.out.println("Connection Closing...");
                if (is!=null){
                    is.close(); 
                }
                if(os!=null){
                    os.close();
                }
                if (s!=null){
                    s.close();
                }

                }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
    public static HashMap<String,String> parseResp(String recv, String struct){
        String[] recvArr = recv.split(",");
        String[] structArr = recv.split(" ");

        HashMap<String,String> aux = new HashMap<>();
        for(int i = 0; i < recvArr.length; i++){
            aux.put(recvArr[i],structArr[i]);
        }
        return aux;
    }
}
