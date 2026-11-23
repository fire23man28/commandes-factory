package com.example.commandes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Test d'intégration : l'application complète tourne contre un vrai PostgreSQL
 * lancé par Testcontainers. Flyway applique les migrations au démarrage.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CommandeControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    MockMvc mvc;

    @Test
    void creerPuisExpedierUneCommande() throws Exception {
        String reponse = mvc.perform(post("/api/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"client\":\"Alice\",\"montant\":120.50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("CREEE"))
                .andReturn().getResponse().getContentAsString();

        Number id = JsonPath.read(reponse, "$.id");

        mvc.perform(post("/api/commandes/" + id + "/expedition"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("EXPEDIEE"));
    }

    @Test
    void listerRenvoieLesCommandesCreees() throws Exception {
        mvc.perform(post("/api/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"client\":\"Claire\",\"montant\":45.00}"))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].client").value(org.hamcrest.Matchers.hasItem("Claire")));
    }

    @Test
    void creerAvecClientVideRenvoieBadRequest() throws Exception {
        mvc.perform(post("/api/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"client\":\"\",\"montant\":120.50}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void commandeInconnueRenvoieNotFound() throws Exception {
        mvc.perform(get("/api/commandes/999999"))
                .andExpect(status().isNotFound());
    }
}
