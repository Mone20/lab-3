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



}
