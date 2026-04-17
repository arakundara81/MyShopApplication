package org.example.myshopapplication.service;


import lombok.RequiredArgsConstructor;
import org.example.myshopapplication.dto.ClientResponse;
import org.example.myshopapplication.dto.CreateClientRequest;
import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.exception.GenericException;
import org.example.myshopapplication.model.Client;
import org.example.myshopapplication.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    public ClientResponse createNewClient(CreateClientRequest newClientRequest) {

        Client existingClient = clientRepository.findClientById(newClientRequest.getClientId());
        if(existingClient == null) {
            Client client = new Client(
                    newClientRequest.getClientId(),
                    newClientRequest.getName(),
                    newClientRequest.getContactMethods(),
                    false,
                    Roles.REGULAR
            );
            clientRepository.addClient(client);
            return mapToClientResponse(client);
        }
        throw new GenericException("Client " + newClientRequest.getClientId() + " already exists", HttpStatus.CONFLICT);
    }

    private ClientResponse mapToClientResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getContactMethods()
        );
    }


    public Client getClientById(Long clientId) {
       Client client =   clientRepository.findClientById(clientId);
        if(client == null) {
            throw new GenericException("Client " + clientId + " is not found", HttpStatus.NOT_FOUND);
        }
        return client;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAllClients();
    }


    public void removeClient(Long clientId) {
        Client client  = getClientById(clientId);
        clientRepository.deleteClient(client);


    }
}
