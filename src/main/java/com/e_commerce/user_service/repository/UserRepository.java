package com.e_commerce.user_service.repository;
import com.e_commerce.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
}
