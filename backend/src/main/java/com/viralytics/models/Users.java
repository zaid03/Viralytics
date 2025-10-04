package com.viralytics.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    public Users() {}

    public Integer getid() { return id; }
    public void setid(Integer id){ this.id = id; }

    public String getname() { return name; }
    public void setname(String name){ this.name = name; }

    public String getemail() { return email; }
    public void setemail(String email){ this.email = email; }

    public String getpassword() { return password; }
    public void setpassword(String password){ this.password = password; }    
}
