package org.example.myshopapplication.repository;

import lombok.Getter;
import org.example.myshopapplication.model.PurchasedProduct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@Getter
public class ClientProductsRepository {

    private final Map<Long, Set<PurchasedProduct>>  clientIdProductsMap = new HashMap<>();


    public Set<PurchasedProduct> getProductsByClient(Long clientId) {
        return clientIdProductsMap.computeIfAbsent(clientId, k -> new HashSet<>());
    }

    public void deleteClientWithHisAllPurchasedProduct(Long clientId){
        clientIdProductsMap.remove(clientId);
    }




}
