package org.example;

public class Admin extends Person{

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "ADMIN");
    }
}
