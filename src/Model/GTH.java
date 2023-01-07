package Model;
/**
 * Write a description of class GTH here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.Random;

public class GTH extends GT implements Hibrido{
    private int motor_eletrico;
    
    public GTH() {
        super();
        this.motor_eletrico = 0;
    }
    
    public GTH(String marca, String modelo, int cilindrada, int potencia,int eletrico) {
        super(marca,modelo,cilindrada,potencia);
        this.motor_eletrico = eletrico;
        int d = (int) ((4000-cilindrada)*0.0075);
        this.setFiabilidade(90-d);
    }
    
    public GTH(GTH p)
    {
        super(p);
        this.motor_eletrico = p.getPotenciaMotorEletrico();
    }
    
    public GTH clone()
    {
        return new GTH(this);
    }
    
    public int getPotenciaMotorEletrico()
    {
        return this.motor_eletrico;
    }
    
    public void setPotenciaMotorEletrico(int potencia)
    {
        this.motor_eletrico = potencia;
    }
    
    public boolean DNF(int volta,int totalvoltas,int clima)
    {
        Random rand=new Random();
        int x=rand.nextInt(100);
        int motorh = this.getPotenciaMotorEletrico()/30;
        int desgaste = (int)(volta + 1); //1% a cada volta
        return (x > (this.getFiabilidade() - desgaste - motorh));
    }
    
    public boolean equals(Object o)
    {
        if(this==o)
        return true;
        
        if(o==null || this.getClass()!=o.getClass())
        return false;
        
        GTH c = (GTH) o;
        return ( super.equals(c) && this.motor_eletrico == c.getPotenciaMotorEletrico());
    }   
}
