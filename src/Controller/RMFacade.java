package Controller;

import Model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class RMFacade {

    public Map<String, User> users;
    public Map<String,Piloto> pilotos;
    public Map<String,Carro> carros;
    public Map<String,Circuito> circuitos;
    public Map<String,Campeonato> campeonatos;
    public Map<String,Campeonato> campeonatosAtivos;
    public Map<String,Synchronizer> syncMap;

    
    public void mockData(){
        Admin a1 = new Admin("admin1","admin1@admin.com","12345");
        Admin a2 = new Admin("admin2","admin2@admin.com","23456");
        Jogador j1 = new Jogador("jog1","jog1@jog.com","54321");
        Jogador j2 = new Jogador("jog2","jog2@jog.com","65432");
        Jogador j3 = new Jogador("jog3","jog3@jog.com","76543");
        Jogador j4 = new Jogador("jog4","jog4@jog.com","87654");
        this.users.put(a1.getEmail(),a1);
        this.users.put(a2.getEmail(),a2);
        this.users.put(j1.getEmail(),j1);
        this.users.put(j2.getEmail(),j2);
        this.users.put(j3.getEmail(),j3);
        this.users.put(j4.getEmail(),j4);

        Piloto p1 = new Piloto("Joao Julio","Portugues",0.1,0.9);
        Piloto p2 = new Piloto("Robert Schimdt","Alemao",0.9,0.1);
        Piloto p3 = new Piloto("Li Ju Son","Japones",0.4,0.6);
        this.pilotos.put("pi1",p1);
        this.pilotos.put("pi2",p2);
        this.pilotos.put("pi3",p3);

        GT c1 = new GT("Ferrari","California",3500,350);
        GTH c2 = new GTH("Tesla","X",2000,250,100);
        PC1 c3 = new PC1("Ford","GT",6000,500);
        PC1H c4 = new PC1H("Porsche","918 Spyder",6000,500,200);
        PC2 c5 = new PC2("Lamborghini","Gallardo",4300,400,30);
        PC2H c6 = new PC2H("Porsche","918 Spyder",4900,500,40,100);
        SC c7 = new SC("Ford","Focus RS",2500,230);
        this.carros.put("ca1",c1);
        this.carros.put("ca2",c2);
        this.carros.put("ca3",c3);
        this.carros.put("ca4",c4);
        this.carros.put("ca5",c5);
        this.carros.put("ca6",c6);
        this.carros.put("ca7",c7);
    }

    public RMFacade() {
        this.users = new HashMap<String,User>();
        this.pilotos = new HashMap<String,Piloto>();
        this.carros = new HashMap<String,Carro>();
        this.circuitos = new HashMap<String,Circuito>();
        this.campeonatos = new HashMap<String,Campeonato>();
        this.syncMap = new HashMap<>();
        mockData();
    }

    public String login(String email, String password, DataInputStream is, DataOutputStream os) {
        if(this.users.containsKey(email)){
            if(this.users.get(email).getPassword().equals(password)){
                this.users.get(email).setEstado(true);
                this.users.get(email).setIs(is);
                this.users.get(email).setOs(os);
                this.users.get(email).setEstado(true);
                return email;
            }
        }
        return "";
    }

    public String register(String nome, String email, String password, DataInputStream is, DataOutputStream os) {
        if(!this.users.containsKey(email)) {
            this.users.put(email, new Jogador(nome, email, password));
            this.users.get(email).setIs(is);
            this.users.get(email).setOs(os);
            this.users.get(email).setEstado(true);
            return email;
        }else{
            return "";
        }
    }

    public void logout(String email) {
        this.users.get(email).setEstado(false);
    }

    public String getNomeUser(String email) {
        return this.users.get(email).getNome();
    }

    public boolean emailIsAdmin(String email) {
        return this.users.containsKey(email);
    }

    public boolean isLoggedIn(String email) {
        return this.users.get(email).getEstado();
    }

    public String listAmigos(String email) {
        StringBuilder sb = new StringBuilder("100:");
        if(this.users.get(email) instanceof Jogador j) {
            for (String a : j.getAmigos()) {
                sb.append(a);
                sb.append("\n");
            }
            return sb.toString();
        }
        return "";
    }

    public String listCampeonatos() {
        StringBuilder sb = new StringBuilder("100:");

        for (Map.Entry<String,Campeonato> e : this.campeonatos.entrySet()) {
            sb.append(e.getKey());
            sb.append("-");
            sb.append(e.getValue());
        }
        return sb.toString();
    }

    public String listCircuitos() {
        StringBuilder sb = new StringBuilder("100:");

        for (Map.Entry<String,Circuito> e : this.circuitos.entrySet()) {
            sb.append(e.getKey());
            sb.append("-");
            sb.append(e.getValue());
        }
        return sb.toString();
    }

    public String listCarros() {
        StringBuilder sb = new StringBuilder("100:");

        for (Map.Entry<String,Carro> e : this.carros.entrySet()) {
            sb.append(e.getKey());
            sb.append("-");
            sb.append(e.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String listPilotos() {
        StringBuilder sb = new StringBuilder("100:");

        for (Map.Entry<String,Piloto> e : this.pilotos.entrySet()) {
            sb.append(e.getKey());
            sb.append("-");
            sb.append(e.getValue());
        }
        return "a\nb\nc\nd\ne\nf\ng\nh";
        //return sb.toString();
    }

    public String addFriend(String usrEmail, String friendEmail) {
        if (this.users.containsKey(usrEmail)) {
            if (this.users.get(usrEmail) instanceof Jogador usr) {
                if (this.users.get(friendEmail) instanceof Jogador) {
                    if (!usr.getAmigos().contains(friendEmail)) {
                        usr.addAmigo(friendEmail);
                        return "100:Amigo adicionado com sucesso!";
                    }
                }
            }
        }
        return "300:Erro ao adicionar amigo";
    }

    public Synchronizer configCampeonato(String usrEmail, String idCampeonato, String idCarro, String idPiloto, String nJogadores, String codigo){
        try {
            Campeonato aux = new Campeonato(this.campeonatos.get(idCampeonato));
            aux.configCampeonato((Jogador) this.users.get(usrEmail), this.carros.get(idCarro), this.pilotos.get(idPiloto), Integer.parseInt(nJogadores));
            this.campeonatosAtivos.put(codigo,aux);
            Synchronizer sync = new Synchronizer(aux);
            this.syncMap.put(codigo,sync);
            CampeonatoThread campThread = new CampeonatoThread(this.campeonatosAtivos.get(codigo),this.syncMap.get(codigo));
            campThread.start();
            return sync;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Synchronizer joinCampeonato(String email, String codigo, String idCarro, String idPiloto){
        if(this.campeonatosAtivos.containsKey(codigo) && this.carros.containsKey(idCarro) && this.pilotos.containsKey(idPiloto)){
            Campeonato cpo = this.campeonatosAtivos.get(codigo);
            if(this.users.get(email) instanceof Jogador j){
                if(!cpo.isRunning()){
                   cpo.addJogador(j,this.carros.get(idCarro),this.pilotos.get(idPiloto));
                   if(cpo.getJogadores().size()==cpo.getNJogadores()){
                       startCampeonato();
                       return this.syncMap.get(codigo);
                   }
                }
            }
        }
        return null;
    }

    public void startCampeonato(){

    }

    public boolean addCampeonato(String nome, ArrayList<String> l){
        if(l.size()==0){
            return false;
        }
        ArrayList<Circuito> aux = new ArrayList<>();
        for(String cod : l){
            aux.add(this.circuitos.get(cod));
        }
        Campeonato c = new Campeonato(nome,aux);
        this.campeonatos.put(genKey(this.campeonatos.keySet()),c);
        return true;
    }


    public void addPiloto(String nome, String nacionalidade, String cts, String sva){
        Piloto p = new Piloto(nome,nacionalidade,Double.parseDouble(cts),Double.parseDouble(sva));
        this.pilotos.put(genKey(this.pilotos.keySet()),p);
    }


    public boolean addCarro(String classe, String marca, String modelo, String cilindrada, String potencia, Integer pEletrico){
        Carro c = null;
        Random randomNum = new Random();
        int rnd = randomNum.nextInt(100);
        if(classe.equals("GT")){
            c = new GT(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia));
        }else if(classe.equals("GTH")){
            c = new GTH(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia),pEletrico);
        }else if(classe.equals("PC1")){
            c = new PC1(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia));
        }else if(classe.equals("PC1H")){
            c = new PC1H(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia),pEletrico);
        }else if(classe.equals("PC2")){
            c = new PC2(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia),rnd);
        }else if(classe.equals("PC2H")){
            c = new PC2H(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia),pEletrico,rnd);
        }else if(classe.equals("SC")){
            c = new SC(marca,modelo,Integer.parseInt(cilindrada),Integer.parseInt(potencia));
        }
        if(c != null){
            this.carros.put(genKey(this.carros.keySet()),c);
            return true;
        }
        return false;
    }

    public static String genKey(Set<String> keys){
        ArrayList<Integer> aux = new ArrayList<>();
        String pref = "";
        for(String k : keys){
            pref = k.replaceAll("[0-9]","");
            int value = Integer.parseInt(k.replaceAll("[^0-9]",""));
            aux.add(value);
        }
        int nxt = Collections.max(aux)+1;
        return pref+nxt;
    }
}

