package org.example.myshopapplication.repository;

import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.model.Client;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientRepository {


    private final Map<Long, Client> clients = new HashMap<>();


    public void addClient(Client newClient) {
        clients.put(newClient.getId(), newClient);
    }

    public Client findClientById(Long id) {
        return clients.get(id);
    }


    public List<Client> findAllClients() {
        return clients.values().stream()
                .filter(client -> client.getRole() == Roles.REGULAR )
                .toList();
    }

    public void deleteClient(Client client) {
        clients.remove(client.getId());
    }
}
