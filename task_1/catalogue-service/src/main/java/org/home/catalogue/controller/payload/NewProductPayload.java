package org.home.catalogue.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(

        @NotNull
        @Size(min = 3, max = 50)
        String title,

        @Size(min = 3, max = 1000)
        String details) {
}
