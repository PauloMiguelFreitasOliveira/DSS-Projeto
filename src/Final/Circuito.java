/**
 * Write a description of class Circuito here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
package Final;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class Circuito implements Serializable
{
    /* Variaveis instancia */
    private String nome;
    private int distancia;
    private Map<String,Integer> layoutGDU; //'ch'-2 (chincane com gdu de 2); 'c'-1 (curva com gdu de 1); 'r'-3 (reta com gdu de 3)
    private int voltas;

    /* Construtores */
    public Circuito(){
        this.nome = "";
        this.distancia = 0;
        this.layoutGDU = new HashMap<String, Integer>();
        this.voltas = 0;
    }
    
    public Circuito(String n,int d, Map<String, Integer> m, int v){
        this.nome = n;
        this.distancia = d;
        if(m == null){
            this.layoutGDU = new HashMap<String, Integer>();
        }
        else
        {
            HashMap<String,Integer> aux = new HashMap<String, Integer>();
            for(String g : m.keySet())
            {
                aux.put(g, m.get(g));
            }
            this.layoutGDU = aux;
        }
        this.voltas = v;
    }
    
    public Circuito(Circuito c){
        this.nome = c.getNome();
        this.distancia = c.getDistancia();
        this.layoutGDU = c.getLayoutGDU();
        this.voltas = c.getVoltas();
    }
    
    /* Gets e Sets */
    public String getNome(){
        return this.nome;
    }
    
    public int getDistancia(){
        return this.distancia;
    }
    
    public int getVoltas(){
        return this.voltas;
    }
    
    public Map<String,Integer> getLayoutGDU(){
        HashMap<String,Integer> aux = new HashMap<String, Integer>();
        for(String g : this.layoutGDU.keySet()){
            aux.put(g, this.layoutGDU.get(g));
        }
        return aux;
    }
    
    public void setNome(String n){
        this.nome = n;
    }
    
    public void setDistancia(int d){
        this.distancia = d;
    }
    
    public void setVoltas(int v){
        this.voltas = v;
    }
    
    public void setLayoutGDU(Map<String,Integer> m){
        this.layoutGDU = new HashMap<>();
        for(String g : m.keySet()){
            this.layoutGDU.put(g, m.get(g));
        }
    }
        
    /* Metodos usuais */
    public Circuito clone()
    {
        return new Circuito(this);
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nNome: ");sb.append(this.nome);
        sb.append("\nDistancia: ");sb.append(this.distancia);
        sb.append("\nNumero de voltas: ");sb.append(this.voltas);
        //sb.append("\nTempo Medio: ");sb.append(TimeConverter.toTimeFormat(this.tempoMedio));
        return sb.toString();
    }
    
    public boolean equals(Object o)
    {
       if(this == o)
       return true;
       
       if(o == null || this.getClass() != o.getClass())
       return false;
       
       Circuito c = (Circuito) o;
       return ( this.nome.equals(c.getNome()) &&
                this.distancia == c.getDistancia() &&
                this.voltas == c.getVoltas()
                //this.tempoMedio == c.getTempoMedio() &&
                );
    }
    
}
