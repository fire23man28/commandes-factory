package com.example.commandes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.commandes.dto.CommandeDTO;
import com.example.commandes.dto.CommandeRequest;
import com.example.commandes.model.Commande;
import com.example.commandes.model.CommandeInvalideException;
import com.example.commandes.model.CommandeNotFoundException;
import com.example.commandes.model.StatutCommande;
import com.example.commandes.repository.CommandeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests unitaires : le repository est remplacé par un mock Mockito. */
@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock
    CommandeRepository repository;

    @InjectMocks
    CommandeService service;

    @Test
    void creerUneCommandeLaMetEnStatutCree() {
        when(repository.save(any(Commande.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommandeDTO dto = service.creer(new CommandeRequest("Alice", new BigDecimal("120.50")));

        assertEquals("Alice", dto.client());
        assertEquals(StatutCommande.CREEE, dto.statut());
    }

    @Test
    void expedierUneCommandeCreeeLaPasseEnExpediee() {
        Commande commande = new Commande("Alice", new BigDecimal("120.50"));
        when(repository.findById(1L)).thenReturn(Optional.of(commande));

        CommandeDTO dto = service.expedier(1L);

        assertEquals(StatutCommande.EXPEDIEE, dto.statut());
    }

    @Test
    void expedierUneCommandeAnnuleeEchoue() {
        Commande commande = new Commande("Alice", new BigDecimal("120.50"));
        commande.annuler();
        when(repository.findById(1L)).thenReturn(Optional.of(commande));

        assertThrows(CommandeInvalideException.class, () -> service.expedier(1L));
    }

    @Test
    void listerRenvoieToutesLesCommandes() {
        when(repository.findAll()).thenReturn(List.of(
                new Commande("Alice", new BigDecimal("120.50")),
                new Commande("Bob", new BigDecimal("80.00"))));

        List<CommandeDTO> dtos = service.lister();

        assertEquals(2, dtos.size());
        assertEquals("Bob", dtos.get(1).client());
    }

    @Test
    void trouverUneCommandeInconnueLeveUneNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CommandeNotFoundException.class, () -> service.trouver(99L));
    }
}
