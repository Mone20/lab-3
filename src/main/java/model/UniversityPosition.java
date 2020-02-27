package model;

public class UniversityPosition {
    private int id;

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    private String position;
    public UniversityPosition(int i,String p)
    {
        this.id=i;
        this.position=p;
    }
}
