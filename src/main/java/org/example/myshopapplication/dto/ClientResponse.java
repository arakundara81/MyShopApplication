package org.example.myshopapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myshopapplication.model.ContactMethod;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    private Long id;
    private String name;
    private Set<ContactMethod> contactMethods;



}
