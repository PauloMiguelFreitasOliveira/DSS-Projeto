package View;

import Controller.*;

import java.lang.reflect.Array;
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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private Socket s = null;
    private RMFacade rmf = null;
    private String usrEmail = null;

    //Menus
    public final String menuInicial = "200:Login\nRegisto";
    public final String menuJogador = "200:Adicionar Amigo\nConfigurar Campeonato\nEntrar Campeonato\nListar amigos\nListar Campeonatos\nListar Circuitos\nListar Pilotos\nListar Carros";
    public final String menuAdmin = "200:Adicionar Campeonato\nAdicionar Circuito\nAdicionar Piloto\nAdicionar Carro\nListar Campeonatos\nListar Circuitos\nListar Pilotos\nListar Carros";

    //Jogador Requests
    public final String loginRequest = "201:Email Password";
    public final String registerRequest = "201:Nome Email Password";
    public final String newFriendRequest = "201:Email";
    public final String joinCampeonatoRequest = "201:Codigo Carro Piloto";
    public final String configCampeonatoRequest = "201:Campeonato Carro Piloto NJogadores";

    //Admin Requests
    public final String addCampeonatoRequest = "202:Nome idCircuito1 idCircuito2 ...";
    public final String addCircuitoRequest = "201:";
    public final String addPilotoRequest = "201:Nome Nacionalidade CTS SVA";
    public final String addCarroRequest = "201:Classe Marca Modelo Cilindrada Potencia Pac PEletrico";

    public ClientThread(Socket s, RMFacade rmf){
        this.s = s;
        this.rmf = rmf;
    }

    public void sendListCampeonatos() throws IOException {
        os.writeUTF("101:"+rmf.listCampeonatos());
        os.flush();
    }

    public void sendListCircuitos() throws IOException {
        os.writeUTF("101:"+rmf.listCircuitos());
        os.flush();
    }

    public void sendListPilotos() throws IOException {
        os.writeUTF("101:"+rmf.listPilotos());
        os.flush();
    }

    public void sendListCarros() throws IOException {
        os.writeUTF("101:"+rmf.listCarros());
        os.flush();
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
            os.flush();
            int option = Integer.parseInt(is.readUTF());

            HashMap<String,String> resp = null;

            switch (option) {
                case 1 -> {
                    os.writeUTF(this.loginRequest);
                    os.flush();
                    resp = parseResp(is.readUTF(), loginRequest.split(":")[1]);
                    usrEmail = rmf.login(resp.get("Email"), resp.get("Password"),is,os);
                    if (!usrEmail.equals("")) {
                        if (rmf.emailIsAdmin(usrEmail)) {
                            os.writeUTF(String.format("100:Bem vindo Administrador %s", rmf.getNomeUser(usrEmail)));
                            os.flush();
                        } else {
                            os.writeUTF(String.format("100:Bem vindo Jogador %s", rmf.getNomeUser(usrEmail)));
                            os.flush();
                        }
                    } else {
                        os.writeUTF("300:Login Falhado");
                        os.flush();

                    }
                }
                case 2 -> {
                    os.writeUTF(this.registerRequest);
                    os.flush();
                    resp = parseResp(is.readUTF(), registerRequest.split(":")[1]);
                    usrEmail = rmf.register(resp.get("Nome"), resp.get("Email"), resp.get("Password"),is,os);
                    if (!usrEmail.equals("")) {
                        os.writeUTF(String.format("100:Bem vindo Jogador %s", rmf.getNomeUser(usrEmail)));
                        os.flush();
                    } else {
                        os.writeUTF("300:Registo Falhado");
                        os.flush();
                    }
                }
                default -> os.writeUTF("500");
            }
            if(!usrEmail.equals("")) {
                if (!rmf.emailIsAdmin(usrEmail)) { //Jogador
                    HashMap<String, String> map;
                    String list = "abcd\nefgh";
                    String rsp;
                    Synchronizer sync;
                    while (rmf.isLoggedIn(usrEmail)) {
                        os.writeUTF(menuJogador);
                        os.flush();
                        option = Integer.parseInt(is.readUTF());
                        switch (option) {
                            case 1 -> {
                                os.writeUTF(newFriendRequest);
                                os.flush();
                                map = parseResp(is.readUTF(), newFriendRequest.split(":")[1]);
                                rsp = rmf.addFriend(usrEmail, map.get("Email"));
                                os.writeUTF(rsp);
                                os.flush();
                            }
                            case 2 -> {
                                sendListCampeonatos();
                                sendListCarros();
                                sendListPilotos();
                                os.writeUTF(configCampeonatoRequest);
                                os.flush();
                                map = parseResp(is.readUTF(), configCampeonatoRequest.split(":")[1]);
                                Random random = new Random();
                                String codigo = random.ints(97, 122 + 1)
                                        .limit(6)
                                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                        .toString();
                                sync = rmf.configCampeonato(usrEmail, map.get("Campeonato"), map.get("Carro"), map.get("Piloto"), map.get("NJogadores"), codigo);
                                if (sync != null) {
                                    os.writeUTF("202:Campeonato configurado\nCodigo: " + codigo);
                                    os.flush();
                                    sync.waitCampeonato();
                                } else {
                                    os.writeUTF("300:Configuracao de campeonato");
                                    os.flush();
                                }
                            }
                            case 3 -> {
                                os.writeUTF(joinCampeonatoRequest);
                                os.flush();
                                map = parseResp(is.readUTF(), joinCampeonatoRequest.split(":")[1]);
                                sync = rmf.joinCampeonato(usrEmail, map.get("Codigo"), map.get("Carro"), map.get("Piloto"));
                                if (sync != null) {
                                    os.writeUTF("100:Entrou no campeonato\n");
                                    os.flush();
                                    sync.waitCampeonato();
                                } else {
                                    os.writeUTF("300:A entrar no campeonato");
                                    os.flush();
                                }
                            }
                            case 4 -> {
                                list = rmf.listAmigos(usrEmail);
                                os.writeUTF("101:" + list);
                                os.flush();
                            }
                            case 5 -> {
                                sendListCampeonatos();
                            }
                            case 6 -> {
                                sendListCircuitos();
                            }
                            case 7 -> {
                                sendListPilotos();
                            }
                            case 8 -> {
                                sendListCarros();
                            }
                            default -> {
                                rmf.logout(usrEmail);
                                os.writeUTF("500");
                                os.flush();
                            }
                        }
                    }
                }
                os.writeUTF("500");
            }else{ //Admin
                String list;
                HashMap<String,String> map;
                ArrayList<String> ll;
                while (rmf.isLoggedIn(usrEmail)) {
                    os.writeUTF(menuAdmin);
                    os.flush();
                    option = Integer.parseInt(is.readUTF());
                    switch (option) {
                        case 1 -> {
                            os.writeUTF(addCampeonatoRequest);
                            os.flush();
                            ll = parseResp2(is.readUTF());
                            String nome = ll.get(0);
                            ll.remove(0);
                            rmf.addCampeonato(nome,ll);
                        }
                        case 2 -> {
                            /*os.writeUTF(addCircuitoRequest);
                            os.flush();
                            map = parseResp(is.readUTF(), addCircuitoRequest.split(":")[1]);
                        */
                        }
                        case 3 -> {
                            os.writeUTF(addPilotoRequest);
                            os.flush();
                            map = parseResp(is.readUTF(), addPilotoRequest.split(":")[1]);
                            rmf.addPiloto(map.get("Nome"),map.get("Nacionalidade"),map.get("CTS"),map.get("SVA"));
                        }
                        case 4 -> {
                            os.writeUTF(addCarroRequest);
                            os.flush();
                            map = parseResp(is.readUTF(), addCarroRequest.split(":")[1]);
                            int pEletrico = 0;
                            if(map.get("Classe").contains("H")){
                                pEletrico = Integer.parseInt(map.get("PEletrico"));
                            }
                            rmf.addCarro(map.get("Classe"),map.get("Marca"),map.get("Modelo"),map.get("Cilindrada"),map.get("Potencia"),pEletrico);
                        }
                        case 5 -> {
                            sendListCampeonatos();
                        }
                        case 6 -> {
                            sendListCircuitos();
                        }
                        case 7 -> {
                            sendListPilotos();
                        }
                        case 8 -> {
                            sendListCarros();
                        }
                        default -> {
                            rmf.logout(usrEmail);
                            os.writeUTF("500");
                            os.flush();
                        }
                    }
                }
            }
        }catch(SocketException e) {
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

            }catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
    public static HashMap<String,String> parseResp(String recv, String struct){
        String[] recvArr = recv.split(",");
        String[] structArr = struct.split(" ");
        System.out.println(Arrays.toString(recvArr));
        System.out.println(Arrays.toString(structArr));

        HashMap<String,String> aux = new HashMap<>();
        for(int i = 0; i < recvArr.length; i++){
            aux.put(structArr[i],recvArr[i]);
        }
        return aux;
    }
    public static ArrayList<String> parseResp2(String recv){
        String[] recvArr = recv.split(",");
        ArrayList<String> aux = new ArrayList<>(Arrays.asList(recvArr));
        Collections.reverse(aux);
        return aux;
    }
}
