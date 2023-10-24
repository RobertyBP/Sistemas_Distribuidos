package org.example.model;

public class User {
    private boolean isAdmin;
    private String name;
    private String email;
    private String password;
    public User(){};
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(Boolean isAdmin, String name, String email, String password) {
        this.isAdmin = isAdmin;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "isAdmin=" + isAdmin +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin(){return  isAdmin;}
}
