package org.example.myshopapplication.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myshopapplication.dto.ClientResponse;
import org.example.myshopapplication.dto.CreateClientRequest;
import org.example.myshopapplication.enums.MethodTypes;
import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.model.PurchasedProduct;
import org.example.myshopapplication.service.AuthService;
import org.example.myshopapplication.service.ClientPurchasedProductService;
import org.example.myshopapplication.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
@Validated
public class ClientController {


    private final ClientService clientService;
    private final AuthService authService;
    private final ClientPurchasedProductService clientPurchasedProductService;

    @Operation(
            summary = "Create new client",
            description = "Creates a new client with REGULAR role. Client is initially not authenticated."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Client created successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Client already exists",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/add")
    public ResponseEntity<ClientResponse> createNewClient(@RequestBody @Valid CreateClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createNewClient(request));
    }


    @Operation(
            summary = "Authenticate client",
            description = "Authenticates a client using a contact method (EMAIL or PHONE). If the provided method matches one of the client's stored contact methods, authentication is successful."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "400", description = "Invalid input or enum value")
    })
    @PostMapping("/auth")
    public ResponseEntity<String> createClientAuthentication(    @Parameter(
                                                                             description = "Client ID",
                                                                             example = "987654321"
                                                                     )
                                                                     @RequestParam Long clientId,

                                                                 @Parameter(
                                                                         description = "Authentication method type",
                                                                         example = "EMAIL"
                                                                 )
                                                                     @RequestParam MethodTypes methodType,

                                                                 @Parameter(
                                                                         description = "Authentication value (email or phone)",
                                                                         example = "john@example.com"
                                                                 )
                                                                     @RequestParam String methodValue ) {

       if (authService.isClientAuthenticationCreatedSuccessfully(clientId, methodType, methodValue)) {
           return ResponseEntity.ok("authentication passed");
       }
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
    }

    @Operation(
            summary = "Purchase product",
            description = "Allows an authenticated REGULAR client to purchase a product from the catalog. The product is added to the client's purchased products list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase successful"),
            @ApiResponse(responseCode = "401", description = "Client is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Client does not have REGULAR role"),
            @ApiResponse(responseCode = "409", description = "Product already purchased by client")
    })

    @PostMapping("/{clientId}/purchase/{productId}")
    public ResponseEntity<String> purchaseProduct(  @Parameter(
                                                                description = "ID of the client performing the purchase",
                                                                example = "987654321"
                                                        )
                                                        @PathVariable Long clientId,

                                                    @Parameter(
                                                            description = "ID of the product to purchase",
                                                            example = "12345670"
                                                    )
                                                        @PathVariable Long productId) {
        authService.checkAuthorizedClient(clientId, Roles.REGULAR);
        clientPurchasedProductService.purchaseProduct(clientId, productId);
        return ResponseEntity.ok("Purchase successful");
    }

    @Operation(
            summary = "Delete purchased product from client",
            description = "Allows an ADMIN to remove a purchased product from a specific client."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "403", description = "Client is not authorized as ADMIN"),
            @ApiResponse(responseCode = "404", description = "Product not found for this client")
    })
    @DeleteMapping("/{clientId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromClient(@PathVariable Long clientId, @PathVariable Long productId, @RequestParam Long adminId) {
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        clientPurchasedProductService.deletePurchasedProductFromClient(clientId, productId);
        return ResponseEntity.ok("product " + productId + " is deleted successfully from client " + clientId);
    }
    @Operation(
            summary = "Delete client",
            description = "Allows an ADMIN to completely remove a client from the system including all purchased products."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Client is not authorized as ADMIN"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteClientByAdmin(@PathVariable Long clientId, @RequestParam Long adminId) {
        authService.checkAuthorizedClient(adminId, Roles.ADMIN);
        clientPurchasedProductService.deleteClientFromTheSystem(clientId);
        return ResponseEntity.ok("Client  " + clientId + " is deleted successfully from the system ");
    }


    @Operation(
            summary = "Get client purchased products",
            description = "Returns all products purchased by an authenticated REGULAR client."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Client is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Client does not have REGULAR role")
    })
    @GetMapping("/{clientId}/products")
    public ResponseEntity<Set<PurchasedProduct>> getClientPurchasedProducts(@PathVariable Long clientId) {
        authService.checkAuthorizedClient(clientId, Roles.REGULAR);
        Set<PurchasedProduct> clientProducts = clientPurchasedProductService.getPurchasedProductsByClientId(clientId);
        return ResponseEntity.ok(clientProducts);
    }


}
