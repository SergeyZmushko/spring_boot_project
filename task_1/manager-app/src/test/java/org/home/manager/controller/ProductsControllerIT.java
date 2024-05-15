package org.home.manager.controller;

import com.github.tomakehurst.wiremock.client.WireMock;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.home.manager.config.TestingBeans;
import org.home.manager.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wiremock.com.google.common.net.HttpHeaders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestingBeans.class)
@AutoConfigureMockMvc
@WireMockTest(httpPort = 54321)
class ProductsControllerIT {

    @Autowired
    MockMvc mockMvc;

    //для запуска раскомментировтаь аннотацию @Primary в классе TestingBeans
    @Test
    void getProductsList_ReturnsProductsList() throws Exception{
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/list")
                .queryParam("filter", "товар")
                .with(user("s.mohito").roles("MANAGER"));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/catalogue-api/products"))
                .withQueryParam("filter", WireMock.equalTo("товар"))
                .willReturn(WireMock.ok("""
                        {"id": 1, "title": "Товар №1", "details": "Описание товара №1"},
                        {"id": 2, "title": "Товар №2", "details": "Описание товара №2"}
                        """)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        //when
        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/list"),
                        model().attribute("filter", "товар"),
                        model().attribute("products", List.of(

                                new Product(1, "Товар №1", "Описание товара №1"),
                                new Product(2, "Товар №2", "Описание товара №2")
                        ))

                );
    }

    @Test
    void getNewProductPage_ReturnsProductPage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/catalogue/products/create")
                .with(user("s.mohito").roles("MANAGER"));
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/new-product")
                );

    }

}
