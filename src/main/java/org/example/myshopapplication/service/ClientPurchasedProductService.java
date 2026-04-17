package org.example.myshopapplication.service;

import lombok.RequiredArgsConstructor;
import org.example.myshopapplication.dto.ClientWithProductsResponse;
import org.example.myshopapplication.exception.GenericException;
import org.example.myshopapplication.model.Client;
import org.example.myshopapplication.model.Product;
import org.example.myshopapplication.model.PurchasedProduct;
import org.example.myshopapplication.repository.ClientProductsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientPurchasedProductService {

    private final ClientService clientService;
    private final ProductService productService;
    private final ClientProductsRepository clientProductsRepository;


    public List<ClientWithProductsResponse> getAllClientsWithProducts() {
        List<Client> allClients = clientService.getAllClients();
        Map<Long, Set<PurchasedProduct>> clientIdProductsMap = clientProductsRepository.getClientIdProductsMap();

        List<ClientWithProductsResponse> clientWithProductsResponseList = new ArrayList<>();

        for (Client client : allClients) {
            Set<PurchasedProduct> purchasedProducts = clientIdProductsMap.getOrDefault(client.getId(), new HashSet<>());

            ClientWithProductsResponse clientWithProductsResponseElement = new ClientWithProductsResponse(
                    client.getId(),
                    client.getName(),
                    client.getContactMethods(),
                    client.getRole(),
                    client.isAuthenticated(),
                    purchasedProducts);

            clientWithProductsResponseList.add(clientWithProductsResponseElement);

        }
        return clientWithProductsResponseList;
    }


    public Set<PurchasedProduct> getPurchasedProductsByClientId(Long clientId) {
        return clientProductsRepository.getProductsByClient(clientId);
    }

    public void purchaseProduct(Long clientId, Long productId) {

        Product productToBuy = productService.getProductById(productId);
        Set<PurchasedProduct> clientPurchasedProducts = this.getPurchasedProductsByClientId(clientId);
        if(isProductAlreadyPurchased(productId, clientPurchasedProducts)) {
            throw new GenericException(
                    "Client " + clientId + " already has this product " + productId,
                    HttpStatus.CONFLICT
            );
        }
        clientPurchasedProducts.add(new PurchasedProduct(productToBuy.getProductId(), productToBuy.getProductName(), productToBuy.getPrice(), LocalDateTime.now()));
    }

    private boolean isProductAlreadyPurchased(Long productId, Set<PurchasedProduct> purchasedProducts) {
        return purchasedProducts.stream()
                  .anyMatch(purchasedProduct -> purchasedProduct.getProductId().equals(productId));


    }

    public void deletePurchasedProductFromClient(Long clientId, Long productId) {

        Client existingClient = clientService.getClientById(clientId);
        Set<PurchasedProduct> purchasedProducts =  this.getPurchasedProductsByClientId(existingClient.getId());

        boolean isRemoved = purchasedProducts
                .removeIf(p -> Objects.equals(p.getProductId(), productId));

        if (!isRemoved) {
            throw new GenericException(
                    "Product " + productId + " is not purchased by client " + clientId,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    public void deleteClientFromTheSystem(Long clientId) {

        clientService.removeClient(clientId);
        clientProductsRepository.deleteClientWithHisAllPurchasedProduct(clientId);

    }

}
