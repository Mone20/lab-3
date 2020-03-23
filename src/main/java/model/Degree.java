package model;

public class Degree {
    private int id;

    public int getId() {
        return id;
    }

    public String getDegree() {
        return degree;
    }

    private String degree;
    public Degree(int i,String d)
    {
        this.id=i;
        this.degree=d;
    }
}
