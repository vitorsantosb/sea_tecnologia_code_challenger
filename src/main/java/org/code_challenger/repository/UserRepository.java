package org.code_challenger.repository;

import org.code_challenger.repository.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByCpf(String cpf);

    boolean existsByCpf(String cpf);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.emails WHERE u.id = :id")
    List<User> findAllWithEmailsAndPhones();
}
