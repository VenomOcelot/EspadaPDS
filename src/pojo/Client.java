package pojo;

import java.util.ArrayList;
import java.util.Date;

public class Client {
    private int idClient;
    private String name;
    private String surname;
    private Date birthDate;
    private String phone;
    private String address;
    private String gender;

    public Client(int idClient, String name, String surname, Date birthDate, String phone, String address, String gender) {
        this.idClient = idClient;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }


    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}