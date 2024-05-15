package org.home.manager.controller;

import org.home.manager.client.ProductsRestClient;
import org.home.manager.controller.payload.NewProductPayload;
import org.home.manager.entity.Product;
import org.home.manager.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductsRestClient productsRestClient;

    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage(){
        //given
        var payload = new NewProductPayload("Новый товар", "Описание товара");
        var model = new ConcurrentModel();

        doReturn(new Product(1, "Новый товар", "Описание товара"))
                .when(this.productsRestClient)
                .createProduct("Новый товар", "Описание товара");

        //when
        var result = this.controller.createProduct(payload, model);
        //then

        assertEquals("redirect:/catalogue/products/1", result);
        verify(this.productsRestClient).createProduct("Новый товар", "Описание товара");
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    @DisplayName("createProduct не создаст новый товар и вернет страницу с ошибками")
    void createProduct_RequestIsValid_ReturnsProductFormWithErrors(){
        //given
        var payload = new NewProductPayload(" ", null);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.productsRestClient)
                .createProduct(" ", null);

        //when
        var result = this.controller.createProduct(payload, model);
        //then

        assertEquals("catalogue/products/new-product", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));

        verify(this.productsRestClient).createProduct(" ", null);
        verifyNoMoreInteractions(this.productsRestClient);
    }
}