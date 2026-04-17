package org.example.myshopapplication.model;

import lombok.*;
import org.example.myshopapplication.enums.Roles;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {
    @EqualsAndHashCode.Include
    private  Long   id;
    private  String name;
    private  Set<ContactMethod> contactMethods;
    private  boolean isAuthenticated;
    private  Roles  role;
}
