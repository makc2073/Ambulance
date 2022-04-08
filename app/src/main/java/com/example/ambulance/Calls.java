package com.example.ambulance;

public class Calls {
    public String Adress,Brigade_number,Firstname,Lastname,Secondname, Age, Phone, Date, Time, Description, Status, Key;

    public Calls() {
    }

    public Calls(String adress, String brigade_number, String firstname, String lastname, String secondname, String age, String phone, String date, String time, String description, String status, String key) {

        this.Adress = adress;
        this.Brigade_number = brigade_number;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Secondname = secondname;
        this.Age = age;
        this.Phone = phone;
        this.Date = date;
        this.Time = time;
        this.Description = description;
        this.Status = status;
        this.Key = key;
    }
}