class CampeonatoThread extends Thread{
    Campeonato campeonato;
    Synchronizer sync;
    public CampeonatoThread(Campeonato c, Synchronizer sync){
        this.campeonato = c;
        this.sync = sync;
    }

    public void sendToAllClients(String msg) throws IOException {
        for(Jogador j : this.campeonato.getJogadores()){
            j.getOs().writeUTF(msg);
        }
    }
    public void sendToClient(String email,String msg) throws IOException {
        for(Jogador j : this.campeonato.getJogadores()){
            if(j.getEmail().equals(email)){
                j.getOs().writeUTF(msg);
            }
        }
    }

    public String waitClientAnswer(String email) throws IOException {
        String s = "";
        for(Jogador j : this.campeonato.getJogadores()){
            if(j.getEmail().equals(email)){
                s = j.getIs().readUTF();
            }
        }
        return s;
    }
    @Override
    public void run() {
        List<Circuito> circuitos = this.campeonato.getCircuitos();
        ArrayList<Carro> carros = this.campeonato.getCarroList();
        ArrayList<Piloto> pilotos = this.campeonato.getPilotoList();
        ArrayList<Corrida> corridas = new ArrayList<>();
        for(Circuito c : circuitos){
            int clima = 0;
            if(Math.random()<0.5){
                clima = 1;
            }
            Corrida corr = new Corrida(carros,pilotos,c,clima);
            try {
                sendToAllClients("hereeeee");
                System.out.println(corr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*Run corr.simulaCorrida() 'Falta fazer'
            * imprimir resultados por volta
            * deixar cada user mudar cenas
            * proxima corrida*/

        }
        this.sync.endCampeonato(); //Retomar threads de clientes
    }
}
