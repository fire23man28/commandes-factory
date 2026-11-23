package com.example.commandes.controller;

import com.example.commandes.dto.CommandeDTO;
import com.example.commandes.dto.CommandeRequest;
import com.example.commandes.service.CommandeService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService service;

    public CommandeController(CommandeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CommandeDTO> creer(@Valid @RequestBody CommandeRequest request) {
        CommandeDTO created = service.creer(request);
        return ResponseEntity.created(URI.create("/api/commandes/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public CommandeDTO trouver(@PathVariable Long id) {
        return service.trouver(id);
    }

    @PostMapping("/{id}/expedition")
    public CommandeDTO expedier(@PathVariable Long id) {
        return service.expedier(id);
    }

    @PostMapping("/{id}/annulation")
    public CommandeDTO annuler(@PathVariable Long id) {
        return service.annuler(id);
    }
}
