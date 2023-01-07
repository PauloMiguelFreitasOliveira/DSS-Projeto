package Model;
import java.util.Random;

public class PC2 extends Carro
{
    private int preparacao_mecanica;
    
    public PC2()
    {
        super();
    }
    
    public PC2(String marca, String modelo, int cilindrada, int potencia,int p_mecanica)
    {
        super(marca,modelo,cilindrada,potencia,0.0);
        this.preparacao_mecanica = p_mecanica;
        int d = (int) ((5000-cilindrada)*0.0075);
        this.setFiabilidade(80-d);
    }
    
    public PC2(PC2 p)
    {
        super(p);
        this.preparacao_mecanica = p.getPreparacaoMecaninca();
    }
    
    public int getPreparacaoMecaninca()
    {
        return this.preparacao_mecanica;
    }
    
    public PC2 clone()
    {
        return new PC2(this);
    }
    
    public boolean DNF(int volta,int totalvoltas,int clima) {
       Random rand=new Random();
       int x=rand.nextInt(100);
       int fiabilidade = super.getFiabilidade() + (super.getCilindrada()/1200) + (this.preparacao_mecanica/10);
       return (x > fiabilidade);
    }
    
    public boolean equals(Object o)
    {
        if(this==o)
            return true;
        
        if(o==null || this.getClass()!=o.getClass())
            return false;
        
        PC2 c = (PC2) o;
        return ( super.equals(c)
                && this.preparacao_mecanica == c.getPreparacaoMecaninca());
    }
}
