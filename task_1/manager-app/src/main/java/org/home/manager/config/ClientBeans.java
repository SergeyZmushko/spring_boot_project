package org.home.manager.config;

import org.home.manager.client.RestClientProductsRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${home.service.catalogue.uri:http://localhost:8081}") String catalogBaseUri,
            @Value("${home.service.catalogue.username:}") String catalogUsername,
            @Value("${home.service.catalogue.password:}") String catalogPassword) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(catalogUsername, catalogPassword))
                .build());
    }
}
