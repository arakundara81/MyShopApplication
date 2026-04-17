package org.example.myshopapplication.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myshopapplication.enums.MethodTypes;
import org.example.myshopapplication.validators.ValidContactMethod;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidContactMethod
public class ContactMethod {


    @Schema(example = "EMAIL")
    @NotNull
    private MethodTypes methodType;

    @Schema(example = "john@example.com")
    @NotEmpty
    private String methodValue;
}
