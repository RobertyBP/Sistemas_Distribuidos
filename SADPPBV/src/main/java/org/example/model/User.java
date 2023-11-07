package org.example.model;

public class User {
    private String id;
    private boolean isAdmin;
    private String name;
    private String email;
    private String password;
    private String token;


    public User() {
    }

    ;


    public User(String id, Boolean isAdmin, String name, String email, String password, String token) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

}
