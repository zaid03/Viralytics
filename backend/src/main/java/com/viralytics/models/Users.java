package com.viralytics.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints= @UniqueConstraint(name = "ux_users_email", columnNames = { "email" }))
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Users() {}

    @PrePersist
    protected void onCreate(){
        if  (createdAt == null) createdAt = Instant.now(); 
    }

    public Integer getId() { return id; }
    public void setId(Integer id){ this.id = id; }

    public String getName() { return name; }
    public void setName(String name){ this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email){ this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password){ this.password = password; }   
    
     public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; } 
}
