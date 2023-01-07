package Model;

import java.util.*;
//import java.util.TreeMap;
//import java.util.Arrays;
import java.io.Serializable;

public class Corrida implements Serializable {
    //variaveis de instancia
    private List<Carro> listaCarros;
    private List<Piloto> listaPilotos;
    private Circuito circuito;
    private List<String> listaUsernames;
    private Set<Carro> resultados;
    //private Map<Carro,Long> bestLap;
    private List<Carro> primeiroVolta;
    private Map<Carro, Integer> dnf;
    private int clima; //1-chove 0-sol

    //Construtores
    public Corrida() {
        this.listaCarros = new ArrayList<Carro>();
        this.listaPilotos = new ArrayList<Piloto>();
        this.circuito = new Circuito();
        this.resultados = new TreeSet<Carro>();
        //this.bestLap = new HashMap<Carro,Long>();
        this.primeiroVolta = new ArrayList<Carro>();
        this.dnf = new HashMap<Carro,Integer>();
        Random rand=new Random();
        int x=rand.nextInt(2);
        this.clima = x;
        this.listaUsernames = new ArrayList<>();
    }


    public Corrida(List<Carro> lc, List<Piloto> lp, Circuito c, int clima, List<String> lu) {
        this();
        this.listaCarros.addAll(lc);
        this.listaPilotos.addAll(lp);
        this.circuito = c.clone();
        this.clima = clima;
        this.listaUsernames.addAll(lu);
    }



    public Corrida(Corrida c) {
        this.listaCarros = c.getCarros();
        this.circuito = c.getCircuito();
        this.resultados = c.getResultados();
        this.primeiroVolta = c.getPrimeiroVolta();
        this.dnf = c.getDNF();
        this.clima = c.getClima();
    }


    //Gets e sets
    public List<Carro> getCarros() {
        ArrayList<Carro> aux = new ArrayList<Carro>();
        for(Carro c: this.listaCarros) {
            aux.add(c.clone());
        }
        return aux;
    }

    public Circuito getCircuito()
    {
       return this.circuito.clone();
    }


    public Set<Carro> getResultados() {
        TreeSet<Carro> aux = new TreeSet<Carro>();
        for(Carro c : this.resultados) {
            aux.add(c.clone());
        }
        return aux;
    }

    public Map<Carro,Integer> getDNF() {
       HashMap<Carro,Integer> aux = new HashMap<Carro,Integer>();
        for(Carro c : this.dnf.keySet()) {
            aux.put(c.clone(), this.dnf.get(c));
        }
        return aux;
    }

    public int getClima()
    {
       return this.clima;
    }


    public List<Carro> getPrimeiroVolta()
    {
        ArrayList<Carro> aux = new ArrayList<Carro>();
        for(Carro c : this.primeiroVolta) {
            aux.add(c.clone());
        }
        return aux;
    }

    public String simulaCorrida(){
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> grid = new ArrayList<>();
        for(int i = 0; i < this.listaUsernames.size();i++){
            grid.add(i);
        }
        Collections.reverse(grid);

        for(int i = 0; i < this.circuito.getVoltas();i++){
            sb.append(simulaVolta(i,grid));
            sb.append("\nFim da volta ").append(i+1).append("\n");
            int pos = 1;
            for(Integer j : grid){
                sb.append(pos).append("º-").append(this.listaPilotos.get(j).getNome()).append("(").append(this.listaUsernames.get(j)).append(") \n");
                pos+=1;
            }
            sb.append("\n");
        }
        System.out.println(sb);
        return sb.toString();
    }


    public String simulaVolta(int volta, List<Integer> lastLap){
        StringBuilder log = new StringBuilder();
        LinkedHashMap<String,Integer> lgdu = this.circuito.getLayoutGDU();
        Random rand = new Random();
        int split = 0;
        for(Map.Entry<String,Integer> gdu : lgdu.entrySet()) { //GDU: 0-possivel;1-dificl;2-impossivel//possivel 50% dificl 25%
            if(gdu.getValue() < 2) {
                if(gdu.getValue() == 1) split = 25;
                else split = 50;
                for (int i = 0; i < lastLap.size()-1; i++) {
                    int rnd = rand.nextInt(100);
                    //Alterar split de acordo com valores dos carros pilotos clima etc...

                    if (rnd < split) {
                        Collections.swap(lastLap, i, i + 1);
                        log.append("In ").append(gdu.getKey()).append(" - ").append(this.listaPilotos.get(lastLap.get(i)).getNome()).append("(").append(this.listaUsernames.get(lastLap.get(i))).append(") ").append("Ultrapassou ").append(this.listaPilotos.get(lastLap.get(i+1)).getNome()).append("(").append(this.listaUsernames.get(lastLap.get(i+1))).append(") ").append("\n");
                    }
                }
            }
        }

        return log.toString();
    }


    public void setCircuito(Circuito c)
    {
       this.circuito = c.clone();
    }

    //Metodos

    public Corrida clone()
    {
       return new Corrida(this);
    }

    /**
     * Adicionar um carro á lista
     */
    public void adicionarCarro(Carro c)
    {
        this.listaCarros.add(c.clone());
    }

    /**
     * adicionar lista de carros
     */
    public void adicionarCarro(List<Carro> l) {
        for(Carro c : l)
        {
            this.listaCarros.add(c.clone());
        }
    }

    /**
     * Numero total de carros
     */
    public int totalCarros()
    {
        return this.listaCarros.size();
    }

    /**
     * Remover um carro
     */
    public void removerCarro(Carro c)
    {
        this.listaCarros.remove(c);
    }

    /**
     * Limpa a lista de carros
     */
    public void limpaListaCarros(){
        this.listaCarros.clear();
    }



    /**
    * Lista de Acidentados DNF
    */
    public String printDNF()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Espetados!!!");
        for(Carro c : this.dnf.keySet()) {
            sb.append("\n" + c.getMarca() + " \t\tVolta: " + this.dnf.get(c));
        }
        return sb.toString();
    }


}
