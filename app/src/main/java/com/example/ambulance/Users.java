package com.example.ambulance;
import java.io.Serializable;
public class Users implements Serializable {
    public String Brigades_number1, Login1, Password1, FirstName, LastName;

    public Users() {
    }
// метод который принимает  и возвращает значения при создании экземпляра класса
    public Users(String brigades_number1, String login1, String password1, String firstName, String lastName) {
        Brigades_number1 = brigades_number1;
        Login1 = login1;
        Password1 = password1;
        FirstName = firstName;
        LastName = lastName;
    }
}
