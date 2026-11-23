package com.example.commandes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCommande statut;

    protected Commande() {
        // requis par JPA
    }

    public Commande(String client, BigDecimal montant) {
        this.client = client;
        this.montant = montant;
        this.statut = StatutCommande.CREEE;
    }

    /** Règle : une commande annulée ne peut pas être expédiée. */
    public void expedier() {
        if (statut == StatutCommande.ANNULEE) {
            throw new CommandeInvalideException("Une commande annulée ne peut pas être expédiée");
        }
        statut = StatutCommande.EXPEDIEE;
    }

    /** Règle : une commande expédiée ne peut pas être annulée. */
    public void annuler() {
        if (statut == StatutCommande.EXPEDIEE) {
            throw new CommandeInvalideException("Une commande expédiée ne peut pas être annulée");
        }
        statut = StatutCommande.ANNULEE;
    }

    public Long getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public StatutCommande getStatut() {
        return statut;
    }
}
