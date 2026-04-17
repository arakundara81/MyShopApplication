package org.example.myshopapplication.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.example.myshopapplication.configuration.ProductCatalogConfig;
import org.example.myshopapplication.dto.CreateProductRequest;
import org.example.myshopapplication.dto.UpdateProductRequest;
import org.example.myshopapplication.exception.GenericException;
import org.example.myshopapplication.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCatalogConfig productCatalogConfig;

    public List<Product>  getAllProductsFromCatalog() {
        return productCatalogConfig.getProducts();
    }

    public Product getProductById(Long productId) {

            return productCatalogConfig.getProducts().stream()
                    .filter(productFromCatalog -> productFromCatalog.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new GenericException("Product " + productId + " is not found in the catalog", HttpStatus.NOT_FOUND));

    }

    public void updateProductInCatalog(Long productId, UpdateProductRequest updateProductRequest) {

        Product productForUpdate = getProductById(productId);
        if (Strings.isNotBlank(updateProductRequest.getProductName())) {
            productForUpdate.setProductName(updateProductRequest.getProductName());
        }

        if (updateProductRequest.getPrice() != null) {
            productForUpdate.setPrice(updateProductRequest.getPrice());
        }
    }

    public void deleteProductFromCatalog(Long productId) {

        Product productForUpdate = getProductById(productId);
        productCatalogConfig.getProducts().remove(productForUpdate);

    }

    public void addProductToCatalog(CreateProductRequest createProductRequest) {

        if(isProductExists(createProductRequest.getProductId())) {
            throw new GenericException("Product " + createProductRequest.getProductId() + " already exist - cannot be added to the catalog", HttpStatus.CONFLICT);
        }
        productCatalogConfig.getProducts().add(new Product(createProductRequest.getProductId(), createProductRequest.getProductName(), createProductRequest.getPrice()));
    }

    private boolean isProductExists(Long productId) {

        return productCatalogConfig.getProducts().stream()
                .anyMatch(productFromCatalog -> productFromCatalog.getProductId().equals(productId));
    }
}
