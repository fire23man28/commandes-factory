package com.example.commandes.service;

import com.example.commandes.dto.CommandeDTO;
import com.example.commandes.dto.CommandeRequest;
import com.example.commandes.model.Commande;
import com.example.commandes.model.CommandeNotFoundException;
import com.example.commandes.repository.CommandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommandeService {

    private final CommandeRepository repository;

    public CommandeService(CommandeRepository repository) {
        this.repository = repository;
    }

    public CommandeDTO creer(CommandeRequest request) {
        Commande commande = repository.save(new Commande(request.client(), request.montant()));
        return toDto(commande);
    }

    @Transactional(readOnly = true)
    public CommandeDTO trouver(Long id) {
        return toDto(charger(id));
    }

    public CommandeDTO expedier(Long id) {
        Commande commande = charger(id);
        commande.expedier();
        return toDto(commande);
    }

    public CommandeDTO annuler(Long id) {
        Commande commande = charger(id);
        commande.annuler();
        return toDto(commande);
    }

    private Commande charger(Long id) {
        return repository.findById(id).orElseThrow(() -> new CommandeNotFoundException(id));
    }

    private static CommandeDTO toDto(Commande commande) {
        return new CommandeDTO(
                commande.getId(),
                commande.getClient(),
                commande.getMontant(),
                commande.getStatut());
    }
}
