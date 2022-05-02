package com.sin.helper.gameEngine.physics;

public class Material {
    private double density;
    private double tempCapacity;
    private double melt;
    private double boil;

    public static int solid=-1;
    public static int liquid=-2;
    public static int gas=-3;

    public Material(double density, double tempCapacity, double melt, double boil) {
        this.density = density;
        this.tempCapacity = tempCapacity;
        this.melt = melt;
        this.boil = boil;
    }

    public static Material water=new Material(998,4.18,0.0,100.0);
    public static Material oakWood=new Material(700,1.45,solid,solid);
    public static Material iron=new Material(7.874,0.450,1540,2750);
    public static Material air=new Material(1.29,1.01,-213,-193);

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public double getTempCapacity() {
        return tempCapacity;
    }

    public void setTempCapacity(double tempCapacity) {
        this.tempCapacity = tempCapacity;
    }

    public double getMelt() {
        return melt;
    }

    public void setMelt(double melt) {
        this.melt = melt;
    }

    public double getBoil() {
        return boil;
    }

    public void setBoil(double boil) {
        this.boil = boil;
    }
}
