package model;

public class Admin extends Person{

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "ADMIN");
    }

    @Override
    public boolean validateEmail(String email) {

        return email.matches("^[a-zA-Z0-9+_.-]+@shop\\.com$");
    }
}
