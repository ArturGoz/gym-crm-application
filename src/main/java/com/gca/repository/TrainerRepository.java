package com.gca.repository;

import com.gca.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t JOIN FETCH t.user u WHERE u.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);
}
