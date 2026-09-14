package com.example.demarches.repository;

import com.example.demarches.model.TokenQRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenQRCodeRepository extends JpaRepository<TokenQRCode, Long> {
    Optional<TokenQRCode> findByToken(String token);
}
