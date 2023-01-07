package Model;

import java.util.Random;

public class GT extends Carro
{
    public GT()
    {
        super();
    }
    
    public GT(String marca, String modelo, int cilindrada, int potencia)
    {
        super(marca,modelo,cilindrada,potencia,0.0);
        int d = (int) ((4000-cilindrada)*0.0075);
        this.setFiabilidade(90-d);
    }
    
    public GT(GT p)
    {
        super(p);
    }
    
    public GT clone()
    {
        return new GT(this);
    }
    
    public boolean DNF(int volta, int clima) {
       Random rand=new Random();
       int x=rand.nextInt(100);
       int desgaste = (int)(volta + 1); //1% a cada volta
       return (x > (this.getFiabilidade() - desgaste));
    }
    
     
    public boolean equals(Object o)
    {
        if(this==o)
            return true;
        
        if(o==null || this.getClass()!=o.getClass())
            return false;
        
        GT c = (GT) o;
        return ( super.equals(c));
    }
}
