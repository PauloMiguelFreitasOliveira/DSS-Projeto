package Model;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.Set;
import java.util.Comparator;
import java.util.Collections;
//import java.util.LinkedList;
import java.io.Serializable;

public class Campeonato implements Serializable
{   
    private String nome;
    private List<Circuito> circuitos;
    private Jogador criador;
    private int nJogadores;
    private List<Jogador> jogadores;
    private Map<String,Carro> carros;
    private Map<String,Piloto> pilotos;
    private List<Corrida> corridas;
    private Map<String,Integer> classificacao;
    private Map<String,Integer> classificacaoH;
    private int prova;
    private boolean running;
    
    public Campeonato() {
        this.nome = "";
        this.circuitos = new ArrayList<>();
        this.criador = null;
        this.nJogadores = 0;
        this.jogadores = new ArrayList<>();
        this.carros = new HashMap<>();
        this.pilotos = new HashMap<>();
        this.corridas = new ArrayList<>();
        this.classificacao = new HashMap<String,Integer>();
        this.prova = 0;
        this.running = false;
    }
    
    public Campeonato(String nome, List<Circuito> circuitos)
    {
        this.nome = nome;
        this.circuitos = circuitos;
        this.jogadores = new ArrayList<>();
        this.carros = new HashMap<>();
        this.pilotos = new HashMap<>();
        this.corridas = new ArrayList<>();
        this.classificacao = new HashMap<String,Integer>();
        this.prova = 0;
        this.running = false;
    }
    
    public Campeonato(Campeonato c)
    {
        this.nome = c.getNome();
        this.circuitos = c.getCircuitos();
        this.jogadores = new ArrayList<>();
        this.carros = new HashMap<>();
        this.pilotos = new HashMap<>();
        this.corridas = new ArrayList<>();
        this.classificacao = new HashMap<String,Integer>();
        this.prova = 0;
        this.running = false;
    }
    
    public String getNome(){
        return this.nome;
    }

    public List<Circuito> getCircuitos(){
        return this.circuitos;
    }

    public Jogador getCriador(){
        return this.criador;
    }

    public int getNJogadores(){
        return this.nJogadores;
    }
    
    public List<Jogador> getJogadores(){
        return this.jogadores;
    }

    public List<Corrida> getCorridas(){
        return this.corridas;
    }

    public Map<String, Integer> getClassificacao(){
        if(this.classificacao == null) return null;
        HashMap<String,Integer> aux = new HashMap<String,Integer>();
        for(String c : this.classificacao.keySet())
        {
            aux.put(c, this.classificacao.get(c));
        }
        return aux;
    }

    public boolean isFull(){
        return this.jogadores.size() >= this.nJogadores;
    }

    public String filled(){
        return this.jogadores.size()+"/"+this.nJogadores;
    }

    public boolean isRunning() {
        return running;
    }

    public void setProva(int prova) {
        this.prova = prova;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setnJogadores(int nJogadores) {
        this.nJogadores = nJogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public void addJogador(Jogador jogador, Carro c, Piloto p) {
        this.jogadores.add(jogador);
        this.carros.put(jogador.getEmail(),c);
        this.pilotos.put(jogador.getEmail(),p);
    }

    public void configCampeonato(Jogador criador, Carro c, Piloto p, int nJogadores) {
        this.criador = criador;
        this.nJogadores = nJogadores;
        addJogador(criador, c, p);

    }

    public ArrayList<Carro> getCarroList(){
        ArrayList<Carro> aux = new ArrayList<>();
        for(Jogador j : this.jogadores){
            aux.add(this.carros.get(j.getEmail()).clone());
        }
        Collections.reverse(aux);
        return aux;
    }

    public Map<String, Piloto> getPilotos() {
        return pilotos;
    }

    public ArrayList<Piloto> getPilotoList(){
        ArrayList<Piloto> aux = new ArrayList<>();
        for(Jogador j : this.jogadores){
            aux.add(this.pilotos.get(j.getEmail()).clone());
        }
        Collections.reverse(aux);
        return aux;
    }

    public ArrayList<String> getUsernameList(){
        ArrayList<String> aux = new ArrayList<>();
        for(Jogador j : this.jogadores){
            aux.add(j.getNome());
        }
        Collections.reverse(aux);
        return aux;
    }

    public Map<String, Carro> getCarros() {
        return carros;
    }

    public void setCorridas(List<Corrida> corridas) {
        this.corridas = corridas;
    }

    public void setClassificacaoH(Map<String, Integer> classificacaoH) {
        this.classificacaoH = classificacaoH;
    }

    public void setClassificacao(Map<String, Integer> classificacao) {
        this.classificacao = classificacao;
    }

    public void setCircuitos(List<Circuito> circuitos) {
        this.circuitos = circuitos;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    //Metodos
    /**
     * Adicionar corrida ao campeonato
     */
    public void addCorrida(Corrida c)
    {
        this.corridas.add(c.clone());
    }

    
    /**
     * Obter corrida nr x
     */
    public Corrida getCorrida(int x)
    {
        return this.corridas.get(x-1).clone();
    }
    
    /**
     * Corridas agendadas por realizar
     */
    public String agendaCorridas()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAGENDA");
        for(int i=0;i<this.corridas.size();i++)
        {
            sb.append("\n");
            sb.append(i+1);sb.append(" - ");sb.append(this.corridas.get(i).getCircuito().getNome());
            if(i<this.prova)
                sb.append(" REALIZADA");
        }
        return sb.toString();
    }

    /**
     * Lista Circuitos
     */
    public String listaCircuitos()
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.corridas.size();i++)
        {
            sb.append("\n");
            sb.append(i+1);sb.append("- ");sb.append(this.corridas.get(i).getCircuito().getNome());
        }
        return sb.toString();
    }

    public String circuitosToString(){
        StringBuilder sb = new StringBuilder();
        for(Circuito c : circuitos){
            sb.append(c.getNome()).append(", ");
        }
        return sb.toString();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(" Nome-");sb.append(this.nome);
        sb.append(";\tCircuitos-");sb.append(circuitosToString()).append("\n");
        return sb.toString();
    }
}
