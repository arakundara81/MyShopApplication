package org.example.myshopapplication.model;


import lombok.*;

import java.math.BigDecimal;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private Long productId;
    private String productName;
    private BigDecimal price;
}
