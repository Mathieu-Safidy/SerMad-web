package com.example.demarches.service;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final TokenQRCodeRepository tokenQRCodeRepository;
    private final DemandeRepository demandeRepository;
    private final UserRepository userRepository;

    public String genererQRCode(Long demandeId) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        String token = UUID.randomUUID().toString();

        TokenQRCode tokenQR = TokenQRCode.builder()
                .token(token)
                .demande(demande)
                .user(demande.getUser())
                .dateExpiration((double) (Instant.now().toEpochMilli() + 3600000))
                .estUtilise(false)
                .build();

        tokenQRCodeRepository.save(tokenQR);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    token,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du QR Code", e);
        }
    }

    public boolean validerQRCode(String token) {
        TokenQRCode tokenQR = tokenQRCodeRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (tokenQR.getEstUtilise()) {
            throw new RuntimeException("QR Code déjà utilisé");
        }

        if (tokenQR.getDateExpiration() < Instant.now().toEpochMilli()) {
            throw new RuntimeException("QR Code expiré");
        }

        tokenQR.setEstUtilise(true);
        tokenQRCodeRepository.save(tokenQR);

        return true;
    }
}
