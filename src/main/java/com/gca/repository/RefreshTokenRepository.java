package com.gca.repository;

import com.gca.model.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.token = :token")
    void deleteByToken(@Param("token") String token);
}
