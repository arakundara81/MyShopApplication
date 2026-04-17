package org.example.myshopapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myshopapplication.model.ContactMethod;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {

    @Schema(example = "987654321")
    @NotNull
    private Long clientId;
    @Schema(example = "John Lennon")
    @NotBlank
    private String name;
    @NotNull
    @Valid
    private Set<ContactMethod> contactMethods;
}