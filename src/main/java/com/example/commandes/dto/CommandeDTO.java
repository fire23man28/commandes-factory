package com.example.commandes.dto;

import com.example.commandes.model.StatutCommande;
import java.math.BigDecimal;

/** Représentation d'une commande renvoyée au client. */
public record CommandeDTO(
        Long id,
        String client,
        BigDecimal montant,
        StatutCommande statut
) {
}
