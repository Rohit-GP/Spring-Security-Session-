package com.security.demo.Repository;

import com.security.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    
    java.util.Optional<User> findByEmailIgnoreCase(String email);
}
