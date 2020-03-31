package model;

import java.util.Objects;

public class Worker {
    private String firstName;
    private String birthDate;
    private int id;
    private String lastName;
    private String middleName;

    public int getPositionId() {
        return positionId;
    }

    public int getDegreeId() {
        return degreeId;
    }

    public int getParentId() {
        return parentId;
    }

    private int positionId;
    private int degreeId;
    private int parentId;
public Worker()
{

}
    public Worker(String firstName, String birthDate, int id, String lastName, String middleName, int positionId, int degreeId, int parentId) {
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.id = id;
        this.lastName = lastName;
        this.middleName = middleName;
        this.positionId = positionId;
        this.degreeId = degreeId;
        this.parentId = parentId;
    }


    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getId() {
        return id;
    }



    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id == worker.id &&
                positionId == worker.positionId &&
                degreeId == worker.degreeId &&
                parentId == worker.parentId &&
                Objects.equals(firstName, worker.firstName) &&
                Objects.equals(birthDate, worker.birthDate) &&
                Objects.equals(lastName, worker.lastName) &&
                Objects.equals(middleName, worker.middleName);
    }


    public String getInf() {
        return this.getId()+"| "+this.getLastName()+" |"+"| "+this.getFirstName()+" |"+this.getMiddleName()+"|"+this.getBirthDate()+"|";
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, birthDate, id, lastName, middleName, positionId, degreeId, parentId);
    }
}
