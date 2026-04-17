package org.example.myshopapplication.service;

import lombok.RequiredArgsConstructor;
import org.example.myshopapplication.enums.MethodTypes;
import org.example.myshopapplication.enums.Roles;
import org.example.myshopapplication.exception.GenericException;
import org.example.myshopapplication.model.Client;
import org.example.myshopapplication.model.ContactMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final ClientService clientService;


    public void checkAuthorizedClient(Long clientId, Roles roleToValidate){

        Client client = clientService.getClientById(clientId);

        if(!client.isAuthenticated()) {
            throw new GenericException("Client " + clientId +
                    " is not authenticated ", HttpStatus.UNAUTHORIZED);
        }
        if (client.getRole() != roleToValidate) {
            throw new GenericException("Client " + clientId+" does not have required role: " + roleToValidate, HttpStatus.FORBIDDEN);
        }
    }

    public boolean isClientAuthenticationCreatedSuccessfully(Long clientId, MethodTypes methodType, String methodValue) {

        Client candidateAuthenticatedClient = clientService.getClientById(clientId);
        if (candidateAuthenticatedClient.getContactMethods().contains(new ContactMethod(methodType, methodValue))) {
            candidateAuthenticatedClient.setAuthenticated(true);
            return true;
        }
        candidateAuthenticatedClient.setAuthenticated(false);
        return false;

    }

}
