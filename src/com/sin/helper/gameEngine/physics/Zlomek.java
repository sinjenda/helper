package com.sin.helper.gameEngine.physics;

public class Zlomek extends Number implements Cloneable{
    private int citatel;
    private int jmenovatel;

    public Zlomek(int citatel, int jmenovatel) {
        this.citatel = citatel;
        this.jmenovatel = jmenovatel;
        toBasicForm();
    }

    public void multiply(Zlomek zlomek){
        citatel=citatel* zlomek.citatel;
        jmenovatel=jmenovatel* zlomek.jmenovatel;
        toBasicForm();
    }

    public void add(Zlomek zlomek){
        try {
           Zlomek z= zlomek.clone();
           z.jmenovatel=z.jmenovatel*jmenovatel;
           z.citatel=z.citatel*jmenovatel;
           jmenovatel= zlomek.jmenovatel*jmenovatel;
           citatel=citatel* zlomek.jmenovatel;
           citatel=citatel+z.citatel;
           toBasicForm();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subtract(Zlomek zlomek){
        try {
            Zlomek z = zlomek.clone();
            z.jmenovatel = z.jmenovatel * jmenovatel;
            z.citatel = z.citatel * jmenovatel;
            jmenovatel = zlomek.jmenovatel * jmenovatel;
            citatel = citatel * zlomek.jmenovatel;
            citatel=citatel-z.citatel;
            toBasicForm();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void divide(Zlomek zlomek){
        zlomek.flip();
        multiply(zlomek);
    }

    public void flip(){
        int i=citatel;
        citatel=jmenovatel;
        jmenovatel=i;
    }

    public Zlomek(int citatel,Zlomek jmenovatel){
        Zlomek z=new Zlomek(citatel,1);
        z.divide(jmenovatel);
        this.citatel=z.citatel;
        this.jmenovatel=z.jmenovatel;
    }

    private void toBasicForm(){
        int u=citatel;
        int w=jmenovatel;
        while (w!=0){
            int r=u%w;
            u=w;
            w=r;
        }
        citatel=citatel/u;
        jmenovatel=jmenovatel/u;
    }

    @Override
    public int intValue() {
        return citatel/jmenovatel;
    }

    @Override
    public long longValue() {
        return citatel/jmenovatel;
    }

    @Override
    public float floatValue() {
        return (float) citatel/jmenovatel;
    }

    @Override
    public double doubleValue() {
        return (double) citatel/jmenovatel;
    }

    @Override
    public Zlomek clone() throws CloneNotSupportedException {
        return (Zlomek) super.clone();
    }
}
