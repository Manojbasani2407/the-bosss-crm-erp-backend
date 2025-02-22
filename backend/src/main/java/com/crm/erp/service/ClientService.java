package com.crm.erp.service;

import com.crm.erp.exception.ResourceNotFoundException;
import com.crm.erp.model.Client;
import com.crm.erp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing clients.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    /**
     * Retrieve all clients.
     *
     * @return List of all clients.
     */
    public List<Client> getAllClients() {
        LOGGER.info("Fetching all clients");
        return clientRepository.findAll();
    }

    /**
     * Retrieve a client by ID.
     *
     * @param id Client ID.
     * @return Found client.
     */
    public Client getClientById(Long id) {
        LOGGER.info("Fetching client with ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
    }

    /**
     * Create a new client.
     *
     * @param client Client entity to be saved.
     * @return Saved client.
     */
    @Transactional
    public Client addClient(Client client) {
        LOGGER.info("Adding new client: {}", client.getName());
        return clientRepository.save(client);
    }

    /**
     * Update an existing client.
     *
     * @param id Client ID.
     * @param clientDetails Updated client details.
     * @return Updated client.
     */
    @Transactional
    public Client updateClient(Long id, Client clientDetails) {
        LOGGER.info("Updating client with ID: {}", id);

        Client client = getClientById(id);
        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());
        client.setPhone(clientDetails.getPhone());
        client.setAddress(clientDetails.getAddress());

        return clientRepository.save(client);
    }

    /**
     * Delete a client by ID.
     *
     * @param id Client ID.
     */
    @Transactional
    public void deleteClient(Long id) {
        LOGGER.info("Deleting client with ID: {}", id);

        Client client = getClientById(id);
        clientRepository.delete(client);

        LOGGER.info("Client with ID {} has been deleted successfully", id);
    }
}
