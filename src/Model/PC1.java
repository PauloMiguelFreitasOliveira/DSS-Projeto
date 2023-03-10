package Model;
/**
 * Write a description of class PC1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.Map;
import java.util.Random;

public class PC1 extends Carro
{
    public PC1()
    {
        super();
    }
    
    public PC1(String marca, String modelo, int cilindrada, int potencia)
    {
        super(marca,modelo,cilindrada,potencia,0.0);
        this.setFiabilidade(95);
    }
    
    public PC1(PC1 p)
    {
        super(p);
    }
    
    public PC1 clone()
    {
        return new PC1(this);
    }
    
    public boolean DNF(int volta,int totalvoltas,int clima)
    {
       Random rand=new Random();
       int x=rand.nextInt(100);
       return (x > super.getFiabilidade());
    }
    
    public boolean equals(Object o)
    {
        if(this==o)
        return true;
        
        if(o==null || this.getClass()!=o.getClass())
        return false;
        
        PC1 c = (PC1) o;
        return ( super.equals(c));
    }
}
