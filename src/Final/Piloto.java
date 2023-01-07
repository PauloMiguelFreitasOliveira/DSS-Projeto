package Final;
/**
 * Write a description of class Piloto here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.io.Serializable;

public class Piloto implements Serializable
{
    public String nome;
    public String nacionalidade;
    public Double cts;
    public Double sva;
    
    //Construtores
    public Piloto()
    {
        this.nome = "";
        this.nacionalidade = "";
        this.cts = 0.0;
        this.sva = 0.0;
    }
    
    public Piloto(String nome, String nacionalidade, Double cts, Double sva)
    {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.cts = cts;
        this.sva = sva;
    }
    
    public Piloto(Piloto p)
    {
        this.nome = p.getNome();
        this.nacionalidade = p.getNacionalidade();
        this.cts = p.getCTS();
        this.sva = p.getSVA();
    }
    
    //Gets e Sets
    public String getNome()
    {
        return this.nome;
    }
    
    public String getNacionalidade()
    {
        return this.nacionalidade;
    }
    
    public Double getCTS()
    {
        return this.cts;
    }
    
    public Double getSVA()
    {
        return this.sva;
    }
        
    public void setNome(String nome)
    {
        this.nome = nome;
    }
    
    public void setNacionalidade(String nacionalidade)
    {
        this.nacionalidade = nacionalidade;
    }
    
    public void setCTS(Double cts)
    {
        this.cts = cts;
    }
    
    public void setSVA(Double sva)
    {
        this.sva = sva;
    }
    
    //Metodos usuais
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nNome: "); sb.append(this.nome);
        sb.append("\tNacionalidade: ");sb.append(this.nacionalidade);
        sb.append("\tCTS: ");sb.append(this.cts);
        sb.append("\tSVA: ");sb.append(this.sva);
        return sb.toString();
    }
    
    public Piloto clone()
    {
        return new Piloto(this);
    }
    
    public boolean ectss(Object o)
    {
        if(this == o)
        return true;
        
        if((o == null) || (this.getClass() != o.getClass()))
        return false;
        
        Piloto p = (Piloto) o;
        return (this.nome.equals(p.getNome()) && 
                this.nacionalidade.equals(p.getNacionalidade()) &&
                this.cts==p.getCTS() &&
                this.sva==p.getSVA());
    }
}
