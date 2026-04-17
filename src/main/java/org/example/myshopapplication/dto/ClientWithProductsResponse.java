package org.example.myshopapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.model.ContactMethod;
import org.example.myshopapplication.model.PurchasedProduct;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithProductsResponse {
    private Long clientId;
    private String name;
    private Set<ContactMethod> contactMethod;
    private Roles role;
    private boolean isAuthenticated;
    private Set<PurchasedProduct> purchasedProducts;
}
