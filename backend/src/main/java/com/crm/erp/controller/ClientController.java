package com.crm.erp.controller;

import com.crm.erp.model.Client;
import com.crm.erp.service.ClientService;
import com.crm.erp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    /**
     * Retrieves all clients.
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        log.info("Fetching all clients...");
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Retrieves a specific client by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        log.info("Fetching client with ID: {}", id);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /**
     * Retrieves invoices for a specific client.
     */
    @GetMapping("/{id}/invoices")
    public ResponseEntity<?> getClientInvoices(@PathVariable Long id) {
        log.info("Fetching invoices for client ID: {}", id);
        try {
            return ResponseEntity.ok(clientService.getInvoicesByClientId(id));
        } catch (ResourceNotFoundException e) {
            log.error("Client not found: {}", e.getMessage());
            return ResponseEntity.status(404).body("Client not found.");
        }
    }

    /**
     * Adds a new client.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        log.info("Adding new client: {}", client.getName());
        return ResponseEntity.ok(clientService.addClient(client));
    }

    /**
     * Updates an existing client.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody Client client) {
        log.info("Updating client ID: {}", id);
        try {
            return ResponseEntity.ok(clientService.updateClient(id, client));
        } catch (ResourceNotFoundException e) {
            log.error("Update failed: {}", e.getMessage());
            return ResponseEntity.status(404).body("Client not found.");
        }
    }

    /**
     * Deletes a client by ID.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        log.warn("Deleting client ID: {}", id);
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok("Client deleted successfully!");
        } catch (ResourceNotFoundException e) {
            log.error("Delete failed: {}", e.getMessage());
            return ResponseEntity.status(404).body("Client not found.");
        }
    }
}
