package sample.pojo;

import java.util.Date;

public class Contract {

    int id;
    Date dateContract;
    int number;
    Date dataChek;
    boolean chek;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateContract() {
        return dateContract;
    }

    public void setDateContract(Date dateContract) {
        this.dateContract = dateContract;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDataChek() {
        return dataChek;
    }

    public void setDataChek(Date dataChek) {
        this.dataChek = dataChek;
    }

    public boolean isChek() {
        return chek;
    }

    public void setChek(boolean chek) {
        this.chek = chek;
    }
}
