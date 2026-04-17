package org.example.myshopapplication.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myshopapplication.dto.ClientWithProductsResponse;
import org.example.myshopapplication.dto.CreateProductRequest;
import org.example.myshopapplication.dto.UpdateProductRequest;
import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.model.Product;
import org.example.myshopapplication.service.AuthService;
import org.example.myshopapplication.service.ClientPurchasedProductService;
import org.example.myshopapplication.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final AuthService authService;
    private final ClientPurchasedProductService clientPurchasedProductService;

    @Operation(
            summary = "Get all products",
            description = "Retrieves all available products from the catalog."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public List<Product> getAllCatalogProducts() {
        return productService.getAllProductsFromCatalog();
    }

    @Operation(
            summary = "Add product to catalog",
            description = "Allows an ADMIN to add a new product to the product catalog."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "403", description = "Client is not authorized as ADMIN"),
            @ApiResponse(responseCode = "409", description = "Product already exists in catalog")
    })
    @PostMapping("/addToCatalog")
    public ResponseEntity<String>  createNewProductToCatalog(@RequestBody @Valid CreateProductRequest createProductRequest, @RequestParam Long adminId){
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        productService.addProductToCatalog(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("product " + createProductRequest.getProductId() + " is added successfully to Catalog");
    }
    @Operation(
            summary = "Update product",
            description = "Allows an ADMIN to update product details such as name or price."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "403", description = "Client is not authorized as ADMIN"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<String> updateProductInCatalog(@PathVariable Long productId, @RequestBody UpdateProductRequest updateProductRequest, @RequestParam Long adminId) {
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        productService.updateProductInCatalog(productId, updateProductRequest);
        return ResponseEntity.ok("product " + productId + " is updated successfully");
    }


    @Operation(
            summary = "Delete product",
            description = "Allows an ADMIN to remove a product from the catalog."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Client is not authorized as ADMIN"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductInCatalog(@PathVariable Long productId, @RequestParam Long adminId) {
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        productService.deleteProductFromCatalog(productId);
        return ResponseEntity.ok("product " + productId + " is deleted successfully");
    }


    @Operation(
            summary = "Get clients with products",
            description = "Allows ADMIN to retrieve all clients with their purchased products."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clients retrieved",
                    content = @Content(schema = @Schema(implementation = ClientWithProductsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized as ADMIN",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/clients")
    public ResponseEntity<List<ClientWithProductsResponse>> getAllClientsWithProducts(@RequestParam Long adminId) {
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        List<ClientWithProductsResponse> result = clientPurchasedProductService.getAllClientsWithProducts();
        return ResponseEntity.ok(result);
    }

}
