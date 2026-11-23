package com.example.commandes.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Données envoyées par le client pour créer une commande. */
public record CommandeRequest(
        @NotBlank String client,
        @NotNull @DecimalMin("0.01") BigDecimal montant
) {
}
