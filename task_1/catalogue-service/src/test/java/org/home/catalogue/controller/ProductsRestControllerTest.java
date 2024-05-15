package org.home.catalogue.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductsRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/products.sql")
    void findProducts_ReturnsProductsList() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "title": "Товар №1", "details":"Описание товар №1" },
                                {"id": 2, "title": "Шоколадка", "details":"Черный шоколад" },
                                {"id": 3, "title": "Клавиатура", "details":"Клавиатура механическая" },
                                {"id": 4, "title": "Мышка", "details":"Описание мышки" }
                                ]
                                """)
                );
    }

    @Test
    @Sql("/sql/products.sql")
    void findProductsWithFilter_ReturnsProductsList() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
                .param("filter", "товар")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        this.mockMvc.perform(requestBuilder)

                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "title": "Товар №1", "details":"Описание товар №1" }
                                ]
                                """)
                );
    }

    @Test
    @Sql("/sql/createTable.sql")
    void createProduct_RequestIsValid_ReturnsNewProduct() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Новый товар для теста", "details": "Новое описание товара"}""")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                header().string(HttpHeaders.LOCATION, "http://localhost/catalogue-api/products/1"),
                        content().json("""
                                {
                                "id": 1,
                                "title": "Новый товар для теста",
                                "details": "Новое описание товара"
                                }
                                """)
                );

    }

    @Test
    @Sql("/sql/createTable.sql")
    void createProduct_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": " ", "details": null}""")
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                "errors": ["size must be between 3 and 50"]
                                }
                                """)
                );

    }

    @Test
    @Sql("/sql/createTable.sql")
    void createProduct_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": " ", "details": null}""")
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );

    }

}