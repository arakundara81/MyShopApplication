package org.example.myshopapplication.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PurchasedProduct {

    @EqualsAndHashCode.Include
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private LocalDateTime purchaseDate;
}
