package org.example.myshopapplication.configuration;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.example.myshopapplication.model.Client;
import org.example.myshopapplication.repository.ClientRepository;
import org.springframework.stereotype.Component;

@Component
@Data
public class AdminLoader {

    private final ClientRepository clientRepository;
    private final ProductCatalogConfig productCatalogConfig;


    @PostConstruct
    public void loadAdmins() {
        for(Client client : productCatalogConfig.getAdmins()) {
            client.setAuthenticated(false);
            clientRepository.addClient(client);
        }
    }
}

