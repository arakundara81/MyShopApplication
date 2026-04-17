package org.example.myshopapplication.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.myshopapplication.model.Client;
import org.example.myshopapplication.model.Product;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
@ConfigurationProperties(prefix = "product-catalog")
@Getter
@Setter
public class ProductCatalogConfig {
    private List<Product> products;
    private List<Client>  admins;

    @PostConstruct
    public void checkConfigurationsArePopulated(){
        if(CollectionUtils.isEmpty(products)){
            log.warn("Product catalog is EMPTY - no products were loaded from configuration");
        }

        if(CollectionUtils.isEmpty(admins)){
            log.warn("Admins list is EMPTY - no admins were loaded from configuration");
        }

        log.info("All configurations are loaded successfully");
    }
}
