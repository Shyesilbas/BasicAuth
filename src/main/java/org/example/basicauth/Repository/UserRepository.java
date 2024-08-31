package org.example.basicauth.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.basicauth.Model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);


}
