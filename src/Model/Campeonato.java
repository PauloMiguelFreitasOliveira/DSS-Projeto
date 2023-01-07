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

//import java.io.ObjectInputStream.GetField;

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
    
    public Campeonato()
    {
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
    }
    
    public Campeonato(Campeonato c)
    {
        this.nome = c.getNome();
        this.circuitos = c.getCircuitos();
        this.criador = c.getCriador();
        this.nJogadores = c.getNJogadores();
        this.jogadores = c.getJogadores();
        this.corridas = c.getCorridas();
        this.classificacao = c.getClassificacao();
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
        HashMap<String,Integer> aux = new HashMap<String,Integer>();
        for(String c : this.classificacao.keySet())
        {
            aux.put(c, this.classificacao.get(c));
        }
        return aux;
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

    public ArrayList<Piloto> getPilotoList(){
        ArrayList<Piloto> aux = new ArrayList<>();
        for(Jogador j : this.jogadores){
            aux.add(this.pilotos.get(j.getEmail()).clone());
        }
        Collections.reverse(aux);
        return aux;
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
     * Simular proxima corrida
     */
    /*public String simularProximaCorrida()
    {
        //StringBuilder sb = new StringBuilder();
        String res;
        if(this.corridas.size() == this.prova)
        {
            //sb.append("\nNÃO HÁ CORRIDAS POR REALIZAR!!!");
            res = "\nNÃO HÁ CORRIDAS POR REALIZAR!!!";
        }
        else
        {
            this.corridas.get(this.prova).simulaCorrida();
            //sb.append(this.corridas.get(this.prova).printResultados());
            res = this.corridas.get(this.prova).printResultados();
            this.prova++;
        }
        
        //return sb.toString();
        return res;
    }*/
    
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
     * Lista a classificacao atual
     */
    /*public String printClassificacao()
    {
        //chamo o ordena e faço print!!
        List<Map.Entry<String, Integer>> aux = this.ordenaClassificacao(this.classificacao);
        StringBuilder sb = new StringBuilder();
        sb.append("\nClassificacao Geral");
        sb.append("\n=========================");
        for(int i=0;i<aux.size();i++)
        {
            sb.append("\n");
            sb.append(i+1);sb.append("º: ");sb.append(aux.get(i));
            //sb.append("\t");sb.append(aux.get(i));
            //i++;
        }
        List<Map.Entry<String, Integer>> aux2 = this.ordenaClassificacao(this.classificacaoH);
        sb.append("\n\nClassificacao Geral Hibrido");
        sb.append("\n=========================");
        for(int i=0;i<aux2.size();i++)
        {
            sb.append("\n");
            sb.append(i+1);sb.append("º: ");sb.append(aux2.get(i));
            //sb.append("\t");sb.append(aux.get(i));
            //i++;
        }
        return sb.toString();
    }*/
    
    /**
     * Atualizar classificacao campeonato
     */
    /*public void atualizarClassificacao()
    {
            int i = this.prova-1;
            Set<Carro> aux = this.corridas.get(i).getResultados();
            int x=4, old_value;
            for(Carro c : aux)
            {    
                if(!(c instanceof Hibrido))
                {
                old_value = 0;
                String g = c.getMarca()+" "+c.getModelo() +" \t"+c.getEquipa().getNome()+" \t"+c.getClass().getName();
                if(this.classificacao.containsKey(g))
                {
                    old_value = this.classificacao.get(g);
                }
                if(x==4)
                {
                    this.classificacao.put(g, old_value+16);
                }
                if(x==3)
                {
                   this.classificacao.put(g, old_value+8);
                }
                if(x==2)
                {
                   this.classificacao.put(g, old_value+4);
                }
                if(x==1)
                {
                   this.classificacao.put(g, old_value+2);
                }
                if(x==0)
                {
                   this.classificacao.put(g, old_value+1);
                }
                if(x<0)
                {
                   this.classificacao.put(g, 0+old_value); 
                }
                x--;
                }
            }
            
            Map<Carro,Integer> aux2 = this.corridas.get(i).getDNF();
            for(Carro q : aux2.keySet())
            {
                if(!(q instanceof Hibrido))
                {
                    old_value = 0;
                    String a = q.getMarca()+" "+q.getModelo() +" \t"+q.getEquipa().getNome()+" \t"+q.getClass().getName();
                    if(this.classificacao.containsKey(a))
                        old_value = this.classificacao.get(a);
                    this.classificacao.put(a,0+old_value);
                }
            }
    }*/
    
    /**
     * Atualizar classificacao campeonato hibrido
     */
    /*public void atualizarClassificacaoHibrido()
    {
            int i = this.prova-1;
            Set<Carro> aux = this.corridas.get(i).getResultados();
            int x=4, old_value;
            for(Carro c : aux)
            { 
                if(c instanceof Hibrido)
                {
                old_value = 0;
                String g = c.getMarca()+" "+c.getModelo() +" \t"+c.getEquipa().getNome()+" \t"+c.getClass().getName();
                if(this.classificacaoH.containsKey(g))
                {
                    old_value = this.classificacaoH.get(g);
                }
                if(x==4)
                {
                    this.classificacaoH.put(g, old_value+16);
                }
                if(x==3)
                {
                   this.classificacaoH.put(g, old_value+8);
                }
                if(x==2)
                {
                   this.classificacaoH.put(g, old_value+4);
                }
                if(x==1)
                {
                   this.classificacaoH.put(g, old_value+2);
                }
                if(x==0)
                {
                   this.classificacaoH.put(g, old_value+1);
                }
                if(x<0)
                {
                   this.classificacaoH.put(g, 0+old_value); 
                }
                x--;
                }
            }
            
            Map<Carro,Integer> aux2 = this.corridas.get(i).getDNF();
            for(Carro q : aux2.keySet())
            {
                if(q instanceof Hibrido)
                {
                    old_value = 0;
                    String a = q.getMarca()+" "+q.getModelo() +" \t"+q.getEquipa().getNome()+" \t"+q.getClass().getName();
                    if(this.classificacaoH.containsKey(a))
                        old_value = this.classificacaoH.get(a);
                    this.classificacaoH.put(a,0+old_value);
                }
            }
    }
    
    private List<Map.Entry<String, Integer>> ordenaClassificacao(Map<String,Integer> classificacao)
    {
        List<Map.Entry<String, Integer>> ordenado = new ArrayList<Map.Entry<String, Integer>>(classificacao.entrySet());
        Collections.sort(ordenado, new Comparator<Map.Entry<String, Integer>>() 
        {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) 
            {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        return ordenado;
    }
    
    /**
     * Info corrida x
     */
    /*public String resultadosCorrida(int x)
    {
        //StringBuilder sb = new StringBuilder();
        //if(this.prova < x)
        //{
          //  String s = ("\nA prova escolhida não existe ou ainda não foi realizada!");
            //return s;
        //}
        //else
        //{
        return this.getCorrida(x).printResultados();
        //}
        //return sb.toString();
    }*/
    
    /**
     * Verificar se corrida já foi realizada
     */
    public boolean corridaRealizada(int x)
    {
        return ((x-1) < this.prova);
    }
    
    /**
     * Lista Carros a participar em proxa nr x
     */
    /*public String listaParticipantes(int x)
    {
        StringBuilder sb = new StringBuilder();
        Corrida aux = this.corridas.get((x-1));
        sb.append(aux.listaCarrosParticipantes());
        return sb.toString();
    }*/
    
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

    public void configCampeonato(){

    }
}
