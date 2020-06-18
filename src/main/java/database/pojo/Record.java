package database.pojo;

public class Record {
    private String surname;
    private String workout;
    private String record;
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }



    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
