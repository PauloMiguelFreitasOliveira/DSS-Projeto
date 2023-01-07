package Controller;

import Model.*;
import View.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    


    public RMFacade() {
        this.users = new HashMap<String,User>();
        this.pilotos = new HashMap<String,Piloto>();
        this.carros = new HashMap<String,Carro>();
        this.circuitos = new HashMap<String,Circuito>();
        this.campeonatos = new HashMap<String,Campeonato>();
        this.syncMap = new HashMap<>();
    }

    public String login(String email, String password, DataInputStream is, DataOutputStream os) {
        if(this.users.containsKey(email)){
            if(this.users.get(email).getPassword().equals(password)){
                this.users.get(email).setEstado(true);
                this.users.get(email).setIs(is);
                this.users.get(email).setOs(os);
                return email;
            }
        }
        return "";
    }

    public String register(String nome, String email, String password, DataInputStream is, DataOutputStream os) {
        if(!this.users.containsKey(email)) {
            this.users.put(email, new User(nome, email, password));
            this.users.get(email).setIs(is);
            this.users.get(email).setOs(os);
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
        return sb.toString();
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
}

class CampeonatoThread extends Thread{
    Campeonato campeonato;
    public CampeonatoThread(Campeonato c, ReentrantLock l){

    }
}
