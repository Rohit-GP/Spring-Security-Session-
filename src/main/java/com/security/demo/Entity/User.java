package com.security.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class User {

    @Id
    private String id;
    
    private String username;
    private String email;
    private String password;
    private String role;
}